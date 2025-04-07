package com.flashpay.flashpay.service;

import com.flashpay.flashpay.domain.Order;
import com.flashpay.flashpay.domain.Settlement;
import com.flashpay.flashpay.repository.OrderRepository;
import com.flashpay.flashpay.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final OrderRepository orderRepository;

    public List<Settlement> getSettlementsByUserId(Long userId) {
        List<Order> userOrders = orderRepository.findAll().stream()
                .filter(order -> order.getUser().getId().equals(userId))
                .toList();

        return settlementRepository.findAll().stream()
                .filter(settlement -> userOrders.contains(settlement.getOrder()))
                .collect(Collectors.toList());
    }
}