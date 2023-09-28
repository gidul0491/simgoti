package com.simg.simgoti.controller;

import com.simg.simgoti.dto.*;
import com.simg.simgoti.service.ClientService;
import com.simg.simgoti.service.CommonService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController{
    private final ClientService clientService;
    private final CommonService commonService;

    // calculate 페이지에서 보험료 확인 버튼을 눌렀을 때 db의 coverage 테이블에서 coverage code, 담보 한글이름, 하루당 보험료, 최소 보험료 정보인 coverageTabList와
    // 담보에 포함되는 세부담보 코드, 세부담보 이름, 세부담보 보장금액 정보인 coverageList를 result라는 변수명에 담아서 반환
    @RequestMapping(value = "/coverageList", method = RequestMethod.GET)
    public Object selectCoverageList(@RequestParam String startDt, @RequestParam String endDt, @RequestParam String clntBirth) throws Exception {
        Map<String, Object> resp = new HashMap<>();
        int insAge;
        try{
            insAge = commonService.calculateInsAge(clntBirth);
        }
        catch(Exception e){
            resp.put("msg","생년월일을 정확히 입력해주세요.");
            resp.put("result","error");
            return resp;
        }

        String validateInsDate = commonService.validateInsDate(startDt, endDt);

        if(insAge < 1 || insAge > 80){
            resp.put("msg","1세부터 80세까지만 가입 가능합니다.");
            resp.put("result","error");
            return resp;
        }
        else if(!validateInsDate.equals("ok")){
            resp.put("msg",validateInsDate);
            resp.put("result","error");
            return resp;
        }
        else{
            int travelDay = getPeriod(startDt, endDt);
            Map<String, List> data = new HashMap<>();
            data.put("coverageList",clientService.selectCoverageList());
            data.put("coverageTabList", clientService.selectCoverageTypeList(travelDay));

            resp.put("data",data);
            resp.put("result","success");
            return resp;
        }
    }

    // apply1Alone 페이지에서 확인 버튼을 누르면 실행
    // 입력한 정보를 세션에 추가, 입력한 정보를 이용하여 db에 저장 혹은 업데이트(이메일/전화번호), db에서 사용하는 pk값을 세션에 추가
    @RequestMapping(value = "/cliInfo", method = RequestMethod.POST)
    public void addAppSession(@RequestParam String clntNm,@RequestParam String clntJuminA, @RequestParam String clntJuminB, @RequestParam String clntPhone, @RequestParam String clntEmail, @RequestParam int clntCnt, HttpServletRequest req) throws Exception{
        HttpSession session = req.getSession();
        session.setAttribute("clntNm", clntNm);
        session.setAttribute("clntJuminA", clntJuminA);
        session.setAttribute("clntJuminB", clntJuminB);
        session.setAttribute("clntPhone", clntPhone);
        session.setAttribute("clntEmail", clntEmail);
        session.setAttribute("clntCnt", clntCnt);
        session.setMaxInactiveInterval(1800);

        String clntGen = clntJuminB.substring(0,1);
        // 주민번호 암호화
        String clntJumin = clntJuminA + clntJuminB;

        int clntPk = clientService.insertOrUpdateClient(clntNm, clntJuminA, clntGen, clntJumin, clntPhone, clntEmail);
        session.setAttribute("clntPk", clntPk);
        System.out.printf("clntPk : %s \n",clntPk);

        if(clntCnt > 1){
        }
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
        try{
            jsonArray = (JSONArray) jsonParser.parse(data);
        }
        catch (ParseException e) {
            e.printStackTrace();
            return "json parsing failed";
        }
        // 대표가입자의 주민번호
        String repClntJumin = clntJuminA + clntJuminB;

        int period = getPeriod(startDt, endDt);
        int repPrem = clientService.getPremium(covCode, period, 1); // 대표가입자의 보험료

        CompanionDto rep = new CompanionDto();
        if(calOrApl.equals("cal")){
            rep = new CompanionDto("대표",clntNm, clntJuminA, clntJuminB.substring(0,1)+"******", repPrem, 0);
        }
        else if(calOrApl.equals("apl")){
            // 대표가입자 정보를 db에 저장/업데이트
            int repClntPk = clientService.insertOrUpdateClient(clntNm, clntJuminA, clntJuminB.substring(0,1), repClntJumin, clntPhone, clntEmail);
            rep = new CompanionDto("대표",clntNm, clntJuminA, clntJuminB.substring(0,1)+"******", repPrem, repClntPk);
            session.setAttribute("clntPk",repClntPk);
            session.setAttribute("clntNm", clntNm);
            session.setAttribute("clntJuminA", clntJuminA);
            session.setAttribute("clntJuminB", clntJuminB);
            session.setAttribute("clntPhone", clntPhone);
            session.setAttribute("clntEmail", clntEmail);
            session.setAttribute("clntCnt", clntCnt);
            session.setMaxInactiveInterval(1800);
        }
        list.add(rep);


        for(int i=0; i<jsonArray.size(); i++) {

            JSONObject item = (JSONObject) jsonArray.get(i);
            System.out.println("동반가입자");
            System.out.println(item);
            String num = item.get("companionNum").toString();
            String nm = item.get("companionNm").toString();
            String juminA = item.get("companionJuminA").toString();
            String juminB = item.get("companionJuminB").toString();

            int prem = clientService.getPremium(covCode, period, 1);

            String gen = juminB.substring(0,1);
            String jumin = juminA + juminB;

            CompanionDto dto = new CompanionDto();
            if(calOrApl.equals("cal")){
                dto = new CompanionDto(num, nm, juminA, juminB.substring(0,1)+"******", prem, 0);
            }
            else if(calOrApl.equals("apl")){
                // 동반가입자 정보를 db에 저장
                int clntPk = clientService.insertOrUpdateClient(nm, juminA, gen, jumin, null, null);
                dto = new CompanionDto(num, nm, juminA, juminB.substring(0,1)+"******", prem, clntPk);
            };
            list.add(dto);
        }

        if(calOrApl.equals("apl")){
            session.setAttribute("companion",list);
            return "ok";
        }

        return list;
    }

    // apply2 페이지에서 모두 아니오 체크 후 여행지 선택하고 확인버튼 클릭시 실행됨
    // 여행목적, 여행출발/종료일시, 담보코드, 여행지를 세션에 저장
    @RequestMapping(value = "/trInfo", method = RequestMethod.POST)
    public String addPurposeSession(@RequestParam String trPurpose, @RequestParam String startDt,@RequestParam String endDt,@RequestParam String covCode,@RequestParam String trPlace, HttpServletRequest req){
        HttpSession session = req.getSession();
        session.setAttribute("trPurpose", trPurpose);
        session.setAttribute("startDt", startDt);
        session.setAttribute("endDt", endDt);
        session.setAttribute("covCode", covCode);
        session.setAttribute("trPlace", trPlace);
        session.setMaxInactiveInterval(1800);
        System.out.printf("이름 %s 주민앞자리 %s 주민뒷자리 %s 폰 %s 이메일 %s 담보 %s 여행자수 %s 여행장소 %s 여행목적 %s 여행출발 %s 여행종료 %s \n",
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
            int period = getPeriod(session.getAttribute("startDt").toString(), session.getAttribute("endDt").toString());
            int totalPrem = clientService.getPremium(session.getAttribute("covCode").toString(), period, clntCnt);
            session.setAttribute("totalPrem", totalPrem);
            session.setMaxInactiveInterval(1800);

            // 결제페이지에서 보여주는 정보 중 보험계약정보
            ApplySummaryDto appSummaryDto = new ApplySummaryDto(
                    period,
                    session.getAttribute("startDt").toString(),
                    session.getAttribute("endDt").toString(),
                    session.getAttribute("trPlace").toString(),
                    session.getAttribute("covCode").toString(),
                    clntCnt,
                    totalPrem
            );
            applyFinalCheckDto.setAppSummaryDto(appSummaryDto);

            // 결제페이지의 보험계약정보중 동승자가 있을때 추가되는 동승자명단
            List<CompanionDto> companionDtoList = new ArrayList<>();
            if (session.getAttribute("companion") != null && (int) session.getAttribute("clntCnt") >1 ) {
                companionDtoList = (ArrayList<CompanionDto>) session.getAttribute("companion");
                applyFinalCheckDto.setCompanionDtoList(companionDtoList);
            }
            applyFinalCheckDto.setCompanionDtoList(companionDtoList);

            // 결제페이지의 대표가입자정보
            ClientDto repClientDto = new ClientDto();
            repClientDto.setClntNm(session.getAttribute("clntNm").toString());
            String resi = session.getAttribute("clntJuminA") + session.getAttribute("clntJuminB").toString().substring(0, 1) + "******";
            repClientDto.setClntJumin(resi);
            repClientDto.setClntPhone(session.getAttribute("clntPhone").toString());
            repClientDto.setClntEmail(session.getAttribute("clntEmail").toString());
            applyFinalCheckDto.setRepClientDto(repClientDto);
        }
        catch (Exception e){
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

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dueDt = now.withHour(23).withMinute(0).withSecond(0).withNano(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String dueDtStr = dueDt.format(formatter);
        session.setAttribute("dueDt",dueDtStr);

        AccDto acc1 = new AccDto("하나은행", "2579106828397", "박준호", dueDtStr);
        accDtoList.add(acc1);

        AccDto acc2 = new AccDto("신한은행", "110501566550", "박준호", dueDtStr);
        accDtoList.add(acc2);

        return accDtoList;
    }


    // 모든 세션정보를 삭제
    @RequestMapping(value = "/allSession", method = RequestMethod.DELETE)
    public void deleteAllSession(HttpServletRequest req) throws Exception {
        HttpSession session = req.getSession();
        session.invalidate();
    }

    // apply3 페이지에서 무통장입금 선택 후 은행까지 선택하면 실행됨
    @RequestMapping(value = "/applyFinish", method = RequestMethod.POST)
    public Object applyFinish(HttpServletRequest req) throws Exception {
        HttpSession session = req.getSession();
        try {
            int totalPrem = Integer.parseInt(session.getAttribute("totalPrem").toString());
            String dueDt = session.getAttribute("dueDt").toString();
            int covCode = Integer.parseInt(session.getAttribute("covCode").toString());
            // 결제 정보 저장
            int payPk = clientService.insertApplyPayment(totalPrem, dueDt);
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
            int aplPk = clientService.insertApplyFinish(payPk, polNo, comCode, insComCode, clntPk, trPurpose, trPlace,trFromDt, trToDt, covCode, clntCnt, premium);

            // 가입자명단정보 저장
            // 대표가입자
            clientService.insertApplyInsuredList(aplPk, clntPk, 'Y');
            // 동승자
            if(session.getAttribute("companion") != null){
                List<CompanionDto> companionDtoList = (ArrayList<CompanionDto>) session.getAttribute("companion");
                for(int i = 1;i<companionDtoList.size(); i++){
                    int comClntPk = companionDtoList.get(i).getClntPk();
                    clientService.insertApplyInsuredList(aplPk, comClntPk, 'N');
                }
            }

            return "ok";
        }
        catch (Exception e){
            System.out.println(e);
            return e;
        }
    }


    public int getPeriod(String startDt, String endDt){
        int result = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startLdt = LocalDateTime.parse(startDt, formatter);
        LocalDateTime endLdt = LocalDateTime.parse(endDt, formatter);
        Duration duration = Duration.between(startLdt, endLdt);
        long seconds = duration.getSeconds();

        result = (int) Math.ceil(((double) seconds / ((long) (24 * 60 * 60))));

        return result;
    }
}
