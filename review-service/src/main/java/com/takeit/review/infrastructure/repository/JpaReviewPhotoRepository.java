package com.takeit.review.infrastructure.repository;

import com.takeit.review.domain.entity.ReviewPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaReviewPhotoRepository extends JpaRepository<ReviewPhoto, Long> {
}
