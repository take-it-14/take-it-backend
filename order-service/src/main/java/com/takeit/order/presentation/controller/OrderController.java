package com.takeit.order.presentation.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.takeit.common.presentation.dto.CommonResponse;
import com.takeit.order.application.dto.OrderCreateResponse;
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
		return CommonResponse.ofSuccess("주문이 등록되었습니다.", orderService.createOrder(request.toServiceDto()));
	}
}
