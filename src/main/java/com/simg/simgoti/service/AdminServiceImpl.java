package com.simg.simgoti.service;

import com.simg.simgoti.dto.AdminClaimDto;
import com.simg.simgoti.dto.AdminInsSumDto;
import com.simg.simgoti.dto.ClaimDto;
import com.simg.simgoti.dto.Pageable;
import com.simg.simgoti.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminMapper adminMapper;
    private final Aes128Service aes = new Aes128Service("simgotiaes128key");
    @Override
    public List<AdminInsSumDto> selectAdminInsSumDtoList(Character useYN, Pageable pageable) throws Exception{
        return adminMapper.selectAdminInsSumDtoList(useYN, pageable.getStart(), pageable.getSize(), pageable.getOrderBy());
    }

    @Override
    public AdminInsSumDto selectAdminInsSumDto(int aplPk) throws Exception {
        return adminMapper.selectAdminInsSumDto(aplPk);
    }

    @Override
    public int updateInsPayedYN(int aplPk, Character payedYN) throws Exception{
        if(payedYN.equals('N')){ // payedYN을 N으로 바꾸는 경우(결제되지않은 상태로 바꾸는경우)
            return adminMapper.updateInsPayedYN(aplPk, 'N', 401);
        }
        else{ // payedYN을 Y로 바꾸는경우(결제된 상태로 바꾸는 경우)
            return adminMapper.updateInsPayedYN(aplPk, 'Y', 402);
        }
    }

    @Override
    public int updateInsStateCode(int aplPk, int aplStateCode) throws Exception{
        return adminMapper.updateInsStateCode(aplPk, aplStateCode);
    }

    @Override
    public List<AdminClaimDto> selectClaimDtoList(Character useYN, Pageable pageable) throws Exception{
        List<AdminClaimDto> list = adminMapper.selectClaimDtoList(useYN, pageable.getStart(), pageable.getSize(), pageable.getOrderBy());
        for(int i=0; i<list.size(); i++){
            AdminClaimDto claimDto = list.get(i);
            claimDto.setClntJumin(aes.decrypt(claimDto.getClntJumin()));
            list.set(i,claimDto);
        }
        return list;
    }
}
