package com.simg.simgoti.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CompanionDto {
    private String companionNum;
    private String companionNm;
    private String companionJuminA;
    private String companionJuminB;
    private int prem;
    private int clntPk;
}
