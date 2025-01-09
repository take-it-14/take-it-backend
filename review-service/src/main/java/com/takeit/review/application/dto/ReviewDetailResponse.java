package com.takeit.review.application.dto;

import com.takeit.review.application.dto.product.ProductDto;
import com.takeit.review.domain.entity.Review;

import java.util.List;
import java.util.UUID;

public record ReviewDetailResponse(
        UUID id,
        int stars,
        String comment,
        String productName,
        List<ReviewPhotoDto> photos
) {
    public static ReviewDetailResponse from(Review review) {
        return new ReviewDetailResponse(
                review.getUuid(),
                review.getStars(),
                review.getComment(),
                null,
                review.getPhotoList().stream().map(ReviewPhotoDto::from).toList()
        );
    }

    public static ReviewDetailResponse of(Review review, ProductDto productDto) {
        return new ReviewDetailResponse(
                review.getUuid(),
                review.getStars(),
                review.getComment(),
                productDto.productName(),
                review.getPhotoList().stream().map(ReviewPhotoDto::from).toList()
        );
    }
}
