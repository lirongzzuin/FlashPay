package com.flashpay.flashpay.repository;

import com.flashpay.flashpay.domain.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {
}