package com.simg.simgoti.dto;

import lombok.Data;

@Data
public class MyPageInsSummaryDto {
    private int aplPk;
    private String covNm;
    private int period;
    private String trFromDt;
    private String trToDt;
    private String trPlace;
    private String aplState;
    private int clntCnt;
    private int premium;
}
