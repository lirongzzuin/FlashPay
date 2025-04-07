package com.flashpay.flashpay.service;

import com.flashpay.flashpay.domain.*;
import com.flashpay.flashpay.repository.OrderRepository;
import com.flashpay.flashpay.repository.ProductRepository;
import com.flashpay.flashpay.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private UserRepository userRepository;
    @Mock private ProductRepository productRepository;
    @Mock private RedisLockService redisLockService;

    @InjectMocks private OrderService orderService;

    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("test").build();
        product = Product.builder().id(1L).title("상품").stock(10).build();
    }

    @Test
    void 주문_정상_처리() {
        when(redisLockService.tryLock(anyString(), anyLong())).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        String result = orderService.placeOrder(1L, 1L);

        assertThat(result).isEqualTo("주문 요청이 접수되었습니다.");
        verify(orderRepository).save(any(Order.class));
        verify(redisLockService).releaseLock("product_lock:1");
    }

    @Test
    void 주문_실패_락_획득불가() {
        when(redisLockService.tryLock(anyString(), anyLong())).thenReturn(false);

        String result = orderService.placeOrder(1L, 1L);

        assertThat(result).isEqualTo("다른 사용자가 처리 중입니다. 잠시 후 다시 시도해주세요.");
        verify(orderRepository, never()).save(any());
    }

    @Test
    void 주문_실패_재고없음() {
        product.setStock(0);
        when(redisLockService.tryLock(anyString(), anyLong())).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        String result = orderService.placeOrder(1L, 1L);

        assertThat(result).isEqualTo("품절된 상품입니다.");
        verify(orderRepository, never()).save(any());
        verify(redisLockService).releaseLock("product_lock:1");
    }
}