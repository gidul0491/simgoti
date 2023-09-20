package com.simg.simgoti.dto;

import lombok.Data;

@Data
public class CoverageTypeDto {
    private String covCode;
    private String covNm;
    private int covPremium;
    private int covPremiumMin;
    private int totPremium = 0;
}
