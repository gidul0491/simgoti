package com.simg.simgoti.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccDto {
    private String accBank;
    private String accNum;
    private String accNm;
    private String accDueDt;
}
