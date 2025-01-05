package com.takeit.review.infrastructure.repository;

import com.takeit.review.application.repository.ReviewRepository;
import com.takeit.review.domain.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {
    private final JpaReviewRepository jpaReviewRepository;

    @Override
    public Review save(Review review) {
        return jpaReviewRepository.save(review);
    }

    @Override
    public Optional<Review> findByUuid(UUID reviewId) {
        return jpaReviewRepository.findByUuidAndIsDeletedIsFalse(reviewId);
    }
}
