package com.simg.simgoti.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ApplyFinishDto {
    private int aplPk;
    private int payPk;
    private String polNo;
    private int comCode;
    private int insComCode;
    private int clntPk;
    private int trPurpose;
    private String trPlace;
    private String trFromDt;
    private String trToDt;
    private int covCode;
    private int clntCnt;
    private int premium;

}
