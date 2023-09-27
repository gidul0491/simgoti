package com.simg.simgoti.mapper;

import com.simg.simgoti.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

// 데이터 베이스의 SQL 문과 연동하기 위한 인터페이스
// 해당 인터페이스의 추상 메소드와 xml 파일의 태그명을 1:1로 연동해주는 어노테이션
@Mapper
public interface ApplyMapper {
    List<CoverageDto> selectCoverageList() throws Exception;
    List<CoverageTypeDto> selectCoverageTypeList() throws Exception;
    Map<String, Integer> selectPremium(int covCode) throws Exception;
    int insertRepClient(ClientDto dto) throws Exception;
    int selectClientNmJumin(String clntNm, String clntJumin) throws Exception;
    int updateClientPhoneEmailByPk(ClientDto dto) throws Exception;
    int insertApplyPayment(ApplyPaymentDto dto) throws Exception;
    int insertApplyFinish(ApplyFinishDto dto) throws Exception;
    int insertApplyInsuredList(int aplPk, int clntPk, Character repYN) throws Exception;
}
