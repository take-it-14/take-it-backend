package com.takeit.payment.presentation.request;

import com.takeit.payment.application.dto.VerifyTossPaymentDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record VerifyTossPaymentRequest(
        @NotNull
        Long orderId,

        @NotNull
        @Size(min = 6, max = 64)
        String tossPaymentOrderId,

        @NotNull
        @Size(max = 200)
        String paymentKey,

        @NotNull
        Integer amount
) {
    public VerifyTossPaymentDto toDto() {
        return VerifyTossPaymentDto.of(this.orderId, this.tossPaymentOrderId, this.paymentKey, this.amount);
    }
}
