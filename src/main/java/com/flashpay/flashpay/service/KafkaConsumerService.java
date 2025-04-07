
package com.flashpay.flashpay.service;

import com.flashpay.flashpay.config.KafkaConfig;
import com.flashpay.flashpay.domain.Order;
import com.flashpay.flashpay.domain.Settlement;
import com.flashpay.flashpay.repository.OrderRepository;
import com.flashpay.flashpay.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final OrderRepository orderRepository;
    private final SettlementRepository settlementRepository;

    @KafkaListener(topics = KafkaConfig.ORDER_TOPIC, groupId = "flashpay-consumer")
    public void consume(String message) {
        // 메시지: "orderId:userId:productId"
        String[] parts = message.split(":");
        if (parts.length < 3) return;

        Long orderId = Long.parseLong(parts[0]);

        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            int amount = order.getProduct().getPrice();

            Settlement settlement = Settlement.builder()
                    .order(order)
                    .amount(amount)
                    .settledAt(LocalDateTime.now())
                    .build();

            settlementRepository.save(settlement);
            System.out.println("✅ 정산 처리 완료 for orderId=" + orderId);
        }
    }
}