package com.takeit.payment.application.dto.payment;

import java.util.UUID;

public record VerifyTossPaymentDto(
        UUID orderId,
        String tossPaymentOrderId,
        String paymentKey,
        Integer amount
) {
    public static VerifyTossPaymentDto of(UUID orderId, String tossPaymentOrderId, String paymentKey, Integer amount) {
        return new VerifyTossPaymentDto(orderId, tossPaymentOrderId, paymentKey, amount);
    }
}
