package com.takeit.review.application.dto;

import com.takeit.review.domain.entity.Review;

import java.util.List;
import java.util.UUID;

public record ReviewDetailResponse(
        UUID id,
        int stars,
        String comment,
        List<ReviewPhotoDto> photos
) {
    public static ReviewDetailResponse from(Review review) {
        return new ReviewDetailResponse(
                review.getUuid(),
                review.getStars(),
                review.getComment(),
                review.getPhotoList().stream().map(ReviewPhotoDto::from).toList()
        );
    }
}
