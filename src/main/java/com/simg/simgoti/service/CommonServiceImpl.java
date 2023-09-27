package com.simg.simgoti.service;

import com.simg.simgoti.mapper.CommonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {
    private final CommonMapper commonMapper;

    public String selectPolNoByCovCode(int covCode) throws Exception {
        return commonMapper.selectPolNoByCovCode(covCode);
    }
}
