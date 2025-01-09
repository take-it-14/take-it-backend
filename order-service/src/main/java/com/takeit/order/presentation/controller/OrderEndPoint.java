package com.takeit.order.presentation.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.takeit.order.application.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feign/v1/orders")
public class OrderEndPoint {

	private final OrderService orderService;

	@GetMapping("/{orderId}/productId")
	public Long findProductIdByOrderUuidAndUserId(
		@PathVariable("orderId") UUID orderId,
		@RequestParam("userId") Long userId
	) {
		return orderService.findProductIdByOrderUuidAndUserId(orderId, userId);
	}
}
