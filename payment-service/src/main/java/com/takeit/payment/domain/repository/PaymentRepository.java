package com.takeit.payment.domain.repository;

import com.takeit.payment.domain.entity.Payment;

public interface PaymentRepository {
    Payment save(Payment payment);
}
