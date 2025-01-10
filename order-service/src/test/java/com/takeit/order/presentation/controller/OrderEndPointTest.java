package com.takeit.order.presentation.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.UUID;

import com.takeit.order.application.dto.OrderDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.takeit.order.application.service.OrderService;

@WebMvcTest(controllers = OrderEndPoint.class)
class OrderEndPointTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrderService orderService;

	@Test
	void validRequest() throws Exception {
		UUID orderId = UUID.randomUUID();
		Long userId = 1L;
		Long productId = 2L;

		Mockito.when(orderService.findProductIdByOrderUuidAndUserId(orderId, userId)).thenReturn(productId);

		mockMvc.perform(get("/feign/v1/orders/{orderId}/productId", orderId)
			.param("userId", userId.toString()))
			.andExpect(status().isOk())
			.andExpect(content().string(productId.toString()));
	}

	@Test
	void validOrderRequest() throws Exception {
		UUID orderUuid = UUID.randomUUID();
		Long userId = 1L;
		Long productId = 2L;
		Long orderId = 1L;
		OrderDto orderDto = new OrderDto(orderId, productId);

		Mockito.when(orderService.findOrderByOrderUuidAndUserId(orderUuid, userId)).thenReturn(orderDto);

		mockMvc.perform(get("/feign/v1/orders/{orderId}", orderUuid)
						.param("userId", userId.toString()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.orderId").value(orderId))
				.andExpect(jsonPath("$.productId").value(productId));
	}
}