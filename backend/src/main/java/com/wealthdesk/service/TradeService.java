package com.wealthdesk.service;

import com.wealthdesk.dto.TradeDto;
import com.wealthdesk.entity.Trade;
import com.wealthdesk.exception.ApiException;
import com.wealthdesk.repository.TradeRepository;
import com.wealthdesk.security.SecurityUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TradeService {

    private final TradeRepository tradeRepository;
    private final SecurityUtils securityUtils;

    public TradeService(TradeRepository tradeRepository, SecurityUtils securityUtils) {
        this.tradeRepository = tradeRepository;
        this.securityUtils = securityUtils;
    }

    @Cacheable(value = "trades", key = "#root.methodName + '_' + T(com.wealthdesk.security.SecurityUtils).class.getName()")
    public List<TradeDto> getAllTrades() {
        Long userId = securityUtils.getCurrentUserId();
        return tradeRepository.findByUserIdOrderByTradeDateDesc(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Page<TradeDto> getTradesPaginated(Pageable pageable) {
        Long userId = securityUtils.getCurrentUserId();
        return tradeRepository.findByUserId(userId, pageable).map(this::toDto);
    }

    @Transactional
    @CacheEvict(value = "trades", allEntries = true)
    public TradeDto createTrade(TradeDto dto) {
        Long userId = securityUtils.getCurrentUserId();

        Trade trade = Trade.builder()
                .userId(userId)
                .symbol(dto.getSymbol().toUpperCase())
                .name(dto.getName())
                .type(dto.getType())
                .quantity(dto.getQuantity())
                .buyPrice(dto.getBuyPrice())
                .sellPrice(dto.getSellPrice())
                .tradeDate(dto.getTradeDate())
                .exchange(dto.getExchange() != null ? dto.getExchange() : "NSE")
                .build();

        trade = tradeRepository.save(trade);
        return toDto(trade);
    }

    public TradeDto getTradeById(Long id) {
        Trade trade = tradeRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Trade not found: " + id));

        Long userId = securityUtils.getCurrentUserId();
        if (!trade.getUserId().equals(userId)) {
            throw ApiException.notFound("Trade not found: " + id);
        }

        return toDto(trade);
    }

    private TradeDto toDto(Trade trade) {
        return TradeDto.builder()
                .id(trade.getId())
                .symbol(trade.getSymbol())
                .name(trade.getName())
                .type(trade.getType())
                .quantity(trade.getQuantity())
                .buyPrice(trade.getBuyPrice())
                .sellPrice(trade.getSellPrice())
                .tradeDate(trade.getTradeDate())
                .exchange(trade.getExchange())
                .realizedPnl(trade.getRealizedPnl())
                .build();
    }
}