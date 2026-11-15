package com.wealthdesk.service;

import com.wealthdesk.dto.HoldingDto;
import com.wealthdesk.entity.Holding;
import com.wealthdesk.repository.HoldingRepository;
import com.wealthdesk.security.SecurityUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HoldingService {

    private final HoldingRepository holdingRepository;
    private final SecurityUtils securityUtils;

    public HoldingService(HoldingRepository holdingRepository, SecurityUtils securityUtils) {
        this.holdingRepository = holdingRepository;
        this.securityUtils = securityUtils;
    }

    @Cacheable(value = "holdings", key = "'user_holdings'")
    public List<HoldingDto> getAllHoldings() {
        Long userId = securityUtils.getCurrentUserId();
        return holdingRepository.findByUserId(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private HoldingDto toDto(Holding holding) {
        return HoldingDto.builder()
                .id(holding.getId())
                .symbol(holding.getSymbol())
                .name(holding.getName())
                .quantity(holding.getQuantity())
                .avgBuyPrice(holding.getAvgBuyPrice())
                .currentPrice(holding.getCurrentPrice())
                .exchange(holding.getExchange())
                .sector(holding.getSector())
                .investedValue(holding.getInvestedValue())
                .currentValue(holding.getCurrentValue())
                .unrealizedPnl(holding.getUnrealizedPnl())
                .pnlPercentage(holding.getPnlPercentage())
                .build();
    }
}