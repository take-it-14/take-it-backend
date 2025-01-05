package com.takeit.review.application.dto;

import com.takeit.review.domain.entity.Review;
import com.takeit.review.domain.entity.ReviewPhoto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record ReviewResponse(
        UUID id,
        int stars,
        String comment,
        List<ReviewPhotoDto> photos
) {
    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                review.getUuid(),
                review.getStars(),
                review.getComment(),
                review.getPhotoList().stream().map(ReviewPhotoDto::from).toList()
        );
    }
}
