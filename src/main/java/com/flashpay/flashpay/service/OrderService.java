package com.flashpay.flashpay.service;

import com.flashpay.flashpay.domain.*;
import com.flashpay.flashpay.repository.OrderRepository;
import com.flashpay.flashpay.repository.ProductRepository;
import com.flashpay.flashpay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final RedisLockService redisLockService;
    private final KafkaProducerService kafkaProducerService;

    private static final long LOCK_TIMEOUT = 3000L; // 3초

    @Transactional
    public String placeOrder(Long userId, Long productId) {
        String lockKey = "product_lock:" + productId;

        if (!redisLockService.tryLock(lockKey, LOCK_TIMEOUT)) {
            return "다른 사용자가 처리 중입니다. 잠시 후 다시 시도해주세요.";
        }

        try {
            User user = userRepository.findById(userId).orElseThrow();
            Product product = productRepository.findById(productId).orElseThrow();

            if (product.getStock() <= 0) {
                return "품절된 상품입니다.";
            }

            product.setStock(product.getStock() - 1); // 재고 감소

            Order order = Order.builder()
                    .user(user)
                    .product(product)
                    .status(OrderStatus.PENDING)
                    .build();
            orderRepository.save(order);

            kafkaProducerService.sendOrderMessage(order); // 반드시 return 전에 전송

            return "주문 요청이 접수되었습니다.";

        } finally {
            redisLockService.releaseLock(lockKey);
        }
    }
}