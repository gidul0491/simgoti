package com.simg.simgoti.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplySummaryDto {
    private int period;
    private String startDt;
    private String endDt;
    private String trPlace;
    private String covCode;
    private int cnt;
    private int totalPrem;
}
