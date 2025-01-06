package com.takeit.review.application.dto;

import com.takeit.review.domain.entity.Review;
import com.takeit.review.domain.entity.ReviewPhoto;

import java.util.List;
import java.util.UUID;

public record CreateReviewResponse(
        UUID id,
        UUID orderId,
        int stars,
        String comment,
        List<ReviewPhotoDto> photos
) {

    public static CreateReviewResponse of(Review review, UUID orderId) {
        return new CreateReviewResponse(
                review.getUuid(),
                orderId,
                review.getStars(),
                review.getComment(),
                review.getPhotoList().stream().map(ReviewPhotoDto::from).toList()
        );
    }

    public record ReviewPhotoDto(
            UUID id,
            String uri
    ){
        public static ReviewPhotoDto from(ReviewPhoto photo) {
            return new ReviewPhotoDto(
                    photo.getUuid(),
                    photo.getUri()
            );
        }
    }
}
