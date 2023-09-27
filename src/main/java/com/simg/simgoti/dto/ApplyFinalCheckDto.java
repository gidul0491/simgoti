package com.simg.simgoti.dto;

import lombok.Data;

import java.util.List;

@Data
public class ApplyFinalCheckDto {
    ApplySummaryDto appSummaryDto;
    List<CompanionDto> companionDtoList;
    ClientDto repClientDto;
}
