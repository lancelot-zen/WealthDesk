package com.wealthdesk.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDto {

    private BigDecimal totalInvestedValue;
    private BigDecimal totalCurrentValue;
    private BigDecimal totalUnrealizedPnl;
    private BigDecimal totalRealizedPnl;
    private BigDecimal portfolioPnlPercentage;
    private int totalHoldings;
    private int totalTrades;
    private int activePolicies;
    private BigDecimal totalInsuranceCoverage;
}