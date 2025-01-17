package com.takeit.order.application.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.takeit.order.application.dto.OrderDto;

import com.takeit.order.presentation.controller.OrderMessageProducer;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.takeit.common.exception.CustomException;
import com.takeit.common.exception.ErrorCode;

import static com.takeit.common.utils.AccessValidator.*;

import com.takeit.order.application.dto.order.OrderCacheDto;
import com.takeit.order.application.dto.order.OrderCreateDto;
import com.takeit.order.application.dto.order.OrderCreateResponse;
import com.takeit.order.application.dto.order.OrderResponse;
import com.takeit.order.application.dto.order.OrderDetailResponse;
import com.takeit.order.application.dto.order.OrderStatusUpdateDto;
import com.takeit.order.application.dto.order.OrderStatusUpdateResponse;
import com.takeit.order.application.dto.order.OrderUpdateDto;
import com.takeit.order.application.dto.order.OrderListResponse;
import com.takeit.order.application.dto.product.ProductDto;
import com.takeit.order.domain.entity.Order;
import com.takeit.order.domain.enums.OrderStatus;
import com.takeit.order.domain.repository.OrderRepository;
import com.takeit.order.infrastructure.client.CouponClient;
import com.takeit.order.infrastructure.client.ProductClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

	private final OrderRepository orderRepository;
	private final ProductClient productClient;
	private final CouponClient couponClient;
	private final RedisService redisService;

	@Value("${order.redis.ttl:300}") // 5분(ID 단독 저장용)
	private long orderIdRedisTtl;

	@Value("${order.redis.ttl:1800}") // 30분(주문 데이터 전체 저장용)
	private long orderRedisTtl;

	private final OrderMessageProducer orderMessageProducer;

	@Transactional
	public OrderCreateResponse createOrder(OrderCreateDto request, Long userId) {

		// 재고 확인 + 재고 차감
		productClient.occupyProduct(request.productId(), request.quantity().intValue());

		// 쿠폰 사용 가능 여부 확인 + 사용 처리
		Long userCouponId = request.userCouponId() != null ?
			couponClient.validUserCouponAndGetUserCouponId(request.userCouponId(), userId) : null;

		// redis에 캐싱할 정보 생성
		OrderCacheDto orderCacheDto = OrderCacheDto.of(
			UUID.randomUUID(),
			userId,
			request.productId(),
			userCouponId,
			request.quantity(),
			request.amount()
		);

		// TTL 안에 결제 완료되는지 확인하기 위함
		redisService.saveOrderId(orderCacheDto.uuid(), orderIdRedisTtl);
		redisService.saveOrder(orderCacheDto, orderRedisTtl);

		return OrderCreateResponse.from(orderCacheDto.uuid());
	}

	public OrderDetailResponse getOrderDetail(UUID orderId, Long userId, String role) {
		Order order = findOrderByUuid(orderId);

		ProductDto product = findProductByProductId(order.getProductId());

		if (isCustomer(role))
			validateUser(userId, order.getCustomerId());
		else if (isSeller(role))
			validateUser(userId, product.sellerId());

		UUID userCouponId = couponClient.getUserCouponUuid(order.getUserCouponId());

		return OrderDetailResponse.of(order, product.uuid(), userCouponId);
	}

	public Page<OrderListResponse> getOrders(Pageable pageable, String status, Long searchUserId, Long userId,
		String role) {
		Page<Order> orderPage;

		if (isCustomer(role))
			validateUser(userId, searchUserId);

		OrderStatus stat = OrderStatus.of(status);

		if (stat == null)
			orderPage = orderRepository.findByCustomerId(searchUserId, pageable);
		else
			orderPage = orderRepository.findByCustomerIdAndStatus(searchUserId, stat, pageable);

		List<Long> productIds = orderPage.getContent()
			.stream()
			.map(Order::getProductId)
			.toList();

		List<ProductDto> products = productClient.getAllProducts(productIds);

		Map<Long, UUID> productIdToUuidMap = products.stream()
			.collect(Collectors.toMap(ProductDto::id, ProductDto::uuid));

		return orderPage.map(
			order -> {
				return OrderListResponse.of(order, productIdToUuidMap.get(order.getProductId()));
			}
		);
	}

	@Transactional
	public OrderResponse updateOrder(UUID orderId, OrderUpdateDto request, Long userId, String role) {
		Order order = findOrderByUuid(orderId);

		ProductDto product = findProductByProductId(order.getProductId());

		checkStock(product.stock(), request.quantity().intValue());

		if (isCustomer(role))
			validateUser(userId, order.getCustomerId());

		checkStatus(order.getStatus());

		UUID userCouponId = couponClient.getUserCouponUuid(order.getUserCouponId());

		order.update(request.quantity(), request.amount());

		return OrderResponse.of(order, product.uuid(), userCouponId);
	}

	@Transactional
	public OrderStatusUpdateResponse updateOrderStatus(UUID orderId, OrderStatusUpdateDto request, Long userId,
		String role) {
		Order order = findOrderByUuid(orderId);

		ProductDto product = findProductByProductId(order.getProductId());

		if (isSeller(role))
			validateUser(userId, product.sellerId());

		OrderStatus status = OrderStatus.of(request.status());
		order.updateStatus(status);

		return OrderStatusUpdateResponse.from(order);
	}

	// 주문 && 결제까지 완료된 건에 한하여 사용자가 요청할때 수행하는 취소 로직
	@Transactional
	public OrderStatusUpdateResponse cancelOrder(UUID orderId, Long userId, String role) {
		Order order = findOrderByUuid(orderId);

		if (isCustomer(role))
			validateUser(userId, order.getCustomerId());

		checkStatus(order.getStatus());

		ProductDto product = findProductByProductId(order.getProductId());

		orderMessageProducer.sendProductCancelRequest(product.uuid(), order.getQuantity());

		// 결제 취소 로직 필요

		if(order.getUserCouponId() != null)
			orderMessageProducer.sendUserCouponCancelRequest(order.getUserCouponId());

		order.cancel();

		return OrderStatusUpdateResponse.from(order);
	}

	// 주문 || 결제가 성공하지 못해서 메시지 큐로 보상 트랜잭션을 수행하는 취소 로직
	@Transactional
	public void failOrder(String orderId) {
		OrderCacheDto orderCacheDto = redisService.getOrder(orderId);

		orderMessageProducer.sendProductCancelRequest(orderCacheDto.productId(), orderCacheDto.quantity());
		if(orderCacheDto.userCouponId() != null)
			orderMessageProducer.sendUserCouponCancelRequest(orderCacheDto.userCouponId());

		redisService.deleteOrder(orderId);
	}

	public Long findProductIdByOrderUuidAndUserId(UUID orderId, Long userId) {
		Order order = findOrderByUuid(orderId);
		validateUser(order.getCustomerId(), userId);

		return order.getProductId();
	}

	private Order findOrderByUuid(UUID uuid) {
		return orderRepository.findByUuid(uuid).orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
	}

	public OrderDto findOrderByOrderUuidAndUserId(UUID orderId, Long userId) {
		Order order = findOrderByUuid(orderId);
		validateUser(order.getCustomerId(), userId);

		return OrderDto.from(order);
	}

	private void checkStatus(OrderStatus status) {
		if (status != OrderStatus.COMPLETED)
			throw new CustomException(ErrorCode.ORDER_CANNOT_BE_MODIFIED);
	}

	private void validateUser(Long currentUserId, Long requiredUserId) {
		if (!currentUserId.equals(requiredUserId))
			throw new CustomException(ErrorCode.FORBIDDEN);
	}

	private void checkStock(Integer currentStock, Integer requiredStock) {
		if (currentStock < requiredStock)
			throw new CustomException(ErrorCode.INVALID_STOCK);
	}

	private ProductDto findProductByProductId(Long productId) {
		List<ProductDto> products = productClient.getAllProducts(List.of(productId));
		if (products.isEmpty())
			throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
		return products.get(0);
	}
}
