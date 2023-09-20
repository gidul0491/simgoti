package com.simg.simgoti.mapper;

import com.simg.simgoti.dto.CountryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommonMapper {
    List<CountryDto> getCountryList() throws Exception;
}
