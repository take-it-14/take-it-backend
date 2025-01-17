package com.takeit.order.presentation.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.takeit.common.presentation.dto.CommonResponse;
import com.takeit.order.application.annotation.RequireRole;
import com.takeit.order.application.dto.order.OrderCreateResponse;
import com.takeit.order.application.dto.order.OrderResponse;
import com.takeit.order.application.dto.order.OrderDetailResponse;
import com.takeit.order.application.dto.order.OrderStatusUpdateResponse;
import com.takeit.order.application.dto.order.OrderListResponse;
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
	@RequireRole({"MASTER", "CUSTOMER"})
	public CommonResponse<OrderCreateResponse> createOrder(
		@RequestBody @Valid OrderCreateRequest request,
		@RequestAttribute(value = "X-UserId") Long userId,
		@RequestAttribute(value = "X-Username") String username
		){
		return CommonResponse.ofSuccess("주문 등록", orderService.createOrder(request.toServiceDto(), userId, username));
	}

	@GetMapping("/{orderId}")
	@RequireRole({"MASTER", "MANAGER", "CUSTOMER", "SELLER"})
	public CommonResponse<OrderDetailResponse> getOrderDetail(
		@PathVariable UUID orderId,
		@RequestAttribute(value = "X-UserId") Long userId,
		@RequestHeader(value = "X-Role", required = true) String role
	){
		return CommonResponse.ofSuccess("주문 상세 조회", orderService.getOrderDetail(orderId, userId, role));
	}

	@GetMapping
	@RequireRole({"MASTER", "MANAGER", "CUSTOMER"})
	public CommonResponse<PageResponse<OrderListResponse>> getOrders(
		Pageable pageable,
		@RequestParam(required = false) String status,
		@RequestParam Long searchUserId,
		@RequestAttribute(value = "X-UserId") Long userId,
		@RequestHeader(value = "X-Role", required = true) String role
	){
		return CommonResponse.ofSuccess("주문 목록 조회", PageResponse.of(orderService.getOrders(pageable, status, searchUserId, userId, role)));
	}

	@PatchMapping("/{orderId}")
	@RequireRole({"MASTER", "CUSTOMER"})
	public CommonResponse<OrderResponse> updateOrder(
		@PathVariable UUID orderId,
		@RequestBody @Valid OrderUpdateRequest request,
		@RequestAttribute(value = "X-UserId") Long userId,
		@RequestHeader(value = "X-Role", required = true) String role
	){
		return CommonResponse.ofSuccess("주문 정보 수정", orderService.updateOrder(orderId, request.toServiceDto(), userId, role));
	}

	@PatchMapping("/{orderId}/status")
	@RequireRole({"MASTER", "SELLER"})
	public CommonResponse<OrderStatusUpdateResponse> updateOrderStatus(
		@PathVariable UUID orderId,
		@RequestBody @Valid OrderStatusUpdateRequest request,
		@RequestAttribute(value = "X-UserId") Long userId,
		@RequestHeader(value = "X-Role", required = true) String role
	){
		return CommonResponse.ofSuccess("주문 상태 변경", orderService.updateOrderStatus(orderId, request.toServiceDto(), userId, role));
	}

	@PatchMapping("/{orderId}/cancel")
	@RequireRole({"MASTER", "CUSTOMER"})
	public CommonResponse<OrderStatusUpdateResponse> cancelOrder(
		@PathVariable UUID orderId,
		@RequestAttribute(value = "X-UserId") Long userId,
		@RequestHeader(value = "X-Role", required = true) String role
	){
		return CommonResponse.ofSuccess("주문 취소", orderService.cancelOrder(orderId, userId, role));
	}
}
