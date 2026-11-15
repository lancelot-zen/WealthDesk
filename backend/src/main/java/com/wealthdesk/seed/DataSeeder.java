package com.wealthdesk.seed;

import com.wealthdesk.document.InsurancePolicy;
import com.wealthdesk.entity.Holding;
import com.wealthdesk.entity.Trade;
import com.wealthdesk.entity.User;
import com.wealthdesk.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final TradeRepository tradeRepository;
    private final HoldingRepository holdingRepository;
    private final InsurancePolicyRepository insurancePolicyRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      TradeRepository tradeRepository,
                      HoldingRepository holdingRepository,
                      InsurancePolicyRepository insurancePolicyRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tradeRepository = tradeRepository;
        this.holdingRepository = holdingRepository;
        this.insurancePolicyRepository = insurancePolicyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Database already seeded — skipping.");
            return;
        }

        log.info("Seeding database with demo data...");

        // ── Users ──
        User user1 = userRepository.save(User.builder()
                .username("rahul")
                .email("rahul@wealthdesk.com")
                .password(passwordEncoder.encode("password123"))
                .fullName("Rahul Sharma")
                .roles(Set.of("ROLE_USER"))
                .build());

        User user2 = userRepository.save(User.builder()
                .username("priya")
                .email("priya@wealthdesk.com")
                .password(passwordEncoder.encode("password123"))
                .fullName("Priya Patel")
                .roles(Set.of("ROLE_USER", "ROLE_ADMIN"))
                .build());

        log.info("Created users: {}, {}", user1.getUsername(), user2.getUsername());

        // ── Trades for user1 ──
        List<Trade> trades = List.of(
                trade(user1.getId(), "RELIANCE", "Reliance Industries", Trade.TradeType.BUY, 50, "2450.00", null, "2026-01-15"),
                trade(user1.getId(), "TCS", "Tata Consultancy Services", Trade.TradeType.BUY, 30, "3520.00", null, "2026-01-20"),
                trade(user1.getId(), "INFY", "Infosys Ltd", Trade.TradeType.BUY, 100, "1480.00", null, "2026-02-01"),
                trade(user1.getId(), "HDFCBANK", "HDFC Bank Ltd", Trade.TradeType.BUY, 40, "1620.00", null, "2026-02-10"),
                trade(user1.getId(), "WIPRO", "Wipro Ltd", Trade.TradeType.BUY, 200, "420.00", null, "2026-02-15"),
                trade(user1.getId(), "RELIANCE", "Reliance Industries", Trade.TradeType.SELL, 25, "2450.00", "2580.00", "2026-03-01"),
                trade(user1.getId(), "TCS", "Tata Consultancy Services", Trade.TradeType.SELL, 15, "3520.00", "3680.00", "2026-03-10"),
                trade(user1.getId(), "INFY", "Infosys Ltd", Trade.TradeType.SELL, 50, "1480.00", "1550.00", "2026-03-15"),
                trade(user1.getId(), "ICICIBANK", "ICICI Bank Ltd", Trade.TradeType.BUY, 60, "980.00", null, "2026-03-20"),
                trade(user1.getId(), "SBIN", "State Bank of India", Trade.TradeType.BUY, 150, "620.00", null, "2026-04-01"),
                trade(user1.getId(), "BAJFINANCE", "Bajaj Finance Ltd", Trade.TradeType.BUY, 20, "6800.00", null, "2026-04-05"),
                trade(user1.getId(), "WIPRO", "Wipro Ltd", Trade.TradeType.SELL, 100, "420.00", "395.00", "2026-04-10"),
                trade(user1.getId(), "MARUTI", "Maruti Suzuki India", Trade.TradeType.BUY, 10, "10500.00", null, "2026-04-15"),
                trade(user1.getId(), "HDFCBANK", "HDFC Bank Ltd", Trade.TradeType.SELL, 20, "1620.00", "1710.00", "2026-05-01"),
                trade(user1.getId(), "TATAMOTORS", "Tata Motors Ltd", Trade.TradeType.BUY, 80, "650.00", null, "2026-05-10"),
                // Trades for user2
                trade(user2.getId(), "RELIANCE", "Reliance Industries", Trade.TradeType.BUY, 100, "2500.00", null, "2026-01-10"),
                trade(user2.getId(), "INFY", "Infosys Ltd", Trade.TradeType.BUY, 75, "1500.00", null, "2026-02-05"),
                trade(user2.getId(), "HDFCBANK", "HDFC Bank Ltd", Trade.TradeType.BUY, 50, "1650.00", null, "2026-02-20"),
                trade(user2.getId(), "RELIANCE", "Reliance Industries", Trade.TradeType.SELL, 50, "2500.00", "2620.00", "2026-03-25"),
                trade(user2.getId(), "ASIANPAINT", "Asian Paints Ltd", Trade.TradeType.BUY, 30, "3200.00", null, "2026-04-01"),
                trade(user2.getId(), "TITAN", "Titan Company Ltd", Trade.TradeType.BUY, 25, "3100.00", null, "2026-04-10"),
                trade(user2.getId(), "INFY", "Infosys Ltd", Trade.TradeType.SELL, 40, "1500.00", "1420.00", "2026-04-20"),
                trade(user2.getId(), "NESTLEIND", "Nestle India Ltd", Trade.TradeType.BUY, 5, "22000.00", null, "2026-05-01"),
                trade(user2.getId(), "SUNPHARMA", "Sun Pharma Industries", Trade.TradeType.BUY, 60, "1150.00", null, "2026-05-15"),
                trade(user2.getId(), "HDFCBANK", "HDFC Bank Ltd", Trade.TradeType.SELL, 25, "1650.00", "1720.00", "2026-06-01")
        );
        tradeRepository.saveAll(trades);
        log.info("Created {} trades", trades.size());

        // ── Holdings for user1 ──
        List<Holding> holdings = List.of(
                holding(user1.getId(), "RELIANCE", "Reliance Industries", 25, "2450.00", "2610.00", "NSE", "Energy"),
                holding(user1.getId(), "TCS", "Tata Consultancy Services", 15, "3520.00", "3750.00", "NSE", "IT"),
                holding(user1.getId(), "INFY", "Infosys Ltd", 50, "1480.00", "1560.00", "NSE", "IT"),
                holding(user1.getId(), "HDFCBANK", "HDFC Bank Ltd", 20, "1620.00", "1730.00", "NSE", "Banking"),
                holding(user1.getId(), "WIPRO", "Wipro Ltd", 100, "420.00", "405.00", "NSE", "IT"),
                holding(user1.getId(), "ICICIBANK", "ICICI Bank Ltd", 60, "980.00", "1050.00", "NSE", "Banking"),
                holding(user1.getId(), "SBIN", "State Bank of India", 150, "620.00", "645.00", "NSE", "Banking"),
                holding(user1.getId(), "BAJFINANCE", "Bajaj Finance Ltd", 20, "6800.00", "7150.00", "NSE", "Finance"),
                // Holdings for user2
                holding(user2.getId(), "RELIANCE", "Reliance Industries", 50, "2500.00", "2610.00", "NSE", "Energy"),
                holding(user2.getId(), "INFY", "Infosys Ltd", 35, "1500.00", "1560.00", "NSE", "IT"),
                holding(user2.getId(), "HDFCBANK", "HDFC Bank Ltd", 25, "1650.00", "1730.00", "NSE", "Banking"),
                holding(user2.getId(), "ASIANPAINT", "Asian Paints Ltd", 30, "3200.00", "3350.00", "NSE", "Consumer"),
                holding(user2.getId(), "TITAN", "Titan Company Ltd", 25, "3100.00", "3280.00", "NSE", "Consumer")
        );
        holdingRepository.saveAll(holdings);
        log.info("Created {} holdings", holdings.size());

        // ── Insurance Policies ──
        List<InsurancePolicy> policies = List.of(
                policy(user1.getId(), "LIC-TL-001", "LIC Term Plan", "LIC", InsurancePolicy.PolicyType.TERM_LIFE,
                        "12000.00", "10000000.00", "2024-01-01", "2054-01-01", "Anita Sharma"),
                policy(user1.getId(), "STAR-HL-002", "Star Health Insurance", "Star Health", InsurancePolicy.PolicyType.HEALTH,
                        "18000.00", "500000.00", "2026-01-01", "2027-01-01", "Self"),
                policy(user1.getId(), "HDFC-UL-003", "HDFC ULIP Growth", "HDFC Life", InsurancePolicy.PolicyType.ULIP,
                        "50000.00", "2500000.00", "2025-06-01", "2040-06-01", "Rahul Sharma"),
                policy(user1.getId(), "ICICI-MT-004", "ICICI Lombard Motor", "ICICI Lombard", InsurancePolicy.PolicyType.MOTOR,
                        "8500.00", "750000.00", "2026-03-01", "2027-03-01", "Self"),
                policy(user2.getId(), "MAX-TL-005", "Max Life Term Plan", "Max Life", InsurancePolicy.PolicyType.TERM_LIFE,
                        "15000.00", "15000000.00", "2023-06-01", "2053-06-01", "Amit Patel"),
                policy(user2.getId(), "NIVA-HL-006", "Niva Bupa Health", "Niva Bupa", InsurancePolicy.PolicyType.HEALTH,
                        "22000.00", "1000000.00", "2026-02-01", "2027-02-01", "Family"),
                policy(user2.getId(), "TATA-HM-007", "Tata AIG Home Insurance", "Tata AIG", InsurancePolicy.PolicyType.HOME,
                        "5000.00", "5000000.00", "2026-01-15", "2027-01-15", "Self")
        );
        insurancePolicyRepository.saveAll(policies);
        log.info("Created {} insurance policies", policies.size());

        log.info("Database seeding complete!");
    }

    private Trade trade(Long userId, String symbol, String name, Trade.TradeType type,
                        int qty, String buyPrice, String sellPrice, String date) {
        return Trade.builder()
                .userId(userId)
                .symbol(symbol)
                .name(name)
                .type(type)
                .quantity(qty)
                .buyPrice(new BigDecimal(buyPrice))
                .sellPrice(sellPrice != null ? new BigDecimal(sellPrice) : null)
                .tradeDate(LocalDate.parse(date))
                .exchange("NSE")
                .build();
    }

    private Holding holding(Long userId, String symbol, String name, int qty,
                            String avgBuy, String current, String exchange, String sector) {
        return Holding.builder()
                .userId(userId)
                .symbol(symbol)
                .name(name)
                .quantity(qty)
                .avgBuyPrice(new BigDecimal(avgBuy))
                .currentPrice(new BigDecimal(current))
                .exchange(exchange)
                .sector(sector)
                .build();
    }

    private InsurancePolicy policy(Long userId, String policyNumber, String policyName,
                                   String provider, InsurancePolicy.PolicyType type,
                                   String premium, String sumAssured,
                                   String startDate, String endDate, String nominee) {
        return InsurancePolicy.builder()
                .userId(userId)
                .policyNumber(policyNumber)
                .policyName(policyName)
                .provider(provider)
                .type(type)
                .premium(new BigDecimal(premium))
                .sumAssured(new BigDecimal(sumAssured))
                .startDate(LocalDate.parse(startDate))
                .endDate(LocalDate.parse(endDate))
                .nominee(nominee)
                .status(InsurancePolicy.PolicyStatus.ACTIVE)
                .build();
    }
}