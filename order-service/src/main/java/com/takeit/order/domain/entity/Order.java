package com.takeit.order.domain.entity;

import java.util.UUID;

import com.takeit.common.domain.model.BaseEntity;
import com.takeit.order.domain.enums.OrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "p_order")
public class Order extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private UUID uuid;

	@Column(nullable = false)
	private Long customerId;

	@Column(nullable = false)
	private Long productId;

	@Column(nullable = false)
	private Long quantity;

	@Column(nullable = false)
	private Long amount;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	public static Order create(Long customerId, Long productId, Long quantity, Long amount) {
		return Order.builder()
			.uuid(UUID.randomUUID())
			.customerId(customerId)
			.productId(productId)
			.quantity(quantity)
			.amount(amount)
			.status(OrderStatus.PENDING)
			.build();
	}

	public void update(Long quantity, Long amount) {
		this.quantity = quantity;
		this.amount = amount;
	}
}
