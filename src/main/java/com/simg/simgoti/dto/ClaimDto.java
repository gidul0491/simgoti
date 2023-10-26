package com.simg.simgoti.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClaimDto {
    String clntNm;
    String clntJumin;
    String benefRel;
    String benefNm;
    String benefEmail;
    String benefPhone;
    String clmDt;
    String clmPlace;
    String clmAmt;
    String clmDetail;
}
