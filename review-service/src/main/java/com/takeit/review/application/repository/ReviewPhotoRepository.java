package com.takeit.review.application.repository;

import com.takeit.review.domain.entity.Review;
import com.takeit.review.domain.entity.ReviewPhoto;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ReviewPhotoRepository {
    List<ReviewPhoto> saveAll(List<ReviewPhoto> reviewPhotos);

    int deletedAll(List<UUID> uuids, String username);

    List<ReviewPhoto> findByReviewAndDeletedIsFalse(Review review);

    List<ReviewPhoto> findAllByReview(Review review);
}
