package com.simg.simgoti.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommonMapper {
    String selectPolNoByCovCode(int covCode) throws Exception;
}
