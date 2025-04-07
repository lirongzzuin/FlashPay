package com.flashpay.flashpay.controller;

import com.flashpay.flashpay.dto.SettlementResponseDto;
import com.flashpay.flashpay.service.SettlementService;
import com.flashpay.flashpay.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@RestController
@RequestMapping("/api/settlements")
@RequiredArgsConstructor
@Tag(name = "Settlement")
public class SettlementController {

    private final SettlementService settlementService;

    @Operation(summary = "전체 정산 목록 조회", description = "모든 정산 데이터를 조회합니다.")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<SettlementResponseDto>> getAllSettlements() {
        return ResponseEntity.ok(ApiResponse.success("전체 정산 목록 조회 성공", settlementService.getAllSettlementsDto()));
    }

    @Operation(summary = "유저별 정산 목록 조회", description = "특정 사용자에 대한 정산 데이터를 조회합니다.")
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}")
    public ResponseEntity<List<SettlementResponseDto>> getSettlementsByUser(@Parameter(description = "사용자 ID") @PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success("유저별 정산 목록 조회 성공", settlementService.getSettlementsByUserIdDto(userId)));
    }

    @Operation(summary = "유저 최신 정산 조회", description = "특정 사용자의 최신 정산 데이터를 조회합니다.")
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/latest")
    public ResponseEntity<SettlementResponseDto> getLatestSettlementByUser(@Parameter(description = "사용자 ID") @PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success("유저 최신 정산 조회 성공", settlementService.getLatestSettlementByUserIdDto(userId)));
    }

    @Operation(summary = "유저 총 정산 금액 조회", description = "특정 사용자의 총 정산 금액을 조회합니다.")
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/total")
    public ResponseEntity<Integer> getTotalSettlementAmountByUser(@Parameter(description = "사용자 ID") @PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success("유저 총 정산 금액 조회 성공", settlementService.getTotalSettlementAmountByUserId(userId)));
    }

    @Operation(summary = "상품별 정산 목록 조회", description = "특정 상품에 대한 정산 데이터를 조회합니다.")
    @RequestMapping(method = RequestMethod.GET, path = "/products/{productId}")
    public ResponseEntity<List<SettlementResponseDto>> getSettlementsByProduct(@Parameter(description = "상품 ID") @PathVariable Long productId) {
        return ResponseEntity.ok(ApiResponse.success("상품별 정산 목록 조회 성공", settlementService.getSettlementsByProductIdDto(productId)));
    }

    @Operation(summary = "상품 최신 정산 조회", description = "특정 상품의 최신 정산 데이터를 조회합니다.")
    @RequestMapping(method = RequestMethod.GET, path = "/products/{productId}/latest")
    public ResponseEntity<SettlementResponseDto> getLatestSettlementByProduct(@Parameter(description = "상품 ID") @PathVariable Long productId) {
        return ResponseEntity.ok(ApiResponse.success("상품 최신 정산 조회 성공", settlementService.getLatestSettlementByProductIdDto(productId)));
    }

    @Operation(summary = "상품 총 정산 금액 조회", description = "특정 상품의 총 정산 금액을 조회합니다.")
    @RequestMapping(method = RequestMethod.GET, path = "/products/{productId}/total")
    public ResponseEntity<Integer> getTotalSettlementAmountByProduct(@Parameter(description = "상품 ID") @PathVariable Long productId) {
        return ResponseEntity.ok(ApiResponse.success("상품 총 정산 금액 조회 성공", settlementService.getTotalSettlementAmountByProductId(productId)));
    }

    @Operation(summary = "최신 정산 목록 조회", description = "최신 정산 데이터를 조회합니다.")
    @RequestMapping(method = RequestMethod.GET, path = "/latest")
    public ResponseEntity<List<SettlementResponseDto>> getLatestSettlements() {
        List<SettlementResponseDto> settlements = settlementService.getLatestSettlementsDto(5);
        return ResponseEntity.ok(ApiResponse.success("최신 정산 목록 조회 성공", settlements));
    }
}