package com.takeit.order.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.takeit.order.domain.entity.Order;

public interface OrderRepositoryImpl extends JpaRepository<Order, Long> {
}
