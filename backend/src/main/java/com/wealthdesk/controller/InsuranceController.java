package com.wealthdesk.controller;

import com.wealthdesk.dto.InsurancePolicyDto;
import com.wealthdesk.service.InsuranceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insurance")
@Tag(name = "Insurance", description = "Insurance policy endpoints")
public class InsuranceController {

    private final InsuranceService insuranceService;

    public InsuranceController(InsuranceService insuranceService) {
        this.insuranceService = insuranceService;
    }

    @GetMapping
    @Operation(summary = "Get all insurance policies for the authenticated user")
    public ResponseEntity<List<InsurancePolicyDto>> getAllPolicies() {
        return ResponseEntity.ok(insuranceService.getAllPolicies());
    }

    @PostMapping
    @Operation(summary = "Create a new insurance policy")
    public ResponseEntity<InsurancePolicyDto> createPolicy(@Valid @RequestBody InsurancePolicyDto dto) {
        InsurancePolicyDto created = insuranceService.createPolicy(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}