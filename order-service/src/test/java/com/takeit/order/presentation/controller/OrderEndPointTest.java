package com.takeit.order.presentation.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.UUID;

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

		mockMvc.perform(get("/feign/v1/orders/{orderId}/product", orderId)
			.param("userId", userId.toString()))
			.andExpect(status().isOk())
			.andExpect(content().string(productId.toString()));
	}
}