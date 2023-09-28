package com.simg.simgoti.service;

import com.simg.simgoti.dto.*;
import com.simg.simgoti.mapper.ApplyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ApplyMapper applyMapper;

    @Override
    public List<CoverageDto> selectCoverageList() throws Exception {
        return applyMapper.selectCoverageList();
    }

    @Override
    public List<CoverageTypeDto> selectCoverageTypeList(int day) throws Exception {
        List<CoverageTypeDto> list = applyMapper.selectCoverageTypeList();
        for(CoverageTypeDto dto : list){
            dto.setTotPremium(calculatePremium(dto.getCovCode(),dto.getCovPremium(), dto.getCovPremiumMin(),day));
        }
        return list;
    }

    @Override
    public int getPremium(String code, int period, int cnt) throws Exception {
        int prem = 0;
        Map<String, Integer> prems = applyMapper.selectPremium(Integer.parseInt(code));
        prem = calculatePremium(code,prems.get("covPremium"), prems.get("covPremiumMin"),period) * cnt;
        return prem;
    }

    @Override
    public int insertOrUpdateClient(String clntNm, String clntBirth, String clntGen, String clntJumin, String clntPhone, String clntEmail) throws Exception {
            int clntPk = -1;
            try {
                clntPk = applyMapper.selectClientNmJumin(clntNm, clntJumin);
                ClientDto dto = new ClientDto(clntPk, clntNm, clntPhone, clntEmail, clntBirth, clntGen, clntJumin);
                if(clntEmail != null && clntEmail != null){
                    applyMapper.updateClientPhoneEmailByPk(dto);
                }
            }
            catch(Exception e){
                System.out.println(e);
                ClientDto dto = new ClientDto();
                dto.setClntNm(clntNm);
                dto.setClntBirth(clntBirth);
                dto.setClntGen(clntGen);
                dto.setClntJumin(clntJumin);
                dto.setClntPhone(clntPhone);
                dto.setClntEmail(clntEmail);
                applyMapper.insertRepClient(dto);
                clntPk = dto.getClntPk();
            }
            return clntPk;
    }

    @Override
    public int selectClientNmJumin(String clntNm, String clntJumin) throws Exception {
        return applyMapper.selectClientNmJumin(clntNm, clntJumin);
    }

    @Override
    public int insertApplyPayment(int premium, String payDueDt) throws Exception {
        ApplyPaymentDto dto = new ApplyPaymentDto();
        dto.setPremium(premium);
        dto.setPayDueDt(payDueDt);
        applyMapper.insertApplyPayment(dto);
        return dto.getPayPk();
    }

    @Override
    public int insertApplyFinish(int payPk, String polNo, int comCode, int insComCode, int clntPk,
                               int trPurpose, String trPlace, String trFromDt, String trToDt, int covCode, int clntCnt, int premium) throws Exception{
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
        applyMapper.insertApplyFinish(dto);
        System.out.println(dto);
        return dto.getAplPk();
    }

    @Override
    public int insertApplyInsuredList(int aplPk, int clntPk, Character repYN) throws Exception{
        return applyMapper.insertApplyInsuredList(aplPk, clntPk, repYN);
    }


    // 담보코드,일당 보험료, 최소 보험료, 보험일수를 이용하여 담보의 보험료를 구하는 메소드
    public int calculatePremium(String code, int perDay, int min, int day){
        int result = min;
        // cheapest 코드는 실속형 코드로 계산법이 다름;
        String cheapest = "10120101";
        if(code.equals(cheapest) && day >= 3){
            result += (day-2) * perDay;
        }
        else if(!code.equals(cheapest) && day == 3){
            result += 1 * perDay;
        }
        else if(!code.equals(cheapest) && day >= 4){
            result += (day -1 ) * perDay;
        }
        result = (int) (Math.floor(result/10) * 10);
        return result;
    }
}
