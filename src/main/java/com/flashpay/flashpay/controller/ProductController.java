package com.flashpay.flashpay.controller;

import com.flashpay.flashpay.common.ApiResponse;
import com.flashpay.flashpay.domain.Product;
import com.flashpay.flashpay.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product Management", description = "Operations related to product management")
public class ProductController {

    private final ProductService productService;

    // 상품 등록 (관리자용)
    @Operation(summary = "상품 등록", description = "관리자가 신규 상품을 등록합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<Product>> createProduct(
            @Parameter(description = "상품명") @RequestParam String title,
            @Parameter(description = "가격") @RequestParam int price,
            @Parameter(description = "재고 수량") @RequestParam int stock,
            @Parameter(description = "판매 시작 시간") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime) {

        Product product = productService.createProduct(title, price, stock, startTime);
        return ResponseEntity.ok(ApiResponse.success("상품 등록 성공", product));
    }

    // 상품 전체 조회
    @Operation(summary = "전체 상품 목록 조회", description = "등록된 모든 상품을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(ApiResponse.success("전체 상품 목록 조회 성공", products));
    }
}