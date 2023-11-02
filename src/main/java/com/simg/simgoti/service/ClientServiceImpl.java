package com.simg.simgoti.service;

import com.simg.simgoti.dto.*;
import com.simg.simgoti.mapper.ClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientMapper clientMapper;
    private final CommonService commonService;
    private final HanaPremiumImpl hanaPremium;
    private final Aes128Service aes = new Aes128Service("simgotiaes128key");

    @Override
    public List<CoverageDto> selectCoverageList(char isOver19) throws Exception {
        return clientMapper.selectCoverageList(isOver19);
    }

    @Override
    public List<CoverageTypeDto> selectCoverageTypeList(int insAge, char gen, int day, char isOver19) throws Exception {
        List<CoverageTypeDto> list = clientMapper.selectCoverageTypeList(isOver19);
        for (CoverageTypeDto dto : list) {
            dto.setTotPremium(hanaPremium.selectByCovAgeGen(dto.getCovCode(), insAge, gen, day));
        }
        return list;
    }


    @Override
    public int insertOrUpdateClient(String clntNm, String clntBirth, String clntGen, String clntJumin, String clntPhone, String clntEmail) throws Exception {
        String encJumin = aes.encrypt(clntJumin);
        int clntPk = -1;
        char isOver19 = 'Y';
        if(commonService.calculateManAge(clntBirth) < 19){
            isOver19 = 'N';
        }
        try {
            clntPk = clientMapper.selectClientNmJumin(clntNm, encJumin).get(0);
            ClientDto dto = new ClientDto(clntPk, clntNm, clntPhone, clntEmail, clntBirth, clntGen, encJumin);
            if (clntPhone != null && clntEmail != null) {
                clientMapper.updateClientPhoneEmailByPk(dto);
            }
        } catch (Exception e) {
//            e.printStackTrace();
            ClientDto dto = new ClientDto();
            dto.setClntNm(clntNm);
            dto.setClntBirth(clntBirth);
            dto.setClntGen(clntGen);
            dto.setClntJumin(encJumin);
            dto.setClntPhone(clntPhone);
            dto.setClntEmail(clntEmail);
            clientMapper.insertRepClient(dto);
            clntPk = dto.getClntPk();
        }
        return clntPk;
    }

    @Override
    public int selectClientNmJumin(String clntNm, String clntJumin) throws Exception{
        String clntJuminEnc = aes.encrypt(clntJumin);
        try{
            return clientMapper.selectClientNmJumin(clntNm, clntJuminEnc).get(0);
        }
        catch (Exception e){
            return -1;
        }
    }

    @Override
    public List<CoverageDto> selectCovDList(String covCode, char isOver19){
        return clientMapper.selectCovDList(covCode, isOver19);
    };

    @Override
    public String selectCovNm(String covCode) {
        return clientMapper.selectCovNm(Integer.parseInt(covCode));
    }

    @Override
    public int insertApplyPayment(int premium, String payDueDt, String accBank, String accNum, String accNm) throws Exception {
        ApplyPaymentDto dto = new ApplyPaymentDto();
        dto.setPremium(premium);
        dto.setPayDueDt(payDueDt);
        dto.setAccBank(accBank);
        dto.setAccNum(accNum);
        dto.setAccNm(accNm);
        clientMapper.insertApplyPayment(dto);
        return dto.getPayPk();
    }

    @Override
    public int insertApplyFinish(int payPk, String polNo, int comCode, int insComCode, int clntPk,
                                 int trPurpose, String trPlace, String trFromDt, String trToDt, int covCode, int clntCnt, int premium) throws Exception {
        ApplyFinishDto dto = new ApplyFinishDto();
        dto.setPayPk(payPk);
        dto.setPolNo(polNo);
        dto.setComCode(comCode);
        dto.setInsComCode(insComCode);
        dto.setClntPk(clntPk);
        dto.setTrPurpose(trPurpose);
        dto.setTrPlace(trPlace);
        dto.setTrFromDt(trFromDt);
        dto.setTrToDt(trToDt);
        dto.setCovCode(covCode);
        dto.setClntCnt(clntCnt);
        dto.setPremium(premium);
        clientMapper.insertApplyFinish(dto);
        System.out.println("보험가입완료 : " + dto);
        return dto.getAplPk();
    }

    @Override
    public int insertApplyInsuredList(int aplPk, int clntPk, int prem, Character repYN, char isOver19) throws Exception {
        return clientMapper.insertApplyInsuredList(aplPk, clntPk, prem, repYN, isOver19);
    }

    @Override
    public List<Integer> selectClientJuminAPhone(String clntJuminA, String clntPhone) throws Exception {
        return clientMapper.selectClientJuminAPhone(clntJuminA, clntPhone);
    }

    @Override
    public List<Integer> selectClientJuminAPhoneName(String clntJuminA, String clntPhone, String clntNm) throws Exception {
        List<Integer> list = new ArrayList<>();
        try{
            list = clientMapper.selectClientJuminAPhoneName(clntJuminA, clntPhone, clntNm);
            return list;
        }
        catch (Exception e){
            e.printStackTrace();
            list.add(-1);
            return list;
        }
    }

    @Override
    public List<MyPageInsSummaryDto> selectInsSummaryDtoList(int clntPk) throws Exception {
        List<MyPageInsSummaryDto> list = new ArrayList<>();
        list = clientMapper.selectApplySummaryList(clntPk);
        for(int i = 0; i < list.size(); i++){
            String trFromDt =  list.get(i).getTrFromDt().substring(0,16);
            String trToDt = list.get(i).getTrToDt().substring(0,16);
            int period = commonService.getPeriod(trFromDt, trToDt);
            list.get(i).setPeriod(period);
            list.get(i).setTrFromDt(trFromDt);
            list.get(i).setTrToDt(trToDt);
        }
        return list;
    }

    @Override
    public MyPageInsSummaryDto selectInsSummaryDto(int aplPk) throws Exception {
        MyPageInsSummaryDto dto = clientMapper.selectInsSummaryDto(aplPk);
        String trFromDt = dto.getTrFromDt().substring(0,16);
        String trToDt = dto.getTrToDt().substring(0,16);
        int period = commonService.getPeriod(trFromDt, trToDt);
        dto.setPeriod(period);
        dto.setTrFromDt(trFromDt);
        dto.setTrToDt(trToDt);
        return dto;
    }

    @Override
    public List<MyPageCompanionDto> selectCompanionDtoList(int aplPk) throws Exception {
        List<MyPageCompanionDto> myPageCompanionDtoList = clientMapper.selectCompanionDtoList(aplPk);
        // aplPk를 이용해서 myPageCompanionDtoList에 covDList 추가하기
        for(int i = 0;i < myPageCompanionDtoList.size(); i++){
            char isOver19 = myPageCompanionDtoList.get(i).getAdult();
            System.out.printf("%s %s", myPageCompanionDtoList.get(i).getClntNm(),myPageCompanionDtoList.get(i).getAdult());
            myPageCompanionDtoList.get(i).setCovDList(clientMapper.selectCoverageDetailList(aplPk, isOver19));
        }
        return myPageCompanionDtoList;
    }

    @Override
    public MyPageAccInfo selectAccInfo(int aplPk) throws Exception{
        return clientMapper.selectAccInfo(aplPk);
    }

    @Override
    public MyPageClientDto selectClientByAplPk(int aplPk) throws Exception {
        return clientMapper.selectClientByAplPk(aplPk);
    }
    @Override
    public int updateClntEmail(int clntPk, String clntEmail) throws Exception{
        return clientMapper.updateClntEmail(clntPk, clntEmail);
    }

    @Override
    public Character selectPayedYNByAplPk(int aplPk) throws Exception{
        return clientMapper.selectPayedYNByAplPk(aplPk);
    }

    @Override
    public int updateAplStateCode(int aplPk,int clntPk,  int aplStateCode) throws Exception{
        return clientMapper.updateAplStateCode(aplPk,clntPk, aplStateCode);
    }

    @Override
    public int callAplRefund(int aplPk, int clntPk, String refnBank, String refnAccount, String refnName) throws Exception{
        return clientMapper.callAplRefund(aplPk, clntPk, refnBank, refnAccount, refnName);
    }

    @Override
    public int insertClaim(ClaimDto claim) throws Exception {
        String clntJuminEnc = aes.encrypt(claim.getClntJumin());
        claim.setClntJumin(clntJuminEnc);
        return clientMapper.insertClaim(claim);
    }

    @Override
    public int checkDuplicatedApplication(int clntPk, String trFromDt, String trToDt) throws Exception{
        return clientMapper.checkDuplicatedApplication(clntPk, trFromDt, trToDt);
    }
}
