package com.takeit.order.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.takeit.order.domain.entity.Order;
import com.takeit.order.domain.repository.OrderRepository;

public interface OrderRepositoryImpl extends JpaRepository<Order, Long>, OrderRepository {
}
