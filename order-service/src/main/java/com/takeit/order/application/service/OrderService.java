package com.takeit.order.application.service;

import java.util.UUID;

import com.takeit.order.application.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.takeit.common.exception.CustomException;
import com.takeit.common.exception.ErrorCode;
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

		// TODO: coupon 검증 및 변환
		Long userCouponId = 1L;

		Order order = Order.create(
			1L, // TODO: 사용자 정보 받아오기
			productId,
			userCouponId,
			request.quantity(),
			request.amount()
		);
		return OrderResponse.of(orderRepository.save(order), request.productId(), request.userCouponId());
	}

	public OrderDetailResponse getOrderDetail(UUID orderId){
		Order order = findOrderByUuid(orderId);

		// TODO: 요청 유저의 정보인지 검증 필요

		// TODO: product-service에서 id->UUID 변환 필요
		UUID productId = UUID.randomUUID();

		// TODO: userCouponId 변환
		UUID userCouponId = UUID.randomUUID();

		return OrderDetailResponse.of(order, productId, userCouponId);
	}

	public Page<OrderListResponse> getOrders(Pageable pageable, String status, String username){
		Page<Order> orderPage;

		// TODO: username->userId 가져오는 로직 필요
		Long userId = 1L;

		OrderStatus stat = OrderStatus.of(status);
		if(stat==null) orderPage = orderRepository.findByCustomerId(userId, pageable);
		else orderPage = orderRepository.findByCustomerIdAndStatus(userId, stat, pageable);

		return orderPage.map(
			order -> {
				UUID productUuid = findProductUuidByProductId(order.getProductId());
				return OrderListResponse.of(order, productUuid);
			}
		);
	}

	@Transactional
	public OrderResponse updateOrder(UUID orderId, OrderUpdateDto request){
		Order order = findOrderByUuid(orderId);

		// TODO: 요청 유저의 정보인지 검증 필요

		// TODO: product-service에서 order.productId로 해당 상품 재고가 몇개 있는지 확인 + id->UUID 변환 필요
		UUID productId = findProductUuidByProductId(1L);

		checkStatus(order.getStatus());

		// TODO: userCouponId 변환
		UUID userCouponId = UUID.randomUUID();

		order.update(request.quantity(), request.amount());

		return OrderResponse.of(order, productId, userCouponId);
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

		checkStatus(order.getStatus());

		order.cancel();

		return OrderStatusUpdateResponse.from(order);
	}

	public Long findProductIdByOrderUuidAndUserId(UUID orderId, Long userId){
		Order order = findOrderByUuid(orderId);
		validateOrderUser(order, userId);

		return order.getProductId();
	}

	public OrderDto findOrderByOrderUuidAndUserId(UUID orderId, Long userId){
		Order order = findOrderByUuid(orderId);
		validateOrderUser(order, userId);

		return OrderDto.from(order);
	}

	private Order findOrderByUuid(UUID uuid){
		return orderRepository.findByUuid(uuid).orElseThrow(()-> new CustomException(ErrorCode.ORDER_NOT_FOUND));
	}

	private UUID findProductUuidByProductId(Long productId){
		// TODO: product-service 요청 필요
		return UUID.randomUUID();
	}

	private void checkStatus(OrderStatus status){
		if(status == OrderStatus.CANCELLED || status == OrderStatus.DELIVERED) throw new CustomException(ErrorCode.ORDER_CANNOT_BE_MODIFIED);
	}

	private void validateOrderUser(Order order, Long userId){
		if(!order.getCustomerId().equals(userId)) throw new CustomException(ErrorCode.FORBIDDEN);
	}
}
