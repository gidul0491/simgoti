package com.simg.simgoti.service;

import com.simg.simgoti.dto.*;
import com.simg.simgoti.mapper.ClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientMapper clientMapper;
    private final CommonService commonService;

    @Override
    public List<CoverageDto> selectCoverageList() throws Exception {
        return clientMapper.selectCoverageList();
    }

    @Override
    public List<CoverageTypeDto> selectCoverageTypeList(int day) throws Exception {
        List<CoverageTypeDto> list = clientMapper.selectCoverageTypeList();
        for (CoverageTypeDto dto : list) {
            dto.setTotPremium(commonService.calculatePremium(dto.getCovCode(), dto.getCovPremium(), dto.getCovPremiumMin(), day));
        }
        return list;
    }

    @Override
    public int getPremium(String code, int period, int cnt) throws Exception {
        int prem = 0;
        Map<String, Integer> prems = clientMapper.selectPremium(Integer.parseInt(code));
        prem = commonService.calculatePremium(code, prems.get("covPremium"), prems.get("covPremiumMin"), period) * cnt;
        return prem;
    }

    @Override
    public int insertOrUpdateClient(String clntNm, String clntBirth, String clntGen, String clntJumin, String clntPhone, String clntEmail) throws Exception {
        Aes128Service aes = new Aes128Service("simgotiaes128key");
        String encJumin = aes.encrypt(clntJumin);
        System.out.printf("encJumin : %s \n",encJumin);
        int clntPk = -1;
        try {
            clntPk = clientMapper.selectClientNmJuminPhone(clntNm, encJumin, clntPhone).get(0);
            ClientDto dto = new ClientDto(clntPk, clntNm, clntPhone, clntEmail, clntBirth, clntGen, encJumin);
            System.out.println(dto);
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

//    @Override
//    public int selectClientNmJumin(String clntNm, String clntJumin) throws Exception {
//        Aes128Service aes = new Aes128Service("simgotiaes128key");
//        String encJumin = aes.encrypt(clntJumin);
//        return clientMapper.selectClientNmJuminPhone(clntNm, encJumin).get(0);
//    }

    @Override
    public int insertApplyPayment(int premium, String payDueDt) throws Exception {
        ApplyPaymentDto dto = new ApplyPaymentDto();
        dto.setPremium(premium);
        dto.setPayDueDt(payDueDt);
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
        System.out.println(dto);
        return dto.getAplPk();
    }

    @Override
    public int insertApplyInsuredList(int aplPk, int clntPk, int prem, Character repYN) throws Exception {
        return clientMapper.insertApplyInsuredList(aplPk, clntPk, prem, repYN);
    }

    @Override
    public int selectClientJuminAPhone(String clntJuminA, String clntPhone) throws Exception {
        return clientMapper.selectClientJuminAPhone(clntJuminA, clntPhone);
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
            myPageCompanionDtoList.get(i).setCovDList(clientMapper.selectCoverageDetailList(aplPk));
        }
        return myPageCompanionDtoList;
    }

    @Override
    public MyPageClientDto selectClientByAplPk(int aplPk) throws Exception {
        return clientMapper.selectClientByAplPk(aplPk);
    }
}
