package com.takeit.review.application.repository;

import com.takeit.review.domain.entity.ReviewPhoto;

import java.util.List;

public interface ReviewPhotoRepository {
    public List<ReviewPhoto> saveAll(List<ReviewPhoto> reviewPhotos);
}
