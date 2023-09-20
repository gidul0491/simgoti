package com.simg.simgoti.service;

import com.simg.simgoti.dto.CountryDto;
import com.simg.simgoti.mapper.CommonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {
    private final CommonMapper commonMapper;
    public List<CountryDto> getCountryList() throws Exception {
        List<CountryDto> result = commonMapper.getCountryList();
      return result;
    };
}
