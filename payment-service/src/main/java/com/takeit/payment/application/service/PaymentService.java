package com.takeit.payment.application.service;

import com.takeit.common.exception.CustomException;
import com.takeit.common.exception.ErrorCode;
import com.takeit.payment.application.dto.VerifyTossPaymentDto;
import com.takeit.payment.domain.entity.Payment;
import com.takeit.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @Transactional
    public void verifyTossPayment(VerifyTossPaymentDto request, String username) {
        // TODO: username으로 User 조회 + 권한 체크
        Long userId = 1L;
        // TODO: orderUUID로 orderId 가져오기
        Long orderId = 1L;

        // TODO: pg사에 결제 확인
        boolean isPaymentSuccess = true;
        String receipt = "영수증";

        if(isPaymentSuccess) {
            paymentRepository.save(Payment.create(orderId, userId, request.amount(), receipt));
        } else {
            throw new CustomException(ErrorCode.WRONG_PAYMENT_INFO);
        }

    }
}
