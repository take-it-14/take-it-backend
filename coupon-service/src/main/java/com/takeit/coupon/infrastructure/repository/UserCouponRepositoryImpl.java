package com.takeit.coupon.infrastructure.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.takeit.coupon.domain.entity.Coupon;
import com.takeit.coupon.domain.entity.UserCoupon;
import com.takeit.coupon.domain.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static com.takeit.coupon.domain.entity.QCoupon.coupon;
import static com.takeit.coupon.domain.entity.QUserCoupon.userCoupon;

@Repository
@RequiredArgsConstructor
public class UserCouponRepositoryImpl implements UserCouponRepository {
    private final JpaUserCouponRepository jpaUserCouponRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public UserCoupon save(UserCoupon userCoupon) {
        return jpaUserCouponRepository.save(userCoupon);
    }

    @Override
    public Optional<UserCoupon> findByUuidAndIsDeletedIsFalse(UUID userCouponId) {
        return jpaUserCouponRepository.findByUuidAndIsDeletedIsFalse(userCouponId);
    }

    @Override
    public boolean existsByCouponAndIsDeletedIsFalse(Coupon coupon) {
        return jpaUserCouponRepository.existsByCouponAndIsDeletedIsFalse(coupon);
    }

    @Override
    public Optional<UserCoupon> getUserCoupon(UUID userCouponId, Long userId) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(userCoupon.isDeleted.eq(false))
                .and(userCoupon.userId.eq(userId))
                .and(userCoupon.uuid.eq(userCouponId));

        JPAQuery<UserCoupon> jpaQuery =
                queryFactory.select(userCoupon)
                        .from(userCoupon)
                        .join(userCoupon.coupon, coupon).fetchJoin()
                        .where(builder);

        UserCoupon userCoupon = jpaQuery.fetchOne();

        return Optional.ofNullable(userCoupon);
    }
}
