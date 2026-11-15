package com.wealthdesk.repository;

import com.wealthdesk.entity.Trade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {

    List<Trade> findByUserIdOrderByTradeDateDesc(Long userId);

    Page<Trade> findByUserId(Long userId, Pageable pageable);

    List<Trade> findByUserIdAndSymbol(Long userId, String symbol);

    List<Trade> findByUserIdAndType(Long userId, Trade.TradeType type);
}