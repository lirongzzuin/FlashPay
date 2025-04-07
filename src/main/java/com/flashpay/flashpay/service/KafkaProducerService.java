package com.flashpay.flashpay.service;

import com.flashpay.flashpay.config.KafkaConfig;
import com.flashpay.flashpay.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendOrderMessage(Order order) {
        String payload = order.getId() + ":" + order.getUser().getId() + ":" + order.getProduct().getId();
        kafkaTemplate.send(KafkaConfig.ORDER_TOPIC, payload);
        System.out.println("✅ Kafka 메시지 전송 완료: " + payload);
    }
}