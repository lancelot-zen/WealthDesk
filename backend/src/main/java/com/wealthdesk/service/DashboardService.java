package com.wealthdesk.service;

import com.wealthdesk.document.InsurancePolicy;
import com.wealthdesk.dto.DashboardDto;
import com.wealthdesk.entity.Holding;
import com.wealthdesk.entity.Trade;
import com.wealthdesk.repository.HoldingRepository;
import com.wealthdesk.repository.InsurancePolicyRepository;
import com.wealthdesk.repository.TradeRepository;
import com.wealthdesk.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class DashboardService {

    private final TradeRepository tradeRepository;
    private final HoldingRepository holdingRepository;
    private final InsurancePolicyRepository insurancePolicyRepository;
    private final SecurityUtils securityUtils;

    public DashboardService(TradeRepository tradeRepository,
                            HoldingRepository holdingRepository,
                            InsurancePolicyRepository insurancePolicyRepository,
                            SecurityUtils securityUtils) {
        this.tradeRepository = tradeRepository;
        this.holdingRepository = holdingRepository;
        this.insurancePolicyRepository = insurancePolicyRepository;
        this.securityUtils = securityUtils;
    }

    public DashboardDto getDashboard() {
        Long userId = securityUtils.getCurrentUserId();

        List<Trade> trades = tradeRepository.findByUserIdOrderByTradeDateDesc(userId);
        List<Holding> holdings = holdingRepository.findByUserId(userId);
        List<InsurancePolicy> policies = insurancePolicyRepository.findByUserId(userId);

        BigDecimal totalInvested = holdings.stream()
                .map(Holding::getInvestedValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCurrent = holdings.stream()
                .map(Holding::getCurrentValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalUnrealizedPnl = totalCurrent.subtract(totalInvested);

        BigDecimal totalRealizedPnl = trades.stream()
                .map(Trade::getRealizedPnl)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal portfolioPnlPct = totalInvested.compareTo(BigDecimal.ZERO) == 0
                ? BigDecimal.ZERO
                : totalUnrealizedPnl.multiply(BigDecimal.valueOf(100))
                        .divide(totalInvested, 2, RoundingMode.HALF_UP);

        long activePolicies = policies.stream()
                .filter(p -> p.getStatus() == InsurancePolicy.PolicyStatus.ACTIVE)
                .count();

        BigDecimal totalCoverage = policies.stream()
                .filter(p -> p.getStatus() == InsurancePolicy.PolicyStatus.ACTIVE)
                .map(InsurancePolicy::getSumAssured)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return DashboardDto.builder()
                .totalInvestedValue(totalInvested)
                .totalCurrentValue(totalCurrent)
                .totalUnrealizedPnl(totalUnrealizedPnl)
                .totalRealizedPnl(totalRealizedPnl)
                .portfolioPnlPercentage(portfolioPnlPct)
                .totalHoldings(holdings.size())
                .totalTrades(trades.size())
                .activePolicies((int) activePolicies)
                .totalInsuranceCoverage(totalCoverage)
                .build();
    }
}