package com.takeit.coupon.application.service;

import com.takeit.common.exception.CustomException;
import com.takeit.coupon.application.dto.CreateCouponResponse;
import com.takeit.coupon.domain.entity.Coupon;
import com.takeit.coupon.domain.repository.CouponRepository;
import com.takeit.coupon.domain.repository.UserCouponRepository;
import com.takeit.coupon.domain.type.CouponType;
import com.takeit.coupon.presentation.request.CreateCouponRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.takeit.common.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    public CreateCouponResponse createCoupon(CreateCouponRequest request, String username) {
        // todo : user 권한 체크 (master, manager), 카테고리 객체 가져오기
        Long categoryId = 1L;
        String categoryName = "의류";

        if(request.type().equals(CouponType.PERCENTAGE) && request.discountValue() > 100) {
            throw new CustomException(INVALID_DISCOUNT_PERCENTAGE_VALUE);
        }

        if(request.startDate().isBefore(LocalDate.now().atStartOfDay())) {
            throw new CustomException(COUPON_START_DATE_IN_PAST);
        }

        if(request.endDate().isBefore(request.startDate()) || request.endDate().isEqual(request.startDate())) {
            throw new CustomException(COUPON_END_DATE_MUST_BE_AFTER_START_DATE);
        }

        return CreateCouponResponse.of(couponRepository.save(Coupon.create(request, categoryId)), categoryName);

    }
}
