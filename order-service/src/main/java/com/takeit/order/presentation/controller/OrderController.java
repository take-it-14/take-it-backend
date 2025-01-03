package com.takeit.order.presentation.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.takeit.common.presentation.dto.CommonResponse;
import com.takeit.order.application.dto.OrderCreateResponse;
import com.takeit.order.application.dto.OrderDetailResponse;
import com.takeit.order.application.service.OrderService;
import com.takeit.order.presentation.request.OrderCreateRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	public CommonResponse<OrderCreateResponse> createOrder(
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
}
