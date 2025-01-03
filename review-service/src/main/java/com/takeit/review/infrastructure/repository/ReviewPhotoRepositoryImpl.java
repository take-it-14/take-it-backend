package com.takeit.review.infrastructure.repository;

import com.takeit.review.application.repository.ReviewPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewPhotoRepositoryImpl implements ReviewPhotoRepository {
    public final JpaReviewPhotoRepository jpaReviewPhotoRepository;
}
