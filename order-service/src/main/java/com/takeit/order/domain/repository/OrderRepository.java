package com.takeit.order.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.takeit.order.domain.entity.Order;

@Repository
public interface OrderRepository {
	Order save(Order order);
	Optional<Order> findByUuid(UUID uuid);
}
