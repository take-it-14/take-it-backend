package com.takeit.payment.presentation.controller;

import com.takeit.common.presentation.dto.CommonResponse;
import com.takeit.payment.application.service.PaymentService;
import com.takeit.payment.presentation.request.CancelTossPaymentRequest;
import com.takeit.payment.presentation.request.VerifyTossPaymentRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/toss/verify")
    public CommonResponse<?> verifyTossPayment(@Valid @RequestBody VerifyTossPaymentRequest request,
                                               @RequestHeader(value = "X-Username") String username) {
        return CommonResponse.ofSuccess(paymentService.verifyTossPayment(request.toDto(), username), null);
    }

    @PatchMapping("/toss/cancel")
    public CommonResponse<?> cancelTossPayment(@Valid @RequestBody CancelTossPaymentRequest request,
                                               @RequestHeader(value = "X-Username") String username) {
        paymentService.cancelTossPayment(request.toDto(), username);
        return CommonResponse.ofSuccess("결제 취소", null);
    }
}
