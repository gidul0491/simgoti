package com.simg.simgoti.dto;

import lombok.Data;

import java.util.List;

@Data
public class MyPageCompanionDto {
//    private String companionNum;
    private String clntNm;
    private String clntBirth;
    private String clntGen;
    private int premium;
    private List<MyPageCoverageDetailDto> covDList;

}
