package com.takeit.coupon.application.service;

import com.querydsl.core.types.Predicate;
import com.takeit.common.exception.CustomException;
import com.takeit.common.utils.AccessValidator;
import com.takeit.coupon.application.dto.CreateUserCouponResponse;
import com.takeit.coupon.application.dto.UpdateUserCouponResponse;
import com.takeit.coupon.application.dto.UserCouponPageResponse;
import com.takeit.coupon.application.dto.UserCouponResponse;
import com.takeit.coupon.application.dto.category.CategoryDto;
import com.takeit.coupon.application.dto.user.UserDto;
import com.takeit.coupon.domain.entity.Coupon;
import com.takeit.coupon.domain.entity.UserCoupon;
import com.takeit.coupon.domain.repository.CouponRepository;
import com.takeit.coupon.domain.repository.UserCouponRepository;
import com.takeit.coupon.presentation.request.CreateUserCouponRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.takeit.common.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCouponService {
    private final UserCouponRepository userCouponRepository;
    private final CouponRepository couponRepository;

    private final UserService userService;
    private final CategoryService categoryService;

    @Transactional
    public CreateUserCouponResponse create(CreateUserCouponRequest request, String username) {
        UserDto couponUserDto = userService.getUser(request.username());

        Coupon coupon = couponRepository.findByUuidAndIsDeletedIsFalse(request.couponId()).orElseThrow(() -> new CustomException(COUPON_NOT_FOUND));

        CategoryDto categoryDto = categoryService.getCategory(coupon.getCategoryId());

        if(coupon.getEndDate().isBefore(LocalDateTime.now())) {
            throw new CustomException(COUPON_EXPIRED);
        }

        return CreateUserCouponResponse.of(userCouponRepository.save(UserCoupon.create(coupon, couponUserDto.id())), coupon.getName(), categoryDto.name());
    }

    @Transactional
    public void delete(UUID userCouponId, String username) {
        UserDto userDto = userService.getUser(username);

        UserCoupon coupon = userCouponRepository.findByUuidAndIsDeletedIsFalse(userCouponId).orElseThrow(() -> new CustomException(USER_COUPON_NOT_FOUND));

        if(checkRoleMasterOrManager(userDto.role())) {
            validationUserId(coupon, userDto.id());
        }

        coupon.delete(username);
    }

    @Transactional
    public UpdateUserCouponResponse used(UUID userCouponId, String username) {
        UserDto userDto = userService.getUser(username);

        UserCoupon coupon = userCouponRepository.findByUuidAndIsDeletedIsFalse(userCouponId).orElseThrow(() -> new CustomException(USER_COUPON_NOT_FOUND));

        if(checkRoleMasterOrManager(userDto.role())) {
            validationUserId(coupon, userDto.id());
        }

        if(LocalDateTime.now().isBefore(coupon.getStartDate()) || coupon.getEndDate().isBefore(LocalDateTime.now())) {
            throw new CustomException(USER_COUPON_INVALID_DATE_RANGE);
        }

        validCouponIsNotUsed(coupon);

        String couponName = coupon.getCoupon().getName();
        coupon.used();

        return UpdateUserCouponResponse.of(userCouponRepository.save(coupon), couponName);
    }

    @Transactional
    public UpdateUserCouponResponse cancel(UUID userCouponId, String username) {
        UserDto userDto = userService.getUser(username);

        UserCoupon coupon = userCouponRepository.findByUuidAndIsDeletedIsFalse(userCouponId).orElseThrow(() -> new CustomException(USER_COUPON_NOT_FOUND));

        if(checkRoleMasterOrManager(userDto.role())) {
            validationUserId(coupon, userDto.id());
        }

        if(!coupon.getIsUsed()) {
            throw new CustomException(USER_COUPON_NOT_USED);
        }

        String couponName = coupon.getCoupon().getName();
        coupon.cancel();

        return UpdateUserCouponResponse.of(userCouponRepository.save(coupon), couponName);
    }

    public Long validUserCouponAndGetUserCouponId(UUID userCouponId, Long userId) {
        UserCoupon coupon = userCouponRepository.findByUuidAndIsDeletedIsFalse(userCouponId).orElseThrow(() -> new CustomException(USER_COUPON_NOT_FOUND));

        validUserCouponUser(coupon, userId);

        validCouponIsNotUsed(coupon);

        return coupon.getId();
    }

    private void validUserCouponUser(UserCoupon coupon, Long userId) {
        if(!coupon.getUserId().equals(userId))  throw new CustomException(UNAUTHORIZED);
    }

    private void validCouponIsNotUsed(UserCoupon coupon) {
        if(coupon.getIsUsed()) throw new CustomException(USER_COUPON_ALREADY_USED);
    }

    public UUID getUserCouponUuid(Long userCouponId) {
        return userCouponRepository.findByIdAndIsDeletedIsFalse(userCouponId).orElseThrow(()-> new CustomException(COUPON_NOT_FOUND)).getUuid();
    }

    @Transactional(readOnly = true)
    public UserCouponResponse getUserCoupon(UUID userCouponId, String username) {
        UserDto userDto = userService.getUser(username);

        UserCoupon userCoupon = userCouponRepository.findByUuidAndUserIdAndFetchJoinCouponAndIsDeletedIsFalse(userCouponId).orElseThrow(() -> new CustomException(USER_COUPON_NOT_FOUND));

        if(checkRoleMasterOrManager(userDto.role())) {
            validationUserId(userCoupon, userDto.id());
        }

        return UserCouponResponse.from(userCoupon);

    }

    private void validationUserId(UserCoupon userCoupon, Long userId) {
        if(!userCoupon.getUserId().equals(userId)) {
            throw new CustomException(UNAUTHORIZED);
        }
    }

    public UserCouponPageResponse getUserCoupons(Predicate predicate, Pageable pageable, String username) {
        UserDto userDto = userService.getUser(username);

        return userCouponRepository.findAll(predicate, pageable, userDto);
    }

    private boolean checkRoleMasterOrManager(String role) {
        return !AccessValidator.isMaster(role) && !AccessValidator.isManager(role);
    }
}
