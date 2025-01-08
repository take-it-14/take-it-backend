package com.takeit.coupon.infrastructure.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.takeit.coupon.application.dto.QUserCouponPageResponse_UserCouponPage_UserCoupon;
import com.takeit.coupon.application.dto.UserCouponPageResponse;
import com.takeit.coupon.domain.entity.Coupon;
import com.takeit.coupon.domain.entity.UserCoupon;
import com.takeit.coupon.domain.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
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
    public Optional<UserCoupon> findByUuidAndUserIdAndFetchJoinCouponAndIsDeletedIsFalse(UUID userCouponId) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(userCoupon.isDeleted.eq(false))
                .and(userCoupon.uuid.eq(userCouponId));

        JPAQuery<UserCoupon> jpaQuery =
                queryFactory.select(userCoupon)
                        .from(userCoupon)
                        .join(userCoupon.coupon, coupon).fetchJoin()
                        .where(builder);

        UserCoupon userCoupon = jpaQuery.fetchOne();

        return Optional.ofNullable(userCoupon);
    }


    public boolean existsByCouponAndIsDeletedIsFalse(Coupon coupon) {
        return jpaUserCouponRepository.existsByCouponAndIsDeletedIsFalse(coupon);
    }

    @Override
    public UserCouponPageResponse findAll(Predicate predicate, Pageable pageable, Long userId) {
        // todo : user 권한이 master, manager가 아닌경우 자신의 쿠폰만 조회하도록 조건 추가.
        BooleanBuilder builder = new BooleanBuilder(predicate);

        builder.and(coupon.isDeleted.eq(false));
        builder.and(userCoupon.userId.eq(userId));

        JPAQuery<UserCouponPageResponse.UserCouponPage.UserCoupon> query =
                queryFactory.select(new QUserCouponPageResponse_UserCouponPage_UserCoupon(
                        userCoupon.uuid,
                        coupon.name,
                        userCoupon.createdBy,
                        coupon.type,
                        coupon.discountValue,
                        coupon.minAmount,
                        coupon.categoryId,
                        userCoupon.startDate,
                        userCoupon.endDate
                ))
                .from(userCoupon)
                .leftJoin(userCoupon.coupon, coupon)
                .where(builder)
                .orderBy(buildOrderBy(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<UserCouponPageResponse.UserCouponPage.UserCoupon> coupons = query.fetch();

        Long total = queryFactory
                .select(userCoupon.count())
                .from(userCoupon)
                .where(builder)
                .fetchOne();

        Page<UserCouponPageResponse.UserCouponPage.UserCoupon> userCouponPage = new PageImpl<>(coupons, pageable, total != null ? total : 0);

        return UserCouponPageResponse.from(userCouponPage);
    }

    private OrderSpecifier<?>[] buildOrderBy(Sort sort) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        for (Sort.Order order : sort) {
            // 필드 타입에 따라 PathBuilder를 다르게 사용
            if (order.getProperty().equals("startDate")) {
                OrderSpecifier<?> orderSpecifier = order.isAscending() ? coupon.startDate.asc() :coupon.startDate.desc();
                orderSpecifiers.add(orderSpecifier);
            } else if (order.getProperty().equals("endDate")) {
                OrderSpecifier<?> orderSpecifier = order.isAscending() ? coupon.endDate.asc() : coupon.endDate.desc();
                orderSpecifiers.add(orderSpecifier);
            } else if (order.getProperty().equals("categoryId")) {
                OrderSpecifier<?> orderSpecifier = order.isAscending() ? coupon.categoryId.asc() :coupon.categoryId.desc();
                orderSpecifiers.add(orderSpecifier);
            } else if (order.getProperty().equals("name")) {
                OrderSpecifier<?> orderSpecifier = order.isAscending() ? coupon.name.asc() :coupon.name.desc();
                orderSpecifiers.add(orderSpecifier);
            } else if (order.getProperty().equals("type")) {
                OrderSpecifier<?> orderSpecifier = order.isAscending() ? coupon.type.asc() :coupon.type.desc();
                orderSpecifiers.add(orderSpecifier);
            } else if(order.getProperty().equals("discountValue")) {
                OrderSpecifier<?> orderSpecifier = order.isAscending() ? coupon.discountValue .asc() :coupon.discountValue.desc();
                orderSpecifiers.add(orderSpecifier);
            } else if(order.getProperty().equals("minAmount")) {
                OrderSpecifier<?> orderSpecifier = order.isAscending() ? coupon.minAmount .asc() :coupon.minAmount.desc();
                orderSpecifiers.add(orderSpecifier);
            } else if(order.getProperty().equals("id")){
                OrderSpecifier<?> orderSpecifier = order.isAscending() ? coupon.id.asc() : coupon.id.desc();
                orderSpecifiers.add(orderSpecifier);
            }
        }

        return orderSpecifiers.toArray(new OrderSpecifier[0]);
    }
}
