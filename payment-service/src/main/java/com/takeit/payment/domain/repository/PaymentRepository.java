package com.takeit.payment.domain.repository;

import com.takeit.payment.domain.entity.Payment;

import java.util.Optional;

public interface PaymentRepository {
    Payment save(Payment payment);

    Optional<Payment> getPaymentByOrderId(Long orderId);
}
