package com.simg.simgoti.service;

import com.simg.simgoti.dto.AdminInsSumDto;
import com.simg.simgoti.dto.Pageable;
import com.simg.simgoti.mapper.AdminMapper;

import java.util.List;

public interface AdminService {
    List<AdminInsSumDto> selectAdminInsSumDtoList(Character useYN, Pageable pageable) throws Exception;
    AdminInsSumDto selectAdminInsSumDto(int aplPk, Character useYN) throws Exception;
    int updateInsPayedYN(int aplPk, Character payedYN) throws Exception;
}
