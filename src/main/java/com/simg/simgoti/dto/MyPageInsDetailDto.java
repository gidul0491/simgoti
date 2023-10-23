package com.simg.simgoti.dto;

import lombok.Data;

import java.util.List;

@Data
public class MyPageInsDetailDto {
    private MyPageInsSummaryDto insSummary;
    private List<MyPageCompanionDto> companionList;
    private MyPageClientDto repClient;
    private MyPageAccInfo myPageAccInfo;
}
