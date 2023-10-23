package com.simg.simgoti.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CompanionDto {
    private String companionNum;
    private String companionNm;
    private String companionJuminA;
    private String companionJuminB;
    private String covNm;
    private int prem;
    private int clntPk;
    private char isOver19;
    private List<CoverageDto> covDList;

    public CompanionDto(String companionNum, String companionNm, String companionJuminA, String companionJuminB,String covNm, int prem, int clntPk, char isOver19){
        this.companionNum = companionNum;
        this.companionNm = companionNm;
        this.companionJuminA = companionJuminA;
        this.companionJuminB = companionJuminB;
        this.covNm = covNm;
        this.prem = prem;
        this.clntPk = clntPk;
        this.isOver19 = isOver19;
    }
}
