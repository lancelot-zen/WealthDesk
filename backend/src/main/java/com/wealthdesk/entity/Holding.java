package com.wealthdesk.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "holdings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Holding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 20)
    private String symbol;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "avg_buy_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal avgBuyPrice;

    @Column(name = "current_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal currentPrice;

    @Column(length = 10)
    private String exchange;

    @Column(length = 30)
    private String sector;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public BigDecimal getInvestedValue() {
        return avgBuyPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal getCurrentValue() {
        return currentPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal getUnrealizedPnl() {
        return getCurrentValue().subtract(getInvestedValue());
    }

    public BigDecimal getPnlPercentage() {
        BigDecimal invested = getInvestedValue();
        if (invested.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return getUnrealizedPnl()
                .multiply(BigDecimal.valueOf(100))
                .divide(invested, 2, java.math.RoundingMode.HALF_UP);
    }
}