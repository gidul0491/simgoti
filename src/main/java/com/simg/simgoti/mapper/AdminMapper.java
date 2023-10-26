package com.simg.simgoti.mapper;

import com.simg.simgoti.dto.AdminClaimDto;
import com.simg.simgoti.dto.AdminInsSumDto;
import com.simg.simgoti.dto.ClaimDto;
import com.simg.simgoti.dto.Pageable;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {
    List<AdminInsSumDto> selectAdminInsSumDtoList(Character useYN, int start, int size, String orderBy) throws Exception;
    AdminInsSumDto selectAdminInsSumDto(int aplPk) throws Exception;
    int updateInsPayedYN(int aplPk, Character payedYN, int aplStateCode) throws Exception;
    int updateInsStateCode(int aplPk, int aplStateCode) throws Exception;
    List<AdminClaimDto> selectClaimDtoList(Character useYN, int start, int size, String orderBy) throws Exception;
}
