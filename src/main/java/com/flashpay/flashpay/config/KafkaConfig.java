package com.flashpay.flashpay.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    public static final String ORDER_TOPIC = "order-topic";

    @Bean
    public NewTopic orderTopic() {
        return new NewTopic(ORDER_TOPIC, 1, (short) 1);
    }
}