package com.takeit.coupon.presentation.controller;

import com.takeit.coupon.application.service.UserCouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponMessageListener {
    private final UserCouponService userCouponService;

    @RabbitListener(queues = "${message.queues.coupon.cancel}")
    public void cancel(Long userCouponId) {
        userCouponService.cancel(userCouponId);
    }
}
