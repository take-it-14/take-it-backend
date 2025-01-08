package com.takeit.payment.application.dto.payment;

public record CancelTossPaymentDto(
        Long orderId,
        String paymentKey,
        String cancelReason
) {
    public static CancelTossPaymentDto of(Long orderId, String paymentKey, String cancelReason) {
        return new CancelTossPaymentDto(orderId, paymentKey, cancelReason);
    }
}
