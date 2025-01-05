package com.takeit.review.infrastructure.repository;

import com.takeit.review.domain.entity.Review;
import com.takeit.review.domain.entity.ReviewPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface JpaReviewPhotoRepository extends JpaRepository<ReviewPhoto, Long> {
    @Modifying
    @Query("update ReviewPhoto set isDeleted = true, deletedAt = CURRENT_TIMESTAMP, deletedBy = :username, updatedAt = CURRENT_TIMESTAMP, updatedBy = :username where uuid in :uuids")
    int deleteAllByUuidIn(List<UUID> uuids, String username);

    List<ReviewPhoto> findByReviewAndIsDeletedIsFalse(Review review);

    List<ReviewPhoto> findAllByReviewAndIsDeletedIsFalse(Review review);
}
