package com.flashpay.flashpay.controller;

import com.flashpay.flashpay.common.ApiResponse;
import com.flashpay.flashpay.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Order")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "주문 요청", description = "사용자가 상품을 주문합니다.")
    @PostMapping
    public ResponseEntity<?> order(
            @Parameter(description = "사용자 ID") @RequestParam Long userId,
            @Parameter(description = "상품 ID") @RequestParam Long productId) {

        String result = orderService.placeOrder(userId, productId);
        return ResponseEntity.ok(ApiResponse.success("주문 요청 처리 성공", result));
    }
}