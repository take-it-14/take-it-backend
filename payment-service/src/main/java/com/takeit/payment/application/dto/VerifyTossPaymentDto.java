package com.takeit.payment.application.dto;

public record VerifyTossPaymentDto(
        Long orderId,
        String tossPaymentOrderId,
        String paymentKey,
        Integer amount
) {
    public static VerifyTossPaymentDto of(Long orderId, String tossPaymentOrderId, String paymentKey, Integer amount) {
        return new VerifyTossPaymentDto(orderId, tossPaymentOrderId, paymentKey, amount);
    }
}
