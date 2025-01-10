package com.takeit.review.application.dto;

import com.takeit.review.domain.entity.Review;

import java.util.List;
import java.util.UUID;

public record UpdateReviewResponse(UUID id,
                                   String productName,
                                   int stars,
                                   String comment,
                                   List<ReviewPhotoDto> photos
) {

    public static UpdateReviewResponse of(Review review, String productName) {
        return new UpdateReviewResponse(
                review.getUuid(),
                productName,
                review.getStars(),
                review.getComment(),
                review.getPhotoList().stream().map(ReviewPhotoDto::from).toList()
        );
    }
}