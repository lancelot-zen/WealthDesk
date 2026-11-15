package com.wealthdesk.controller;

import com.wealthdesk.dto.TradeDto;
import com.wealthdesk.service.TradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trades")
@Tag(name = "Trades", description = "Trade management endpoints")
public class TradeController {

    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @GetMapping
    @Operation(summary = "Get all trades for the authenticated user")
    public ResponseEntity<List<TradeDto>> getAllTrades() {
        return ResponseEntity.ok(tradeService.getAllTrades());
    }

    @GetMapping("/paginated")
    @Operation(summary = "Get trades with pagination")
    public ResponseEntity<Page<TradeDto>> getTradesPaginated(
            @PageableDefault(size = 20, sort = "tradeDate") Pageable pageable) {
        return ResponseEntity.ok(tradeService.getTradesPaginated(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a trade by ID")
    public ResponseEntity<TradeDto> getTradeById(@PathVariable Long id) {
        return ResponseEntity.ok(tradeService.getTradeById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new trade")
    public ResponseEntity<TradeDto> createTrade(@Valid @RequestBody TradeDto tradeDto) {
        TradeDto created = tradeService.createTrade(tradeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}