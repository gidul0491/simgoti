package com.simg.simgoti.dto;

import lombok.Data;

@Data
public class PdfInsInfoDto {
   // a.aplPk, a.trFromDt, a.trToDt, a.trPlace, cd.codeNm as aplState, a.clntCnt, a.premium, c.covNm, cl.clntNm
    String aplPk;
    String trFromDt;
    String trToDt;
    String trPlace;
    int clntCnt;
    int premium;
    String covNm;
    String clntNm;
}
