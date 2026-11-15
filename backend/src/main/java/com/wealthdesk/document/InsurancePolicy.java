package com.wealthdesk.document;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "insurance_policies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsurancePolicy {

    @Id
    private String id;

    private Long userId;

    private String policyNumber;

    private String policyName;

    private String provider;

    private PolicyType type;

    private BigDecimal premium;

    private BigDecimal sumAssured;

    private LocalDate startDate;

    private LocalDate endDate;

    private String nominee;

    private PolicyStatus status;

    @CreatedDate
    private LocalDateTime createdAt;

    public enum PolicyType {
        TERM_LIFE, HEALTH, MOTOR, HOME, TRAVEL, ULIP
    }

    public enum PolicyStatus {
        ACTIVE, EXPIRED, CANCELLED, PENDING
    }
}