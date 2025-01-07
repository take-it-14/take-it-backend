package com.takeit.review.application.dto;

import com.takeit.review.domain.entity.Review;

import java.util.List;
import java.util.UUID;

public record UpdateReviewResponse(UUID id,
                                   UUID orderId,
                                   int stars,
                                   String comment,
                                   List<ReviewPhotoDto> photos
) {

    public static UpdateReviewResponse of(Review review, UUID orderId) {
        return new UpdateReviewResponse(
                review.getUuid(),
                orderId,
                review.getStars(),
                review.getComment(),
                review.getPhotoList().stream().map(ReviewPhotoDto::from).toList()
        );
    }
}