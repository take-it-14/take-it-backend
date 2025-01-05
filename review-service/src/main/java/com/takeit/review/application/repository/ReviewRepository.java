package com.takeit.review.application.repository;

import com.takeit.review.domain.entity.Review;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository {
    Review save(Review review);

    Optional<Review> findByUuid(UUID reviewId);
}
