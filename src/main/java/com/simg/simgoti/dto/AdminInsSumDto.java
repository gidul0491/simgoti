package com.simg.simgoti.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class AdminInsSumDto {
    private int aplPk;
    private String clntNm;
    private String trPlace;
    private int covCode;
    private String covNm;
    private int clntCnt;
    private int premium;
    private String aplCdt;
    private int aplStateCode;
    private String aplState;
    private String payedYN;
//    private String useYN;
}
