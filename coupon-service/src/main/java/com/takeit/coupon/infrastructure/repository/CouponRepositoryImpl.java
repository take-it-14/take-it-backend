package com.takeit.coupon.infrastructure.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.takeit.coupon.application.dto.CouponPageResponse;
import com.takeit.coupon.application.dto.QCouponPageResponse_CouponPage_Coupon;
import com.takeit.coupon.domain.entity.Coupon;
import com.takeit.coupon.domain.entity.QCoupon;
import com.takeit.coupon.domain.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
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

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {
    private final JpaCouponRepository jpaCouponRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Coupon save(Coupon coupon) {
        return jpaCouponRepository.save(coupon);
    }

    @Override
    public Optional<Coupon> findByUuidAndIsDeletedIsFalse(UUID couponId) {
        return jpaCouponRepository.findByUuidAndIsDeletedIsFalse(couponId);
    }

    @Override
    public CouponPageResponse findAll(Predicate predicate, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder(predicate);

        builder.and(coupon.isDeleted.eq(false));

        JPAQuery<CouponPageResponse.CouponPage.Coupon> query =
                queryFactory.select(new QCouponPageResponse_CouponPage_Coupon(
                        coupon.uuid,
                        coupon.name,
                        coupon.type,
                        coupon.discountValue,
                        coupon.minAmount,
                        coupon.categoryId,
                        coupon.startDate,
                        coupon.endDate,
                        coupon.expirationDate
                ))
                .from(coupon)
                .where(builder)
                .orderBy(buildOrderBy(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<CouponPageResponse.CouponPage.Coupon> content = query.fetch();

        Long total = queryFactory
                .select(coupon.count())
                .from(coupon)
                .where(builder)
                .fetchOne();

        Page<CouponPageResponse.CouponPage.Coupon> couponPage = new PageImpl<>(content, pageable, total != null ? total : 0);

        return CouponPageResponse.from(couponPage);

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
            } else if(order.getProperty().equals("expirationDate")) {
                OrderSpecifier<?> orderSpecifier = order.isAscending() ? coupon.expirationDate .asc() :coupon.expirationDate.desc();
                orderSpecifiers.add(orderSpecifier);
            } else if(order.getProperty().equals("id")){
                OrderSpecifier<?> orderSpecifier = order.isAscending() ? coupon.id.asc() : coupon.id.desc();
                orderSpecifiers.add(orderSpecifier);
            }
        }

        return orderSpecifiers.toArray(new OrderSpecifier[0]);
    }
}
