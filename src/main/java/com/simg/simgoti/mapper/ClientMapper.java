package com.simg.simgoti.mapper;

import com.simg.simgoti.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// 데이터 베이스의 SQL 문과 연동하기 위한 인터페이스
// 해당 인터페이스의 추상 메소드와 xml 파일의 태그명을 1:1로 연동해주는 어노테이션
@Mapper
public interface ClientMapper {
    List<CoverageDto> selectCoverageList(char isOver19) throws Exception;
    List<CoverageTypeDto> selectCoverageTypeList(char isOver19) throws Exception;
    Map<String, Integer> selectPremium(int covCode) throws Exception;
    int insertRepClient(ClientDto dto) throws Exception;
    List<Integer> selectClientNmJumin(String clntNm, String clntJumin) throws Exception;
    int updateClientPhoneEmailByPk(ClientDto dto) throws Exception;
    List<CoverageDto> selectCovDList(String covCode, char isOver19);
    String selectCovNm(int covCode);
    int insertApplyPayment(ApplyPaymentDto dto) throws Exception;
    int insertApplyFinish(ApplyFinishDto dto) throws Exception;
    int insertApplyInsuredList(int aplPk, int clntPk, int prem, Character repYN, char adult) throws Exception;
    List<Integer> selectClientJuminAPhone(String clntJuminA, String clntPhone) throws Exception;
    List<Integer> selectClientJuminAPhoneName(String clntJuminA, String clntPhone, String clntNm ) throws Exception;
    List<MyPageInsSummaryDto> selectApplySummaryList(int clntPk) throws Exception;
    MyPageInsSummaryDto selectInsSummaryDto(int aplPk) throws Exception;
    List<MyPageCompanionDto> selectCompanionDtoList(int aplPk) throws Exception;
    MyPageAccInfo selectAccInfo(int aplPk) throws Exception;
    MyPageClientDto selectClientByAplPk(int aplPk) throws Exception;
    List<MyPageCoverageDetailDto> selectCoverageDetailList(int aplPk, char isOver19) throws Exception;
    int updateClntEmail(int clntPk, String clntEmail) throws Exception;
    PdfInsInfoDto selectInsForPdf(int aplPk) throws Exception;
    ArrayList<PdfClntDto> selectClntForPdf(int aplPk) throws Exception;
    ArrayList<PdfCovDto> selectCovDForPdf(int aplPk) throws Exception;
    Character selectPayedYNByAplPk(int aplPk) throws Exception;
    int updateAplStateCode(int aplPk, int clntPk, int aplStateCode) throws Exception;
    int callAplRefund(int aplPk, int clntPk, String refnBank, String refnAccount, String refnName) throws Exception;
    int selectByCovAgeGen(String covCode, int age, char gen, int day) throws Exception;
    String selectEmailByAplPk(int aplPk) throws Exception;
    int insertClaim(ClaimDto claim) throws Exception;
}
