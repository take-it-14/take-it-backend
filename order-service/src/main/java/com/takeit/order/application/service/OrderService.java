package com.takeit.order.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.takeit.order.application.dto.OrderCreateDto;
import com.takeit.order.application.dto.OrderCreateResponse;
import com.takeit.order.domain.entity.Order;
import com.takeit.order.domain.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

	private final OrderRepository orderRepository;

	@Transactional
	public OrderCreateResponse createOrder(OrderCreateDto request){

		// TODO: product-service에서 productId 유효성 검사 + id 받아오는 로직 구현 필요
		Long productId = 1L;
		// TODO: product-service에서 quantity 만큼 재고가 있는지 확인하는 로직 필요

		Order order = Order.create(
			1L, // TODO: 사용자 정보 받아오기
			productId,
			request.quantity(),
			request.amount()
		);
		return OrderCreateResponse.from(orderRepository.save(order), request.productId());
	}
}
