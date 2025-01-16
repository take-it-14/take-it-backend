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

	@Value("${message.queues.product.cancel}")
	private String productQueue;

	private final OrderMessageProducer orderMessageProducer;

	@Transactional
	public OrderCreateResponse createOrder(OrderCreateDto request, Long userId) {

		ProductDto product = productClient.getProductByUuid(request.productId());

		checkStock(product.stock(), request.quantity().intValue());

		Long userCouponId = request.userCouponId() != null ?
			couponClient.validUserCouponAndGetUserCouponId(request.userCouponId(), userId) : null;

		OrderCacheDto orderCacheDto = OrderCacheDto.of(
			UUID.randomUUID(),
			userId,
			product.id(),
			userCouponId,
			request.quantity(),
			request.amount()
		);

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

	@Transactional
	public OrderStatusUpdateResponse cancelOrder(UUID orderId, Long userId, String role) {
		Order order = findOrderByUuid(orderId);

		if (isCustomer(role))
			validateUser(userId, order.getCustomerId());

		checkStatus(order.getStatus());

		order.cancel();

		return OrderStatusUpdateResponse.from(order);
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
		if (status == OrderStatus.CANCELLED || status == OrderStatus.DELIVERED)
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

	@Transactional
	public void cancelOrder(Long orderId) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
		checkCanCanceled(order.getStatus());
		order.cancel();
		orderMessageProducer.sendProductCancelRequest(order.getProductId(), order.getQuantity());
		if(order.getUserCouponId() != null)
			orderMessageProducer.sendUserCouponRequest(order.getUserCouponId());
	}

	private void checkCanCanceled(OrderStatus status) {
		if (status == OrderStatus.CANCELLED)
			throw new CustomException(ErrorCode.ORDER_ALREADY_CANCELED);
	}
}
