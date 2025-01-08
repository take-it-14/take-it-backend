package com.takeit.payment.domain.entity;

public enum PaymentStatus {
    COMPLETED("결제 완료"),
    CANCELED("결제 취소");

    private final String status;

    PaymentStatus(String status) {
        this.status = status;
    }
}
