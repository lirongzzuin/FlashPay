package com.flashpay.flashpay.repository;

import com.flashpay.flashpay.domain.Order;
import com.flashpay.flashpay.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}