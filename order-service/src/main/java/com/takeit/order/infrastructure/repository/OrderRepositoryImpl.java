package com.takeit.order.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.takeit.order.domain.entity.Order;
import com.takeit.order.domain.repository.OrderRepository;

public interface OrderRepositoryImpl extends JpaRepository<Order, Long>, OrderRepository {
	Optional<Order> findByUuid(UUID uuid);
}
