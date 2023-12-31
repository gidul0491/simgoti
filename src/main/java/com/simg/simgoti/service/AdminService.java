package com.simg.simgoti.service;

import com.simg.simgoti.dto.AdminClaimDto;
import com.simg.simgoti.dto.AdminInsSumDto;
import com.simg.simgoti.dto.ClaimDto;
import com.simg.simgoti.dto.Pageable;
import com.simg.simgoti.mapper.AdminMapper;

import java.util.List;

public interface AdminService {
    List<AdminInsSumDto> selectAdminInsSumDtoList(Character useYN, Pageable pageable) throws Exception;
    AdminInsSumDto selectAdminInsSumDto(int aplPk) throws Exception;
    int updateInsPayedYN(int aplPk, Character payedYN) throws Exception;
    int updateInsStateCode(int aplPk, int aplStateCode) throws Exception;
    List<AdminClaimDto> selectClaimDtoList(Character useYN, Pageable pageable) throws Exception;
}
