package com.simg.simgoti.mapper;

import com.simg.simgoti.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Map;

// 데이터 베이스의 SQL 문과 연동하기 위한 인터페이스
// 해당 인터페이스의 추상 메소드와 xml 파일의 태그명을 1:1로 연동해주는 어노테이션
@Mapper
public interface ClientMapper {
    List<CoverageDto> selectCoverageList() throws Exception;
    List<CoverageTypeDto> selectCoverageTypeList() throws Exception;
    Map<String, Integer> selectPremium(int covCode) throws Exception;
    int insertRepClient(ClientDto dto) throws Exception;
    List<Integer> selectClientNmJuminPhone(String clntNm, String clntJumin, String clntPhone) throws Exception;
    int updateClientPhoneEmailByPk(ClientDto dto) throws Exception;
    int insertApplyPayment(ApplyPaymentDto dto) throws Exception;
    int insertApplyFinish(ApplyFinishDto dto) throws Exception;
    int insertApplyInsuredList(int aplPk, int clntPk, int prem, Character repYN) throws Exception;
    int selectClientJuminAPhone(String clntJuminA, String clntPhone) throws Exception;
    List<MyPageInsSummaryDto> selectApplySummaryList(int clntPk) throws Exception;
    MyPageInsSummaryDto selectInsSummaryDto(int aplPk) throws Exception;
    List<MyPageCompanionDto> selectCompanionDtoList(int aplPk) throws Exception;
    MyPageClientDto selectClientByAplPk(int aplPk) throws Exception;
    List<MyPageCoverageDetailDto> selectCoverageDetailList(int aplPk) throws Exception;
    int updateClntEmail(int clntPk, String clntEmail) throws Exception;
    PdfInsInfoDto selectInsForPdf(int aplPk) throws Exception;
}
