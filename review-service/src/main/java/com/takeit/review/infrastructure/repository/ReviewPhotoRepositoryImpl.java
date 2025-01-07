package com.takeit.review.infrastructure.repository;

import com.takeit.review.application.repository.ReviewPhotoRepository;
import com.takeit.review.domain.entity.Review;
import com.takeit.review.domain.entity.ReviewPhoto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ReviewPhotoRepositoryImpl implements ReviewPhotoRepository {
    public final JpaReviewPhotoRepository jpaReviewPhotoRepository;

    @Override
    public List<ReviewPhoto> saveAll(List<ReviewPhoto> reviewPhotos) {
        return jpaReviewPhotoRepository.saveAll(reviewPhotos);
    }

    @Override
    public List<ReviewPhoto> findByReviewAndDeletedIsFalse(Review review) {
        return jpaReviewPhotoRepository.findByReviewAndIsDeletedIsFalse(review);
    }

    @Override
    public List<ReviewPhoto> findAllByReview(Review review) {
        return jpaReviewPhotoRepository.findAllByReviewAndIsDeletedIsFalse(review);
    }

    @Override
    public List<ReviewPhoto> findByUuidInAndIsDeletedIsFalse(List<UUID> uuids) {
        return jpaReviewPhotoRepository.findByUuidInAndIsDeletedIsFalse(uuids);
    }
}
