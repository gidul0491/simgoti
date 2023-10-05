package com.simg.simgoti.service;

import com.simg.simgoti.dto.*;

import java.util.List;

public interface ClientService {
//    List<ClientDto> selectClientList() throws Exception;
    List<CoverageDto> selectCoverageList() throws Exception;
    List<CoverageTypeDto> selectCoverageTypeList(int day) throws Exception;

    int getPremium(String code, int period, int cnt) throws Exception;
    int insertOrUpdateClient(String clntNm, String clntBirth, String clntGen, String clntJumin, String clntPhone, String clntEmail) throws Exception;
//    int selectClientNmJumin(String clntNm, String clntJumin) throws Exception;
    int insertApplyPayment(int premium, String payDueDt) throws Exception;
    int insertApplyFinish(int payPk, String polNo, int comCode, int insComCode, int clntPk,
                        int trPurpose, String trPlace, String trFromDt, String trToDt, int covCode, int clntCnt, int premium) throws Exception;
    int insertApplyInsuredList(int aplPk, int clntPk, int prem, Character repYN) throws Exception;
    int selectClientJuminAPhone(String clntJuminA, String clntPhone) throws Exception;
    List<MyPageInsSummaryDto> selectInsSummaryDtoList(int clntPk) throws Exception;
    MyPageInsSummaryDto selectInsSummaryDto(int aplPk) throws Exception;
    List<MyPageCompanionDto> selectCompanionDtoList(int aplPk) throws Exception;
    MyPageClientDto selectClientByAplPk(int aplPk) throws Exception;
    int updateClntEmail(int clntPk, String clntEmail) throws Exception;
}
