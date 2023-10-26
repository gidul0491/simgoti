package com.simg.simgoti.service;

import ch.qos.logback.classic.spi.IThrowableProxy;
import com.simg.simgoti.dto.*;

import java.util.List;

public interface ClientService {
//    List<ClientDto> selectClientList() throws Exception;
    List<CoverageDto> selectCoverageList(char isOver19) throws Exception;
    List<CoverageTypeDto> selectCoverageTypeList(int insAge, char gen, int day, char isOver19) throws Exception;

    int insertOrUpdateClient(String clntNm, String clntBirth, String clntGen, String clntJumin, String clntPhone, String clntEmail) throws Exception;
//    int selectClientNmJumin(String clntNm, String clntJumin) throws Exception;
    List<CoverageDto> selectCovDList(String covCode, char inOver19);
    String selectCovNm(String covCode);
    int insertApplyPayment(int premium, String payDueDt, String accBank, String accNum, String accNm) throws Exception;
    int insertApplyFinish(int payPk, String polNo, int comCode, int insComCode, int clntPk,
                        int trPurpose, String trPlace, String trFromDt, String trToDt, int covCode, int clntCnt, int premium) throws Exception;
    int insertApplyInsuredList(int aplPk, int clntPk, int prem, Character repYN, char isOver19) throws Exception;
    List<Integer> selectClientJuminAPhone(String clntJuminA, String clntPhone) throws Exception;
    List<Integer> selectClientJuminAPhoneName(String clntJuminA, String clntPhone, String clntNm) throws Exception;
    List<MyPageInsSummaryDto> selectInsSummaryDtoList(int clntPk) throws Exception;
    MyPageInsSummaryDto selectInsSummaryDto(int aplPk) throws Exception;
    List<MyPageCompanionDto> selectCompanionDtoList(int aplPk) throws Exception;
    MyPageAccInfo selectAccInfo(int aplPk) throws Exception;
    MyPageClientDto selectClientByAplPk(int aplPk) throws Exception;
    int updateClntEmail(int clntPk, String clntEmail) throws Exception;
    Character selectPayedYNByAplPk(int aplPk) throws Exception;
    int updateAplStateCode(int aplPk,int clntPk, int aplStateCode) throws Exception;
    int callAplRefund(int aplPk, int clntPk, String refnBank, String refnAccount, String refnName) throws Exception;
    int insertClaim(ClaimDto claim) throws Exception;
}
