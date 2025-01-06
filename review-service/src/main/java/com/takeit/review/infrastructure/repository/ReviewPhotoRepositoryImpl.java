package com.takeit.review.infrastructure.repository;

import com.takeit.review.application.repository.ReviewPhotoRepository;
import com.takeit.review.domain.entity.ReviewPhoto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewPhotoRepositoryImpl implements ReviewPhotoRepository {
    public final JpaReviewPhotoRepository jpaReviewPhotoRepository;

    @Override
    public List<ReviewPhoto> saveAll(List<ReviewPhoto> reviewPhotos) {
        return jpaReviewPhotoRepository.saveAll(reviewPhotos);
    }
}
