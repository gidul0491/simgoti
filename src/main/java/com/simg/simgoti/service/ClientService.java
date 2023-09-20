package com.simg.simgoti.service;

import com.simg.simgoti.dto.ClientDto;
import com.simg.simgoti.dto.CoverageDto;
import com.simg.simgoti.dto.CoverageTypeDto;
import jdk.jshell.spi.ExecutionControlProvider;

import java.util.List;

public interface ClientService {
//    List<ClientDto> selectClientList() throws Exception;
    List<CoverageDto> selectCoverageList() throws Exception;
    List<CoverageTypeDto> selectCoverageTypeList(int day) throws Exception;
}
