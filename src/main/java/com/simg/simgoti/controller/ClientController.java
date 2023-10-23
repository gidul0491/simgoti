package com.simg.simgoti.controller;

import com.simg.simgoti.dto.*;
import com.simg.simgoti.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.net.URLEncoder;
import java.util.*;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
    private final CommonService commonService;
    private final EmailService emailService;
    private final HanaPremiumImpl hanaPremium;
    private final SMS sms;

    // calculate 페이지에서 보험료 확인 버튼을 눌렀을 때 db의 coverage 테이블에서 coverage code, 담보 한글이름, 하루당 보험료, 최소 보험료 정보인 coverageTabList와
    // 담보에 포함되는 세부담보 코드, 세부담보 이름, 세부담보 보장금액 정보인 coverageList를 result라는 변수명에 담아서 반환
    @RequestMapping(value = "/coverageList", method = RequestMethod.GET)
    public Object selectCoverageList(@RequestParam String startDt, @RequestParam String endDt, @RequestParam String clntBirth, @RequestParam char clntGen) throws Exception {
        Map<String, Object> resp = new HashMap<>();
        int insAge;
        try {
            insAge = commonService.calculateInsAge(clntBirth);
        } catch (Exception e) {
            resp.put("msg", "생년월일을 정확히 입력해주세요.");
            resp.put("result", "error");
            return resp;
        }
        int manAge = commonService.calculateManAge(clntBirth);
        char isOver19 = 'Y';
        if(manAge <19){
            isOver19 = 'N';
        }

        String validateInsDate = commonService.validateInsDate(startDt, endDt);

        if (manAge < 0 || manAge > 79) {
            resp.put("msg", "만 0세부터 79세까지 가입 가능합니다.");
            resp.put("result", "error");
            return resp;
        } else if (!validateInsDate.equals("ok")) {
            resp.put("msg", validateInsDate);
            resp.put("result", "error");
            return resp;
        } else {
            int travelDay = commonService.getPeriod(startDt, endDt);
            Map<String, List> data = new HashMap<>();
            data.put("coverageList", clientService.selectCoverageList(isOver19));
            data.put("coverageTabList", clientService.selectCoverageTypeList(insAge, clntGen, travelDay, isOver19));

            resp.put("data", data);
            resp.put("result", "success");
            return resp;
        }
    }

    // apply1Alone 페이지에서 확인 버튼을 누르면 실행
    // 입력한 정보를 세션에 추가, 입력한 정보를 이용하여 db에 저장 혹은 업데이트(이메일/전화번호), db에서 사용하는 pk값을 세션에 추가
    @RequestMapping(value = "/cliInfo", method = RequestMethod.POST)
    public Object addAppSession(@RequestParam String covCode,@RequestParam String startDt, @RequestParam String endDt, @RequestParam String clntNm, @RequestParam String clntJuminA, @RequestParam String clntJuminB, @RequestParam String clntPhone, @RequestParam String clntEmail, @RequestParam int clntCnt, HttpServletRequest req) throws Exception {
        HttpSession session = req.getSession();
        session.setAttribute("clntNm", clntNm);
        session.setAttribute("clntJuminA", clntJuminA);
        session.setAttribute("clntJuminB", clntJuminB);
        session.setAttribute("clntPhone", clntPhone);
        session.setAttribute("clntEmail", clntEmail);
        session.setAttribute("clntCnt", clntCnt);
        session.setMaxInactiveInterval(1800);

        String clntGen = clntJuminB.substring(0, 1);
        // 주민번호
        String clntJumin = clntJuminA + clntJuminB;

        int clntPk = clientService.insertOrUpdateClient(clntNm, clntJuminA, clntGen, clntJumin, clntPhone, clntEmail);
        session.setAttribute("clntPk", clntPk);

        char isOver19 = 'Y';
        if(commonService.calculateManAge(clntJuminA) < 19){
            isOver19 = 'N';
        }
        // 동승자리스트로 세션에 저장(마지막 총보험료계산을위함)
        List<CompanionDto> list = new ArrayList<>();
        int repPrem = hanaPremium.selectByCovAgeGen(covCode, commonService.calculateInsAge(clntJuminA), commonService.getGen(clntJuminB), commonService.getPeriod(startDt, endDt));
        CompanionDto rep = new CompanionDto("대표", clntNm, clntJuminA, clntJuminB.substring(0, 1) + "******",clientService.selectCovNm(covCode), repPrem, clntPk, isOver19);
        list.add(rep);
        session.setAttribute("companion",list);
        System.out.println("개인정보동의 : " + list);

        return "ok";
    }

    // apply1Many 에서 보험료 산출 버튼, 신청 버튼을 누르면 실행
    // 보험료 산출버튼일때(calOrApl == "cal" 일때) : 여행일수, 담보 종류를 이용하여 피보험자 한명한명 보험료 계산 후 dto 리스트를 반환
    // 신청 버튼일때(calOrApl == "apl" 일때) : 대표가입자 정보를 세션에 저장 후 db에 저장, 동반가입자 정보를 한명한명 db에 저장하고 리스트를 세션에 저장
    @RequestMapping(value = "/cliInfoMany", method = RequestMethod.POST)
    public Object cliInfoMany(@RequestParam String data, @RequestParam String covCode, @RequestParam String startDt, @RequestParam String endDt, @RequestParam String clntNm, @RequestParam String clntJuminA, @RequestParam String clntJuminB, @RequestParam String clntPhone, @RequestParam String clntEmail, @RequestParam int clntCnt, HttpServletRequest req, @RequestParam String calOrApl) throws Exception {
        HttpSession session = req.getSession();
        List<CompanionDto> list = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = null;
        try {
            jsonArray = (JSONArray) jsonParser.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
            return "json parsing failed";
        }
        // 대표가입자의 주민번호
        String repClntJumin = clntJuminA + clntJuminB;

        int period = commonService.getPeriod(startDt, endDt);
//        int repPrem = clientService.getPremium(covCode, commonService.calculateInsAge(clntJuminA), commonService.getGen(clntJuminB), period, 1); // 대표가입자의 보험료
        int repPrem = hanaPremium.selectByCovAgeGen(covCode, commonService.calculateInsAge(clntJuminA), commonService.getGen(clntJuminB), period);
        char repIsOver19 = 'Y';
        if(commonService.calculateManAge(clntJuminA) < 19){
            repIsOver19 = 'N';
        }

        // 대표가입자 처리
        CompanionDto rep = new CompanionDto();
        if (calOrApl.equals("cal")) {
            rep = new CompanionDto("대표", clntNm, clntJuminA, clntJuminB.substring(0, 1) + "******",clientService.selectCovNm(covCode), repPrem, 0,repIsOver19);
            rep.setCovDList(clientService.selectCovDList(covCode,repIsOver19));
        } else if (calOrApl.equals("apl")) {
            // 대표가입자 정보를 db에 저장/업데이트
            int repClntPk = clientService.insertOrUpdateClient(clntNm, clntJuminA, clntJuminB.substring(0, 1), repClntJumin, clntPhone, clntEmail);

            // 대표가입자 정보를 세션의 동반가입자 리스트에 추가
            rep = new CompanionDto("대표", clntNm, clntJuminA, clntJuminB.substring(0, 1) + "******",clientService.selectCovNm(covCode), repPrem, repClntPk, repIsOver19);
            rep.setCovDList(clientService.selectCovDList(covCode,repIsOver19));
            session.setAttribute("clntPk", repClntPk);
            session.setAttribute("clntNm", clntNm);
            session.setAttribute("clntJuminA", clntJuminA);
            session.setAttribute("clntJuminB", clntJuminB);
            session.setAttribute("clntPhone", clntPhone);
            session.setAttribute("clntEmail", clntEmail);
            session.setAttribute("clntCnt", clntCnt);
            session.setMaxInactiveInterval(1800);
        }
        list.add(rep);

        // 동반가입자 처리
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject item = (JSONObject) jsonArray.get(i);

            String num = item.get("companionNum").toString();
            String nm = item.get("companionNm").toString();
            String juminA = item.get("companionJuminA").toString();
            String juminB = item.get("companionJuminB").toString();

//            int prem = clientService.getPremium(covCode, commonService.calculateInsAge(juminA),commonService.getGen(juminB), period, 1); // 동반가입자의 보험료
            int prem = hanaPremium.selectByCovAgeGen(covCode, commonService.calculateInsAge(juminA),commonService.getGen(juminB), period);

            String gen = juminB.substring(0, 1);
            String jumin = juminA + juminB;
            char isOver19 = 'Y';
            if(commonService.calculateManAge(juminA)<19){
                isOver19 = 'N';
            }

            CompanionDto dto = new CompanionDto();
            if (calOrApl.equals("cal")) {
                dto = new CompanionDto(num, nm, juminA, juminB.substring(0, 1) + "******",clientService.selectCovNm(covCode), prem, 0, isOver19);
                dto.setCovDList(clientService.selectCovDList(covCode,isOver19));
            } else if (calOrApl.equals("apl")) {
                // 동반가입자 정보를 db에 저장후 pk 반환
                int clntPk = clientService.insertOrUpdateClient(nm, juminA, gen, jumin, null, null);

                dto = new CompanionDto(num, nm, juminA, juminB.substring(0, 1) + "******",clientService.selectCovNm(covCode), prem, clntPk, isOver19);
                dto.setCovDList(clientService.selectCovDList(covCode,isOver19));
            }
            ;
            list.add(dto);
            System.out.println("동반가입자 "+ dto);
        }

        if (calOrApl.equals("apl")) {
            session.setAttribute("companion", list);
            session.setMaxInactiveInterval(1800);
            return "ok";
        }

        return list;
    }

    // apply2 페이지에서 모두 아니오 체크 후 여행지 선택하고 확인버튼 클릭시 실행됨
    // 여행목적, 여행출발/종료일시, 담보코드, 여행지를 세션에 저장
    @RequestMapping(value = "/trInfo", method = RequestMethod.POST)
    public String addPurposeSession(@RequestParam String trPurpose, @RequestParam String startDt, @RequestParam String endDt, @RequestParam String covCode, @RequestParam String trPlace, HttpServletRequest req) {
        HttpSession session = req.getSession();
        session.setAttribute("trPurpose", trPurpose);
        session.setAttribute("startDt", startDt);
        session.setAttribute("endDt", endDt);
        session.setAttribute("covCode", covCode);
        session.setAttribute("trPlace", trPlace);
        session.setMaxInactiveInterval(1800);
        System.out.printf("보험가입 정보 요약 : 이름 %s 주민앞자리 %s 주민뒷자리 %s 폰 %s 이메일 %s 담보 %s 여행자수 %s 여행장소 %s 여행목적 %s 여행출발 %s 여행종료 %s \n",
                session.getAttribute("clntNm"),
                session.getAttribute("clntJuminA"),
                session.getAttribute("clntJuminB"),
                session.getAttribute("clntPhone"),
                session.getAttribute("clntEmail"),
                session.getAttribute("covCode"),
                session.getAttribute("clntCnt"),
                session.getAttribute("trPlace"),
                session.getAttribute("trPurpose"),
                session.getAttribute("startDt"),
                session.getAttribute("endDt")
        );
        return "ok";
    }

    // apply3 페이지가 열리자마자 실행됨
    // 세션에서 보험가입신청정보, 동승자정보, 대표가입자정보를 꺼내와서 반환해줌
    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    public Object getAppSum(HttpServletRequest req) throws Exception {
        HttpSession session = req.getSession();
        ApplyFinalCheckDto applyFinalCheckDto = new ApplyFinalCheckDto();
        try {
            int clntCnt = (int) session.getAttribute("clntCnt");
            int period = commonService.getPeriod(session.getAttribute("startDt").toString(), session.getAttribute("endDt").toString());
            String covCode = (String) session.getAttribute("covCode");

            // 총 보험료
            // 세션에서 동승자 리스트를 불러와 for문으로 모든 동승자 보험료의 합을 대입
//            int totalPrem = clientService.getPremium(session.getAttribute("covCode").toString(), commonService.calculateInsAge(session.getAttribute("clntJuminA").toString()),commonService.getGen("clntJuminB"), period, clntCnt);
            int totalPrem = 0;
            List<CompanionDto> list = (ArrayList<CompanionDto>) session.getAttribute("companion");
            for(CompanionDto item : list){
                totalPrem += item.getPrem();
            }
            session.setAttribute("totalPrem", totalPrem);

            session.setMaxInactiveInterval(1800);

            // 결제페이지에 표시될 보험계약정보를 dto에 추가
            ApplySummaryDto appSummaryDto = new ApplySummaryDto(
                    period,
                    session.getAttribute("startDt").toString(),
                    session.getAttribute("endDt").toString(),
                    session.getAttribute("trPlace").toString(),
                    covCode,
                    clntCnt,
                    totalPrem
            );
            applyFinalCheckDto.setAppSummaryDto(appSummaryDto);

            // 결제페이지에 표시될 동승자 명단을 dto에 추가, 동승자가 2인 이상일시 추가됨
            List<CompanionDto> companionDtoList = new ArrayList<>();
            if (session.getAttribute("companion") != null && (int) session.getAttribute("clntCnt") > 1) {
                companionDtoList = (ArrayList<CompanionDto>) session.getAttribute("companion");
                for(int i=0; i<companionDtoList.size(); i++){
                    char isOver19 = companionDtoList.get(i).getIsOver19();
                    companionDtoList.get(i).setCovDList(clientService.selectCovDList(covCode, isOver19));
                }

                applyFinalCheckDto.setCompanionDtoList(companionDtoList);
            }
            applyFinalCheckDto.setCompanionDtoList(companionDtoList);

            // 결제페이지에 표시될 대표가입자정보를 dto에 추가
            ClientDto repClientDto = new ClientDto();
            repClientDto.setClntNm(session.getAttribute("clntNm").toString());
            String resi = session.getAttribute("clntJuminA") +"-"+ session.getAttribute("clntJuminB").toString().substring(0, 1) + "******";
            repClientDto.setClntJumin(resi);
            repClientDto.setClntPhone(session.getAttribute("clntPhone").toString());
            repClientDto.setClntEmail(session.getAttribute("clntEmail").toString());
            applyFinalCheckDto.setRepClientDto(repClientDto);
        } catch (Exception e) {
            return e;
        }
        return applyFinalCheckDto;
    }

    // apply3 페이지에서 무통장입금 선택하면 실행됨
    // 결제 마감시각, 계좌정보를 리스트로 반환
    @RequestMapping(value = "/payAccount", method = RequestMethod.GET)
    public Object getPayAccount(HttpServletRequest req) throws Exception {
        HttpSession session = req.getSession();
        List<AccDto> accDtoList = new ArrayList<>();

//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime dueDt;
//        if(now.getHour() >= 22){
//            dueDt = now.withHour(23).withMinute(0).withSecond(0).withNano(0).plusDays(1);
//        }
//        else{
//            dueDt = now.withHour(23).withMinute(0).withSecond(0).withNano(0);
//        }
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//        String dueDtStr = dueDt.format(formatter);
//        session.setAttribute("dueDt", dueDtStr);

        String dueDtStr = session.getAttribute("startDt").toString();
        session.setAttribute("dueDt", dueDtStr);

        AccDto acc1 = new AccDto("하나은행", "25791068283907", "박준호", dueDtStr);
        accDtoList.add(acc1);

        AccDto acc2 = new AccDto("신한은행", "110501566550", "박준호", dueDtStr);
        accDtoList.add(acc2);

        return accDtoList;
    }


    // 로그인정보 제외 모든 세션정보를 삭제
    @RequestMapping(value = "/allSession", method = RequestMethod.DELETE)
    public void deleteAllSession(HttpServletRequest req) throws Exception {
        HttpSession session = req.getSession();
        int myPageClntPk = -1;
        if(session.getAttribute("myPageClntPk") != null){
            myPageClntPk = (int) session.getAttribute("myPageClntPk");
        }
        session.invalidate();
        session = req.getSession();
        if(myPageClntPk != -1){
            session.setAttribute("myPageClntPk",myPageClntPk);
            session.setMaxInactiveInterval(1800);
        }
    }

    // apply3 페이지에서 무통장입금 선택 후 은행까지 선택하면 실행됨
    // 결제데이터, 가입자명단데이터, 가입신청데이터를 db에 저장
    @RequestMapping(value = "/applyFinish", method = RequestMethod.POST)
    public Object applyFinish(@RequestParam String accBank, @RequestParam String accNum, @RequestParam String accNm, HttpServletRequest req) throws Exception {
        HttpSession session = req.getSession();
        try {
            int totalPrem = Integer.parseInt(session.getAttribute("totalPrem").toString());
            String dueDt = session.getAttribute("dueDt").toString();
            int covCode = Integer.parseInt(session.getAttribute("covCode").toString());
            // 결제 정보 저장
            int payPk = clientService.insertApplyPayment(totalPrem, dueDt, accBank, accNum, accNm);
            // 가입신청 정보 저장
            String polNo = commonService.selectPolNoByCovCode(covCode);
            int comCode = 1; // 업체코드는 1로 설정
            int insComCode = 101; // 보험사코드는 101로 설정
            int clntPk = Integer.parseInt(session.getAttribute("clntPk").toString());
            int trPurpose = Integer.parseInt(session.getAttribute("trPurpose").toString());
            String trPlace = session.getAttribute("trPlace").toString();
            String trFromDt = session.getAttribute("startDt").toString();
            String trToDt = session.getAttribute("endDt").toString();
            int clntCnt = Integer.parseInt(session.getAttribute("clntCnt").toString());
            int premium = Integer.parseInt(session.getAttribute("totalPrem").toString());
            int aplPk = clientService.insertApplyFinish(payPk, polNo, comCode, insComCode, clntPk, trPurpose, trPlace, trFromDt, trToDt, covCode, clntCnt, premium);

            // 가입자명단정보 저장
            // companion 리스트를가져와 for문 돌리기
            if (session.getAttribute("companion") != null) {
                List<CompanionDto> companionDtoList = (ArrayList<CompanionDto>) session.getAttribute("companion");
                for (int i = 0; i < companionDtoList.size(); i++) {
                    int comClntPk = companionDtoList.get(i).getClntPk();
                    int prem = companionDtoList.get(i).getPrem();
                    char repYN = 'N';
                    if(i == 0){
                        repYN = 'Y';
                    }
                    char isOver19 = companionDtoList.get(i).getIsOver19();
                    clientService.insertApplyInsuredList(aplPk, comClntPk, prem, repYN, isOver19);
                }
            }
            // 동승자가 없는 경우 - 대표가입자만
//            else{
//                clientService.insertApplyInsuredList(aplPk, clntPk, totalPrem, 'Y');
//            }

            return "ok";
        } catch (Exception e) {
            System.out.println(e);
            return e;
        }
    }

    // 인증번호 발송후 세션에 저장
    @RequestMapping(value = "/checkNum", method = RequestMethod.POST)
    public String checkNum(@RequestParam String phone, HttpServletRequest req) throws Exception {
        HttpSession session = req.getSession();


        if (phone.length() == 11) {

            // 문자 발송
//            String checkNum = sms.certificationSms(phone);
//            session.setAttribute("checkNum",checkNum);
//            System.out.println(phone + " 인증번호 : "+ checkNum);

            // 테스트
            session.setAttribute("checkNum", "000000");
            System.out.println("인증번호 : "+session.getAttribute("checkNum"));


            session.setMaxInactiveInterval(301);
            return "success";
        } else {
            return "fail";
        }

    }

    @RequestMapping(value = "/checkNumCheck", method = RequestMethod.POST)
    public String checkNumCheck(@RequestParam String checkNum, @RequestParam String clntJuminA, @RequestParam String clntPhone, HttpServletRequest req) throws Exception {
        HttpSession session = req.getSession();
        String num = (String) session.getAttribute("checkNum");
        List<Integer> clntPk = clientService.selectClientJuminAPhone(clntJuminA, clntPhone);

        // 인증번호가 일치할떼
        if (num != null && num.equals(checkNum)) {
            // 주민번호 앞자리, 전화번호가 동일한 고객이 있을때 이름을 추가적으로 입력받기
            if(clntPk.size() >=2){
                session.setAttribute("clntJuminA",clntJuminA);
                session.setAttribute("clntPhone",clntPhone);
                return "dataOver1";
            }
            else{
                try{
                    // 세션에 저장된 인증번호 제거
                    if (session.getAttribute("checkNum") != null) {
                        session.invalidate();
                        session = req.getSession();
                    }
                    // 세션에 고객 pk 저장
                    session.setAttribute("myPageClntPk", clntPk.get(0));
                    session.setMaxInactiveInterval(1800);
                }
                catch (Exception e){
                    // 가입된 고객이 아닐 때 pk값에 -1을 저장
                    session.setAttribute("myPageClntPk", -1);
                    session.setMaxInactiveInterval(1800);
                }
                return "success";
            }
        } else if (num == null) {
            return "session timeout";
        } else {
            return "fail";
        }
    }

    // 주민번호 앞자리, 전화번호가 일치하는 고객이 2명이상인 경우 추가적으로 이름을 입력받기위한 함수
    @RequestMapping(value = "/checkNumCheck/dataOver1", method = RequestMethod.POST)
    public String dataOver1(@RequestParam String name, @RequestParam String checkNum, HttpServletRequest req) throws Exception {
        HttpSession session = req.getSession();
        try{
            // 전송된 인증번호가 아직 세션에 남아있을때
            if(session.getAttribute("checkNum") != null){
                String num = session.getAttribute("checkNum").toString();
                // 인증번호가 일치할 때
                if(num.equals(checkNum)){
                    String clntJuminA = session.getAttribute("clntJuminA").toString();
                    String clntPhone = session.getAttribute("clntPhone").toString();
                    String clntNm = name;
                    System.out.printf("select : %s %s %s \n",clntNm, clntJuminA, clntPhone);
                    int clntPk = clientService.selectClientJuminAPhoneName(clntJuminA, clntPhone, clntNm).get(0);
                    // 세션에 저장된 인증번호 제거
                    session.invalidate();
                    session = req.getSession();
                    session.setAttribute("myPageClntPk", clntPk);
                    session.setMaxInactiveInterval(1800);
                    return "success";
                }
                else {
                    return "fail";
                }
            }
            else {return "session timeout";}
        }
        catch (Exception e){
            e.printStackTrace();
            return "bad network";
        }
    }

    // 전화번호 인증이 완료되었을때 MyPage2에서 가입된 보험 리스트가 나옴
    @RequestMapping(value = "/insList", method = RequestMethod.GET)
    public Object insList(HttpServletRequest req) throws Exception {
        HttpSession session = req.getSession();
        Map<String, Object> result = new HashMap<>();
        if (session.getAttribute("myPageClntPk") != null) {
            result.put("result", "success");
            int clntPk = (int) session.getAttribute("myPageClntPk");
            result.put("insList", clientService.selectInsSummaryDtoList(clntPk));
            result.put("clntPk",clntPk);
            return result;
        } else {
            result.put("result", "error");
            result.put("msg", "session timeout");
            return result;
        }
    }

    @RequestMapping(value = "/insDetail", method = RequestMethod.GET)
    public Object insDetail(@RequestParam int aplPk, @RequestParam int clntPk, HttpServletRequest req) throws Exception {
        HttpSession session = req.getSession();
        Map<String, Object> result = new HashMap<>();
        if ((int) session.getAttribute("myPageClntPk") == clntPk) {
            result.put("result","success");
            MyPageInsDetailDto insDetailDto = new MyPageInsDetailDto();
            MyPageInsSummaryDto myPageInsSummaryDto = clientService.selectInsSummaryDto(aplPk);
            List<MyPageCompanionDto> companionDtoList = clientService.selectCompanionDtoList(aplPk);
            MyPageClientDto repClientDto = clientService.selectClientByAplPk(aplPk);
            insDetailDto.setInsSummary(myPageInsSummaryDto);
            insDetailDto.setCompanionList(companionDtoList);
            insDetailDto.setRepClient(repClientDto);

            if(myPageInsSummaryDto.getAplStateCode() == 401){
                MyPageAccInfo myPageAccInfo = clientService.selectAccInfo(aplPk);
                insDetailDto.setMyPageAccInfo(myPageAccInfo);
            }

            System.out.println("clntPk : "+clntPk);
            System.out.println("repClientDto : "+repClientDto);
            result.put("insDetail",insDetailDto);

        } else {
            result.put("result", "error");
            result.put("msg", "session timeout");
        }
        return result;
    }

    @RequestMapping(value = "/email", method = RequestMethod.PUT)
    public Object editEmail(HttpServletRequest req, @RequestParam int clntPk, @RequestParam String email) throws Exception {
        HttpSession session = req.getSession();
        Map<String,Object> result = new HashMap<>();
        if(session.getAttribute("myPageClntPk") != null && (int)session.getAttribute("myPageClntPk") == clntPk ){
            clientService.updateClntEmail(clntPk, email);
            result.put("result","success");
            result.put("msg","email updated successfully");
            return result;
        }
        else{
            result.put("result","error");
            result.put("msg","세션이 만료되었습니다.");
            return result;
        }
    }

    @RequestMapping(value = "/applicationPdfKrDownload", method = RequestMethod.GET)
    public void applicationPdfKrDownload(@RequestParam int aplPk, @RequestParam int clntPk, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HttpSession session = req.getSession();

        if(session.getAttribute("myPageClntPk") != null && (int)session.getAttribute("myPageClntPk") == clntPk ){

            String filePath = System.getProperty("user.dir") + File.separator + "tempPdf" + File.separator + "application_"+aplPk+".pdf";
            PDDocument document = emailService.createPdfKr(aplPk);
            document.save(filePath);
            document.close();
            byte[] files = FileUtils.readFileToByteArray(new File(filePath));

            resp.setContentType("application/octet-stream");
            resp.setContentLength(files.length);
            resp.setHeader("Content-Disposition","attachment;fileName=\""+ URLEncoder.encode("가입증명서.pdf","UTF-8")+"\"");

            resp.getOutputStream().write(files);
            resp.getOutputStream().flush();
            resp.getOutputStream().close();

            // 생성한 PDF 파일 삭제
            File generatedPdf = new File(filePath);
            if (generatedPdf.exists()) {
                generatedPdf.delete();
            }
        }
    }

    @RequestMapping(value = "/applicationPdfKrEmail", method = RequestMethod.GET)
    public void applicationPdfKrEmail(@RequestParam int aplPk, @RequestParam int clntPk, @RequestParam String clntEmail,HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HttpSession session = req.getSession();
        if(session.getAttribute("myPageClntPk") != null && (int)session.getAttribute("myPageClntPk") == clntPk ){
            emailService.sendApplyMail(aplPk,"[SIMG 해외여행자보험] 가입증명서", clntEmail, "가입증명서.pdf");
        }
    }

    @RequestMapping(value = "/applicationCancel", method = RequestMethod.PUT)
    public Object applicationCancel(@RequestParam int aplPk, @RequestParam int clntPk, HttpServletRequest req) throws Exception {
        HttpSession session = req.getSession();
        Map<String,Object> result = new HashMap<>();
        if(session.getAttribute("myPageClntPk") != null && (int)session.getAttribute("myPageClntPk") == clntPk ){
            Character payedYN = clientService.selectPayedYNByAplPk(aplPk);
            if(payedYN == 'N'){
                clientService.updateAplStateCode(aplPk,clntPk,404);
                result.put("result","success");
                result.put("msg","deleted");
                return result;
            }
            else{
                result.put("result","success");
                result.put("msg","account required");
                result.put("aplPk",aplPk);
                return result;
            }
        }
        else if(session.getAttribute("myPageClntPk") == null){
            result.put("result","error");
            result.put("msg","세션이 만료되었습니다.");
            return result;
        }
        else{
            result.put("result","error");
            result.put("msg","네트워크 연결이 불안정합니다.");
            return result;
        }
    }
    @RequestMapping(value = "/applicationCancel/account", method = RequestMethod.PUT)
    public Object applicationCancel(@RequestParam int aplPk, @RequestParam int clntPk, @RequestParam String refnBank, @RequestParam String refnAccount, @RequestParam String refnName, HttpServletRequest req) throws Exception {
        HttpSession session = req.getSession();
        Map<String,Object> result = new HashMap<>();
        if(session.getAttribute("myPageClntPk") != null && (int)session.getAttribute("myPageClntPk") == clntPk ){
            result.put("result","success");
            result.put("msg",clientService.callAplRefund(aplPk, clntPk, refnBank, refnAccount, refnName));
            return result;
        }
        else if(session.getAttribute("myPageClntPk") == null){
            result.put("result","error");
            result.put("msg","세션이 만료되었습니다.");
            return result;
        }
        else{
            result.put("result","error");
            result.put("msg","네트워크 연결이 불안정합니다.");
            return result;
        }
    }
}
