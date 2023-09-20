package com.simg.simgoti.service;

import com.simg.simgoti.dto.ClientDto;
import com.simg.simgoti.dto.CoverageDto;
import com.simg.simgoti.dto.CoverageTypeDto;
import com.simg.simgoti.mapper.ApplyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ApplyMapper applyMapper;
//    @Override
//    public List<ClientDto> selectClientList() throws Exception {
//        return applyMapper.selectClientList();
//    }

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

    // 최소 보험료, 일당 보험료를 이용, 실속형과 그 이외 담보의 보험료를 구하는 메소드
    public int calculatePremium(String code, int perDay, int min, int day){
        int result = min;
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
