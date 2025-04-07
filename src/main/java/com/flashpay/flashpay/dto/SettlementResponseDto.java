package com.flashpay.flashpay.dto;

import com.flashpay.flashpay.domain.Settlement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class SettlementResponseDto {
    private Long id;
    private Long orderId;
    private int amount;
    private LocalDateTime settledAt;

    public static SettlementResponseDto from(Settlement settlement) {
        return SettlementResponseDto.builder()
            .id(settlement.getId())
            .orderId(settlement.getOrder().getId())
            .amount(settlement.getAmount())
            .settledAt(settlement.getSettledAt())
            .build();
    }
}
