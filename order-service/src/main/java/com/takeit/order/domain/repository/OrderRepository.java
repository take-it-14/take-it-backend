package com.takeit.order.domain.repository;

import org.springframework.stereotype.Repository;

import com.takeit.order.domain.entity.Order;

@Repository
public interface OrderRepository {
	Order save(Order order);
}
