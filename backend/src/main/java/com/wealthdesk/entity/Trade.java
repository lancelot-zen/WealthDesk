package com.wealthdesk.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "trades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 20)
    private String symbol;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TradeType type;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "buy_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal buyPrice;

    @Column(name = "sell_price", precision = 12, scale = 2)
    private BigDecimal sellPrice;

    @Column(name = "trade_date", nullable = false)
    private LocalDate tradeDate;

    @Column(length = 10)
    private String exchange;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public BigDecimal getRealizedPnl() {
        if (sellPrice == null || type == TradeType.BUY) {
            return BigDecimal.ZERO;
        }
        return sellPrice.subtract(buyPrice).multiply(BigDecimal.valueOf(quantity));
    }

    public enum TradeType {
        BUY, SELL
    }
}