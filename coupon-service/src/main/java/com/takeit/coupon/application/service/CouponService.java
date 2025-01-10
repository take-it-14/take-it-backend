package com.takeit.coupon.application.service;

import com.querydsl.core.types.Predicate;
import com.takeit.common.exception.CustomException;
import com.takeit.common.utils.AccessValidator;
import com.takeit.coupon.application.dto.CouponPageResponse;
import com.takeit.coupon.application.dto.CouponResponse;
import com.takeit.coupon.application.dto.CreateCouponResponse;
import com.takeit.coupon.application.dto.UpdateCouponResponse;
import com.takeit.coupon.application.dto.category.CategoryDto;
import com.takeit.coupon.application.dto.user.UserDto;
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
@Transactional(readOnly = true)
public class CouponService {
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    @Transactional
    public CreateCouponResponse createCoupon(CreateCouponRequest request, String username) {
        UserDto userDto = userService.getUser(username);

        validRole(userDto.role());

        CategoryDto categoryDto = null;

        if(request.categoryId() != null)
            categoryDto = categoryService.getCategory(request.categoryId());

        if(request.type().equals(CouponType.PERCENTAGE)) {
            validationPercentageValue(request.discountValue());
        }

        validationDateRange(request.startDate(), request.endDate());

        return CreateCouponResponse.of(
                couponRepository.save(Coupon.create(request,categoryDto == null ? null : categoryDto.id())),
                categoryDto == null ? null : categoryDto.name()
        );
    }



    @Transactional
    public UpdateCouponResponse updateCoupon(UUID couponId, UpdateCouponRequest request, String username) {
        UserDto userDto = userService.getUser(username);

        validRole(userDto.role());

        CategoryDto categoryDto = null;

        if(request.categoryId() != null)
            categoryDto = categoryService.getCategory(request.categoryId());

        Coupon coupon = couponRepository.findByUuidAndIsDeletedIsFalse(couponId).orElseThrow(() -> new CustomException(COUPON_NOT_FOUND));

        if(userCouponRepository.existsByCouponAndIsDeletedIsFalse(coupon)) {
            throw new CustomException(COUPON_UPDATED_FAIL_CAUSE_EXIST_USER_COUPON);
        }

        if(request.type().equals(CouponType.PERCENTAGE)) {
            validationPercentageValue(request.discountValue());
        }

        if(request.startDate() != null || request.endDate() != null) {
            validationDateRange(request.startDate() == null ? coupon.getStartDate() : request.startDate(),
                    request.endDate() == null ? coupon.getEndDate() : request.endDate());
        }

        coupon.update(request, categoryDto == null ? null : categoryDto.id());

        return UpdateCouponResponse.of(couponRepository.save(coupon), categoryDto == null ? null : categoryDto.name());

    }

    @Transactional
    public void deleteCoupon(UUID couponId, String username) {
        UserDto userDto = userService.getUser(username);

        validRole(userDto.role());

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

        if(endDate.isBefore(LocalDateTime.now())) {
            throw new CustomException(COUPON_END_DATE_IN_PAST);
        }

        if(endDate.isBefore(startDate) || endDate.isEqual(startDate)) {
            throw new CustomException(COUPON_END_DATE_MUST_BE_AFTER_START_DATE);
        }
    }


    public CouponResponse getCoupon(String username, UUID couponId) {
        UserDto userDto = userService.getUser(username);

        Coupon coupon = couponRepository.findByUuidAndIsDeletedIsFalse(couponId).orElseThrow(() -> new CustomException(COUPON_NOT_FOUND));

        validRole(userDto.role());

        String categoryName = null;

        if(coupon.getCategoryId() != null)
            categoryName = categoryService.getCategory(coupon.getCategoryId()).name();

        return CouponResponse.of(coupon, categoryName);
    }

    public CouponPageResponse getCoupons(String username, Predicate predicate, Pageable pageable) {
        UserDto userDto = userService.getUser(username);

        validRole(userDto.role());

        return couponRepository.findAll(predicate, pageable);
    }

    private void validRole(String role) {
        if(!AccessValidator.isManager(role) && !AccessValidator.isMaster(role)) {
            throw new CustomException(UNAUTHORIZED);
        }
    }
}
