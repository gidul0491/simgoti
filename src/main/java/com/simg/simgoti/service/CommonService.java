package com.simg.simgoti.service;

import com.simg.simgoti.dto.CountryDto;
import com.simg.simgoti.dto.CoverageTypeDto;

import java.util.List;

public interface CommonService {
    List<CountryDto> getCountryList() throws Exception;
}
