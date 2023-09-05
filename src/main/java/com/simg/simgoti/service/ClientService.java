package com.simg.simgoti.service;

import com.simg.simgoti.dto.ClientDto;

import java.util.List;

public interface ClientService {
    List<ClientDto> selectClientList() throws Exception;
}
