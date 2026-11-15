package com.wealthdesk.repository;

import com.wealthdesk.entity.Holding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HoldingRepository extends JpaRepository<Holding, Long> {

    List<Holding> findByUserId(Long userId);

    Optional<Holding> findByUserIdAndSymbol(Long userId, String symbol);

    List<Holding> findByUserIdAndSector(Long userId, String sector);
}