package com.takeit.coupon.application.service;

import com.querydsl.core.types.Predicate;
import com.takeit.common.exception.CustomException;
import com.takeit.coupon.application.dto.CouponPageResponse;
import com.takeit.coupon.application.dto.CouponResponse;
import com.takeit.coupon.application.dto.CreateCouponResponse;
import com.takeit.coupon.application.dto.UpdateCouponResponse;
import com.takeit.coupon.domain.entity.Coupon;
import com.takeit.coupon.domain.repository.CouponRepository;
import com.takeit.coupon.domain.repository.UserCouponRepository;
import com.takeit.coupon.domain.type.CouponType;
import com.takeit.coupon.presentation.request.CreateCouponRequest;
import com.takeit.coupon.presentation.request.UpdateCouponRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.takeit.common.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    @Transactional
    public CreateCouponResponse createCoupon(CreateCouponRequest request, String username) {
        // todo : user 권한 체크 (master, manager), 카테고리 객체 가져오기
        Long categoryId = 1L;
        String categoryName = "의류";

        if(request.type().equals(CouponType.PERCENTAGE)) {
            validationPercentageValue(request.discountValue());
        }

        validationDateRange(request.startDate(), request.endDate());

        return CreateCouponResponse.of(couponRepository.save(Coupon.create(request, categoryId)), categoryName);

    }

    @Transactional
    public UpdateCouponResponse updateCoupon(UUID couponId, UpdateCouponRequest request, String username) {
        // todo : user 권한 체크 (master, manager), 카테고리 객체 가져오기
        Long categoryId = 1L;
        String categoryName = "가방";

        Coupon coupon = couponRepository.findByUuidAndIsDeletedIsFalse(couponId).orElseThrow(() -> new CustomException(COUPON_NOT_FOUND));

        if(request.type().equals(CouponType.PERCENTAGE)) {
            validationPercentageValue(request.discountValue());
        }

        if(request.startDate() != null || request.endDate() != null) {
            validationDateRange(request.startDate() == null ? coupon.getStartDate() : request.startDate(),
                    request.endDate() == null ? coupon.getEndDate() : request.endDate());
        }

        coupon.update(request, categoryId);

        return UpdateCouponResponse.of(couponRepository.save(coupon), categoryName);

    }

    @Transactional
    public void deleteCoupon(UUID couponId, String username) {
        // todo : user 권한 체크 (master, manager)
        Coupon coupon = couponRepository.findByUuidAndIsDeletedIsFalse(couponId).orElseThrow(() -> new CustomException(COUPON_NOT_FOUND));

        coupon.delete(username);

        couponRepository.save(coupon);
    }

    private void validationPercentageValue(int value) {
        if(value > 100) throw new CustomException(INVALID_DISCOUNT_PERCENTAGE_VALUE);
    }

    private void validationDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if(startDate.isBefore(LocalDate.now().atStartOfDay())) {
            throw new CustomException(COUPON_START_DATE_IN_PAST);
        }

        if(endDate.isBefore(startDate) || endDate.isEqual(startDate)) {
            throw new CustomException(COUPON_END_DATE_MUST_BE_AFTER_START_DATE);
        }
    }

    @Transactional(readOnly = true)
    public CouponResponse getCoupon(String username, UUID couponId) {
        // todo : user 권한 체크 (master, manager), category name 받아오기
        String category = "의류";
        return CouponResponse.of(couponRepository.findByUuidAndIsDeletedIsFalse(couponId).orElseThrow(() -> new CustomException(COUPON_NOT_FOUND)), category);
    }

    @Transactional(readOnly = true)
    public CouponPageResponse getCoupons(String username, Predicate predicate, Pageable pageable) {
        // todo : user 권한 체크 (master, manager)

        return couponRepository.findAll(predicate, pageable);
    }
}
