package com.wealthdesk.dto;

import com.wealthdesk.entity.Trade;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeDto {

    private Long id;

    @NotBlank(message = "Symbol is required")
    private String symbol;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Trade type is required")
    private Trade.TradeType type;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Buy price is required")
    @DecimalMin(value = "0.01", message = "Buy price must be positive")
    private BigDecimal buyPrice;

    private BigDecimal sellPrice;

    @NotNull(message = "Trade date is required")
    private LocalDate tradeDate;

    private String exchange;

    private BigDecimal realizedPnl;
}