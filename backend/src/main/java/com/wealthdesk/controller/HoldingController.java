package com.wealthdesk.controller;

import com.wealthdesk.dto.HoldingDto;
import com.wealthdesk.service.HoldingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/holdings")
@Tag(name = "Holdings", description = "Portfolio holdings endpoints")
public class HoldingController {

    private final HoldingService holdingService;

    public HoldingController(HoldingService holdingService) {
        this.holdingService = holdingService;
    }

    @GetMapping
    @Operation(summary = "Get all holdings for the authenticated user")
    public ResponseEntity<List<HoldingDto>> getAllHoldings() {
        return ResponseEntity.ok(holdingService.getAllHoldings());
    }
}