package com.takeit.review.infrastructure.repository;

import com.takeit.review.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaReviewRepository extends JpaRepository<Review, Long> {
}
