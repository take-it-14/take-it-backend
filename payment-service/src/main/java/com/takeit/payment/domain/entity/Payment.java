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

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name="status")
    @Enumerated(value=EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "receipt", nullable = false, length = 500)
    private String receipt;

    public static Payment create(Long orderId, Long userId, Integer amount, String receipt) {
        return Payment.builder()
                .orderId(orderId)
                .userId(userId)
                .status(PaymentStatus.COMPLETED)
                .amount(amount)
                .receipt(receipt)
                .build();
    }

    public void cancel() {
        this.status = PaymentStatus.CANCELED;
    }
}

