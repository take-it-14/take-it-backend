package com.takeit.payment.domain.entity;

import com.takeit.common.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@Entity
@Table(name = "p_payment")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @Builder.Default
    private UUID uuid = UUID.randomUUID();

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "payment_key", length = 200, nullable = false)
    private String paymentKey;

    @Column(name = "amount", nullable = false)
    private Integer amount;
}

