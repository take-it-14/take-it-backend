package com.takeit.order.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.takeit.common.exception.CustomException;
import com.takeit.common.exception.ErrorCode;
import com.takeit.order.application.dto.OrderCreateDto;
import com.takeit.order.application.dto.OrderResponse;
import com.takeit.order.application.dto.OrderDetailResponse;
import com.takeit.order.application.dto.OrderStatusUpdateDto;
import com.takeit.order.application.dto.OrderStatusUpdateResponse;
import com.takeit.order.application.dto.OrderUpdateDto;
import com.takeit.order.domain.entity.Order;
import com.takeit.order.domain.enums.OrderStatus;
import com.takeit.order.domain.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

	private final OrderRepository orderRepository;

	@Transactional
	public OrderResponse createOrder(OrderCreateDto request){

		// TODO: product-service에서 productId 유효성 검사 + id 받아오는 로직 구현 필요
		Long productId = 1L;
		// TODO: product-service에서 quantity 만큼 재고가 있는지 확인하는 로직 필요

		Order order = Order.create(
			1L, // TODO: 사용자 정보 받아오기
			productId,
			request.quantity(),
			request.amount()
		);
		return OrderResponse.from(orderRepository.save(order), request.productId());
	}

	public OrderDetailResponse getOrderDetail(UUID orderId){
		Order order = findOrderByUuid(orderId);

		// TODO: 요청 유저의 정보인지 검증 필요

		// TODO: product-service에서 id->UUID 변환 필요
		UUID productId = UUID.randomUUID();

		return OrderDetailResponse.from(order, productId);
	}

	public Page<OrderDetailResponse> getOrders(Pageable pageable, String status, String username){
		Page<Order> orderPage;

		// TODO: username->userId 가져오는 로직 필요
		Long userId = 1L;

		OrderStatus stat = OrderStatus.of(status);
		if(stat==null) orderPage = orderRepository.findByCustomerId(userId, pageable);
		else orderPage = orderRepository.findByCustomerIdAndStatus(userId, stat, pageable);

		return orderPage.map(
			order -> {
				UUID productUuid = findProductUuidByProductId(order.getProductId());
				return OrderDetailResponse.from(order, productUuid);
			}
		);
	}

	@Transactional
	public OrderResponse updateOrder(UUID orderId, OrderUpdateDto request){
		Order order = findOrderByUuid(orderId);

		// TODO: 요청 유저의 정보인지 검증 필요

		// TODO: product-service에서 order.productId로 해당 상품 재고가 몇개 있는지 확인 + id->UUID 변환 필요
		UUID productId = findProductUuidByProductId(1L);

		if(order.getStatus()==OrderStatus.CANCELLED || order.getStatus()==OrderStatus.DELIVERED) throw new CustomException(ErrorCode.ORDER_CANNOT_BE_MODIFIED);

		order.update(request.quantity(), request.amount());

		return OrderResponse.from(order, productId);
	}

	@Transactional
	public OrderStatusUpdateResponse updateOrderStatus(UUID orderId, OrderStatusUpdateDto request){
		Order order = findOrderByUuid(orderId);

		// TODO: 요청 유저의 정보인지 검증 필요
		OrderStatus status = OrderStatus.of(request.status());
		order.updateStatus(status);

		return OrderStatusUpdateResponse.from(order);
	}

	@Transactional
	public OrderStatusUpdateResponse cancelOrder(UUID orderId){
		Order order = findOrderByUuid(orderId);

		// TODO: 요청 유저의 정보인지 검증 필요

		if(order.getStatus()==OrderStatus.CANCELLED || order.getStatus()==OrderStatus.DELIVERED) throw new CustomException(ErrorCode.ORDER_CANNOT_BE_MODIFIED);

		order.cancel();

		return OrderStatusUpdateResponse.from(order);
	}


	private Order findOrderByUuid(UUID uuid){
		return orderRepository.findByUuid(uuid).orElseThrow(()-> new CustomException(ErrorCode.ORDER_NOT_FOUND));
	}

	private UUID findProductUuidByProductId(Long productId){
		// TODO: product-service 요청 필요
		return UUID.randomUUID();
	}
}
