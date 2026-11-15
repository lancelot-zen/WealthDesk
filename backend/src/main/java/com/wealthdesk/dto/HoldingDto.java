package com.wealthdesk.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HoldingDto {

    private Long id;
    private String symbol;
    private String name;
    private Integer quantity;
    private BigDecimal avgBuyPrice;
    private BigDecimal currentPrice;
    private String exchange;
    private String sector;
    private BigDecimal investedValue;
    private BigDecimal currentValue;
    private BigDecimal unrealizedPnl;
    private BigDecimal pnlPercentage;
}