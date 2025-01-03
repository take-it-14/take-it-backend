package com.takeit.order.presentation.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.takeit.common.presentation.dto.CommonResponse;
import com.takeit.order.application.dto.OrderResponse;
import com.takeit.order.application.dto.OrderDetailResponse;
import com.takeit.order.application.dto.OrderStatusUpdateResponse;
import com.takeit.order.application.dto.PageResponse;
import com.takeit.order.application.service.OrderService;
import com.takeit.order.presentation.request.OrderCreateRequest;
import com.takeit.order.presentation.request.OrderStatusUpdateRequest;
import com.takeit.order.presentation.request.OrderUpdateRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	public CommonResponse<OrderResponse> createOrder(
		@RequestBody @Valid OrderCreateRequest request
	){
		return CommonResponse.ofSuccess("주문 등록", orderService.createOrder(request.toServiceDto()));
	}

	@GetMapping("/{orderId}")
	public CommonResponse<OrderDetailResponse> getOrderDetail(
		@PathVariable UUID orderId
	){
		return CommonResponse.ofSuccess("주문 상세 조회", orderService.getOrderDetail(orderId));
	}

	@GetMapping
	public CommonResponse<PageResponse<OrderDetailResponse>> getOrders(
		Pageable pageable,
		@RequestParam(required = false) String status
		//@RequestHeader(value = "X-Username") String username
	){
		String username = "aaaaaa";
		return CommonResponse.ofSuccess("주문 목록 조회", PageResponse.of(orderService.getOrders(pageable, status, username)));
	}

	@PatchMapping("/{orderId}")
	public CommonResponse<OrderResponse> updateOrder(
		@PathVariable UUID orderId,
		@RequestBody @Valid OrderUpdateRequest request
	){
		return CommonResponse.ofSuccess("주문 정보 수정", orderService.updateOrder(orderId, request.toServiceDto()));
	}

	@PatchMapping("/{orderId}/status")
	public CommonResponse<OrderStatusUpdateResponse> updateOrderStatus(
		@PathVariable UUID orderId,
		@RequestBody @Valid OrderStatusUpdateRequest request
	){
		return CommonResponse.ofSuccess("주문 상태 변경", orderService.updateOrderStatus(orderId, request.toServiceDto()));
	}

	@PatchMapping("/{orderId}/cancel")
	public CommonResponse<OrderStatusUpdateResponse> cancelOrder(
		@PathVariable UUID orderId
	){
		return CommonResponse.ofSuccess("주문 취소", orderService.cancelOrder(orderId));
	}
}
