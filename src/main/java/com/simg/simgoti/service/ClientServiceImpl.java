package com.simg.simgoti.service;

import com.simg.simgoti.dto.ClientDto;
import com.simg.simgoti.mapper.ApplyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ApplyMapper applyMapper;
    @Override
    public List<ClientDto> selectClientList() throws Exception {
        return applyMapper.selectClientList();
    }
}
