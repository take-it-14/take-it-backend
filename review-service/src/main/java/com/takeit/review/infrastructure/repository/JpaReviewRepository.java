package com.takeit.review.infrastructure.repository;

import com.takeit.review.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface JpaReviewRepository extends JpaRepository<Review, Long> {
    @Query("select r from Review r where r.uuid = :reviewId and r.isDeleted = false")
    Optional<Review> findByUuidAndIsDeletedIsFalse(UUID reviewId);
}
