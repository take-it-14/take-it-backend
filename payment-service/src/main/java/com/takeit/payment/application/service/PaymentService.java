package com.takeit.payment.application.service;

import com.takeit.common.exception.CustomException;
import com.takeit.common.exception.ErrorCode;
import com.takeit.payment.application.dto.payment.CancelTossPaymentDto;
import com.takeit.payment.application.dto.payment.VerifyTossPaymentDto;
import com.takeit.payment.domain.entity.Payment;
import com.takeit.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final RabbitTemplate rabbitTemplate;

    @Value("${message.queue.order.cancel}")
    private String queueOrder;

    @Transactional
    public void verifyTossPayment(VerifyTossPaymentDto request, String username) {
        // TODO: username으로 User 조회 + 권한 체크
        Long userId = 1L;
        // TODO: orderUUID로 orderId 가져오기
        Long orderId = 1L;

        // TODO: pg사에 결제 확인(실제로는 try catch를 통해 진행해야함)
        boolean isPaymentSuccess = false;
        String receipt = "영수증";

        if(isPaymentSuccess) {
            paymentRepository.save(Payment.create(orderId, userId, request.amount(), receipt));
        } else {
            rabbitTemplate.convertAndSend(queueOrder, orderId);
//            throw new CustomException(ErrorCode.WRONG_PAYMENT);
        }

    }

    @Transactional
    public void cancelTossPayment(CancelTossPaymentDto request, String username) {
        // TODO: username으로 권한 체크(Master만 가능)

        Payment payment = paymentRepository.getPaymentByOrderId(request.orderId())
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));

        // TODO: pg사에 취소 요청(실제로는 try catch를 통해 진행해야함)
        boolean isPaymentCanceled = true;

        if(isPaymentCanceled) {
            payment.cancel();
            rabbitTemplate.convertAndSend(queueOrder, request.orderId());
        } else {
            throw new CustomException(ErrorCode.PAYMENT_CANCEL_FAIL);
        }

    }
}
