package com.simg.simgoti.mapper;

import com.simg.simgoti.dto.ClientDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

// 데이터 베이스의 SQL 문과 연동하기 위한 인터페이스
// 해당 인터페이스의 추상 메소드와 xml 파일의 태그명을 1:1로 연동해주는 어노테이션
@Mapper
public interface ApplyMapper {
    List<ClientDto> selectClientList() throws Exception;


}
