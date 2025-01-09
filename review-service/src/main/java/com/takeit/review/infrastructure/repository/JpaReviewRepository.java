package com.takeit.review.infrastructure.repository;

import com.takeit.review.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface JpaReviewRepository extends JpaRepository<Review, Long> {
    @Query("select r from Review r where r.uuid = :reviewId and r.isDeleted = false")
    Optional<Review> findByUuidAndIsDeletedIsFalse(@Param("reviewId") UUID reviewId);

    @Query("select distinct r from Review r " +
            "left join fetch r.photoList p " +
            "where r.isDeleted = false and (p.isDeleted = false or p is null) and r.uuid = :reviewId")
    Optional<Review> findReviewAndReviewPhotosByUuid(@Param("reviewId") UUID reviewId);
}
