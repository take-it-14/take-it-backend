package com.takeit.review.infrastructure.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.takeit.review.application.dto.QReviewPageResponse_ReviewPage_Review;
import com.takeit.review.application.dto.ReviewPageResponse;
import com.takeit.review.application.repository.ReviewRepository;
import com.takeit.review.domain.entity.Review;
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

import static com.takeit.review.domain.entity.QReview.review;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {
    private final JpaReviewRepository jpaReviewRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Review save(Review review) {
        return jpaReviewRepository.save(review);
    }

    @Override
    public Optional<Review> findByUuid(UUID reviewId) {
        return jpaReviewRepository.findByUuidAndIsDeletedIsFalse(reviewId);
    }

    @Override
    public Optional<Review> findReviewAndReviewPhotosByUuid(UUID reviewId) {
        return jpaReviewRepository.findReviewAndReviewPhotosByUuid(reviewId);
    }

    @Override
    public ReviewPageResponse findAll(Predicate predicate, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(review.isDeleted.eq(false));

        JPAQuery<ReviewPageResponse.ReviewPage.Review> query =
                queryFactory.select(new QReviewPageResponse_ReviewPage_Review(
                        review.uuid,
                        review.stars,
                        review.comment
                ))
                .from(review)
                .where(builder)
                .orderBy(buildOrderBy(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<ReviewPageResponse.ReviewPage.Review> content = query.fetch();

        Long total = queryFactory
                .select(review.count())
                .from(review)
                .where(builder)
                .fetchOne();

        Page<ReviewPageResponse.ReviewPage.Review> page = new PageImpl<>(content, pageable, total != null ? total : 0);

        return ReviewPageResponse.from(page);
    }

    private OrderSpecifier<?>[] buildOrderBy(Sort sort) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        for (Sort.Order order : sort) {
            // 필드 타입에 따라 PathBuilder를 다르게 사용
            if (order.getProperty().equals("stars")) {
                OrderSpecifier<?> orderSpecifier = order.isAscending() ? review.stars.asc() :review.stars.desc();
                orderSpecifiers.add(orderSpecifier);
            } else if (order.getProperty().equals("productId")) {
                OrderSpecifier<?> orderSpecifier = order.isAscending() ? review.productId.asc() : review.productId.desc();
                orderSpecifiers.add(orderSpecifier);
            } else {
                OrderSpecifier<?> orderSpecifier = order.isAscending() ? review.id.asc() : review.id.desc();
                orderSpecifiers.add(orderSpecifier);
            }
        }

        return orderSpecifiers.toArray(new OrderSpecifier[0]);
    }
}
