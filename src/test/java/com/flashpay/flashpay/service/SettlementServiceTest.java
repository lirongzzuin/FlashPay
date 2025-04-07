package com.flashpay.flashpay.service;

import com.flashpay.flashpay.domain.Order;
import com.flashpay.flashpay.domain.Settlement;
import com.flashpay.flashpay.dto.SettlementResponseDto;
import com.flashpay.flashpay.repository.SettlementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SettlementServiceTest {

    @Mock
    private SettlementRepository settlementRepository;

    @InjectMocks
    private SettlementService settlementService;

    private Settlement settlement;

    @BeforeEach
    void setUp() {
        Order order = Order.builder().id(1L).build();
        settlement = Settlement.builder()
                .id(1L)
                .order(order)
                .amount(1000)
                .settledAt(LocalDateTime.now())
                .build();
    }

    @Test
    void 전체_정산_조회() {
        when(settlementRepository.findAll()).thenReturn(List.of(settlement));

        List<SettlementResponseDto> result = settlementService.getAllSettlementsDto();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAmount()).isEqualTo(1000);
    }

    @Test
    void 사용자별_정산_총합_조회() {
        when(settlementRepository.findByOrderUserId(1L)).thenReturn(List.of(settlement));

        int totalAmount = settlementService.getTotalSettlementAmountByUserId(1L);

        assertThat(totalAmount).isEqualTo(1000);
    }
}
