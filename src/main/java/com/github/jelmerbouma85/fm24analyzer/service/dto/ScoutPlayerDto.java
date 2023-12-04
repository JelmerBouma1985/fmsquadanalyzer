package com.github.jelmerbouma85.fm24analyzer.service.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ScoutPlayerDto {

    private String name;
    private String age;
    private String transferValue;
    private String salary;
    private BigDecimal position1Min;
    private BigDecimal position1Max;
    private BigDecimal position2Min;
    private BigDecimal position2Max;
    private BigDecimal position3Min;
    private BigDecimal position3Max;
    private BigDecimal position4Min;
    private BigDecimal position4Max;
    private BigDecimal position5Min;
    private BigDecimal position5Max;
    private BigDecimal position6Min;
    private BigDecimal position6Max;
    private BigDecimal position7Min;
    private BigDecimal position7Max;
    private BigDecimal position8Min;
    private BigDecimal position8Max;
    private BigDecimal position9Min;
    private BigDecimal position9Max;
    private BigDecimal position10Min;
    private BigDecimal position10Max;
    private BigDecimal position11Min;
    private BigDecimal position11Max;
}
