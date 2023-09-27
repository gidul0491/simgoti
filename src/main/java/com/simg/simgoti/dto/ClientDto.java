package com.simg.simgoti.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {
    private int clntPk;
    private String clntNm;
    private String clntPhone;
    private String clntEmail;
    private String clntBirth;
    private String clntGen;
    private String clntJumin;
}
