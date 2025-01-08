package com.takeit.payment.presentation.request;

import com.takeit.payment.application.dto.payment.CancelTossPaymentDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CancelTossPaymentRequest(
        @NotNull
        Long orderId,

        @NotNull
        @Size(max = 200)
        String paymentKey,

        @NotNull
        @Size(max = 200)
        String cancelReason

) {
    public CancelTossPaymentDto toDto() {
        return CancelTossPaymentDto.of(this.orderId, this.paymentKey, this.cancelReason);
    }
}

