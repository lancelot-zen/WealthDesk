package com.wealthdesk.dto;

import com.wealthdesk.document.InsurancePolicy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsurancePolicyDto {

    private String id;

    @NotBlank(message = "Policy number is required")
    private String policyNumber;

    @NotBlank(message = "Policy name is required")
    private String policyName;

    @NotBlank(message = "Provider is required")
    private String provider;

    @NotNull(message = "Policy type is required")
    private InsurancePolicy.PolicyType type;

    @NotNull(message = "Premium is required")
    private BigDecimal premium;

    @NotNull(message = "Sum assured is required")
    private BigDecimal sumAssured;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private String nominee;

    private InsurancePolicy.PolicyStatus status;
}