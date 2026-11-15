package com.wealthdesk.service;

import com.wealthdesk.document.InsurancePolicy;
import com.wealthdesk.dto.InsurancePolicyDto;
import com.wealthdesk.repository.InsurancePolicyRepository;
import com.wealthdesk.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InsuranceService {

    private final InsurancePolicyRepository insurancePolicyRepository;
    private final SecurityUtils securityUtils;

    public InsuranceService(InsurancePolicyRepository insurancePolicyRepository, SecurityUtils securityUtils) {
        this.insurancePolicyRepository = insurancePolicyRepository;
        this.securityUtils = securityUtils;
    }

    public List<InsurancePolicyDto> getAllPolicies() {
        Long userId = securityUtils.getCurrentUserId();
        return insurancePolicyRepository.findByUserId(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public InsurancePolicyDto createPolicy(InsurancePolicyDto dto) {
        Long userId = securityUtils.getCurrentUserId();

        InsurancePolicy policy = InsurancePolicy.builder()
                .userId(userId)
                .policyNumber(dto.getPolicyNumber())
                .policyName(dto.getPolicyName())
                .provider(dto.getProvider())
                .type(dto.getType())
                .premium(dto.getPremium())
                .sumAssured(dto.getSumAssured())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .nominee(dto.getNominee())
                .status(dto.getStatus() != null ? dto.getStatus() : InsurancePolicy.PolicyStatus.ACTIVE)
                .build();

        policy = insurancePolicyRepository.save(policy);
        return toDto(policy);
    }

    private InsurancePolicyDto toDto(InsurancePolicy policy) {
        return InsurancePolicyDto.builder()
                .id(policy.getId())
                .policyNumber(policy.getPolicyNumber())
                .policyName(policy.getPolicyName())
                .provider(policy.getProvider())
                .type(policy.getType())
                .premium(policy.getPremium())
                .sumAssured(policy.getSumAssured())
                .startDate(policy.getStartDate())
                .endDate(policy.getEndDate())
                .nominee(policy.getNominee())
                .status(policy.getStatus())
                .build();
    }
}