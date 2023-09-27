package com.simg.simgoti.dto;

import lombok.Data;

@Data
public class ApplyPaymentDto {
    private int payPk;
    private int premium;
    private String payDueDt;
}
