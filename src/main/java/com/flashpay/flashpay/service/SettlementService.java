package com.flashpay.flashpay.service;

import com.flashpay.flashpay.domain.Settlement;
import com.flashpay.flashpay.dto.SettlementResponseDto;
import com.flashpay.flashpay.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRepository settlementRepository;

    public List<SettlementResponseDto> getAllSettlementsDto() {
        return settlementRepository.findAll().stream()
                .map(SettlementResponseDto::from)
                .toList();
    }

    public List<SettlementResponseDto> getSettlementsByUserIdDto(Long userId) {
        return settlementRepository.findByOrderUserId(userId).stream()
                .map(SettlementResponseDto::from)
                .toList();
    }

    public SettlementResponseDto getLatestSettlementByUserIdDto(Long userId) {
        return settlementRepository.findTopByOrderUserIdOrderBySettledAtDesc(userId)
                .map(SettlementResponseDto::from)
                .orElseThrow(() -> new IllegalArgumentException("정산 데이터가 존재하지 않습니다."));
    }

    public int getTotalSettlementAmountByUserId(Long userId) {
        return settlementRepository.findByOrderUserId(userId).stream()
                .mapToInt(Settlement::getAmount)
                .sum();
    }

    public List<SettlementResponseDto> getSettlementsByProductIdDto(Long productId) {
        return settlementRepository.findByOrderProductId(productId).stream()
                .map(SettlementResponseDto::from)
                .toList();
    }

    public SettlementResponseDto getLatestSettlementByProductIdDto(Long productId) {
        return settlementRepository.findTopByOrderProductIdOrderBySettledAtDesc(productId)
                .map(SettlementResponseDto::from)
                .orElseThrow(() -> new IllegalArgumentException("정산 데이터가 존재하지 않습니다."));
    }

    public int getTotalSettlementAmountByProductId(Long productId) {
        return settlementRepository.findByOrderProductId(productId).stream()
                .mapToInt(Settlement::getAmount)
                .sum();
    }

    public List<SettlementResponseDto> getLatestSettlementsDto(int count) {
        return settlementRepository.findTopN(count).stream()
                .map(SettlementResponseDto::from)
                .toList();
    }
}