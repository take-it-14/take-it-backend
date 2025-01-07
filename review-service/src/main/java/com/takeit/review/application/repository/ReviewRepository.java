package com.takeit.review.application.repository;

import com.querydsl.core.types.Predicate;
import com.takeit.review.application.dto.ReviewPageResponse;
import com.takeit.review.domain.entity.Review;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository {
    Review save(Review review);

    Optional<Review> findByUuid(UUID reviewId);

    Optional<Review> findReviewAndReviewPhotosByUuid(UUID reviewId);

    ReviewPageResponse findAll(Predicate predicate, Pageable pageable);
}
