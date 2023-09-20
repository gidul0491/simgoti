package com.simg.simgoti.dto;

import lombok.Data;

@Data
public class CoverageDto {
    private String covCode;
    private String covNm;
    private int covPremium;
    private int covPremiumMin;
    private String covDCode;
    private String covDNm;
    private int covDAmt;
}
