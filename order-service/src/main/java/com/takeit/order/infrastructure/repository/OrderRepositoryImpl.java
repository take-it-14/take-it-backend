package com.takeit.order.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.takeit.order.domain.entity.Order;
import com.takeit.order.domain.enums.OrderStatus;
import com.takeit.order.domain.repository.OrderRepository;

public interface OrderRepositoryImpl extends JpaRepository<Order, Long>, OrderRepository {
	Optional<Order> findByUuid(UUID uuid);
	Page<Order> findByCustomerId(Long customerId, Pageable pageable);
	Page<Order> findByCustomerIdAndStatus(Long customerId, OrderStatus orderStatus, Pageable pageable);
}
