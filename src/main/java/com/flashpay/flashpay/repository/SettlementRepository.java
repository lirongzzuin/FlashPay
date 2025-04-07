package com.flashpay.flashpay.repository;

import com.flashpay.flashpay.domain.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {
    List<Settlement> findByOrderUserId(Long userId);

    Optional<Settlement> findTopByOrderUserIdOrderBySettledAtDesc(Long userId);

    List<Settlement> findByOrderProductId(Long productId);

    Optional<Settlement> findTopByOrderProductIdOrderBySettledAtDesc(Long productId);

    @Query(value = "SELECT s FROM Settlement s ORDER BY s.settledAt DESC LIMIT ?1")
    List<Settlement> findTopN(int count);
}