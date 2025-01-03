package com.takeit.order.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.takeit.order.domain.entity.Order;
import com.takeit.order.domain.enums.OrderStatus;

@Repository
public interface OrderRepository {
	Order save(Order order);
	Optional<Order> findByUuid(UUID uuid);
	Page<Order> findByCustomerId(Long customerId, Pageable pageable);
	Page<Order> findByCustomerIdAndStatus(Long customerId, OrderStatus orderStatus, Pageable pageable);
}
