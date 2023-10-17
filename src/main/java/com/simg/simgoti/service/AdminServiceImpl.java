package com.simg.simgoti.service;

import com.simg.simgoti.dto.AdminInsSumDto;
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
        if(payedYN.equals('N')){
            return adminMapper.updateInsPayedYN(aplPk, 'N', 401);
        }
        else{
            return adminMapper.updateInsPayedYN(aplPk, 'Y', 402);
        }
    }

    @Override
    public int updateInsStateCode(int aplPk, int aplStateCode) throws Exception{
        return adminMapper.updateInsStateCode(aplPk, aplStateCode);
    }
}
