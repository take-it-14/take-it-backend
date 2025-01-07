package com.takeit.review.application.dto;

import com.takeit.review.domain.entity.ReviewPhoto;

import java.util.UUID;

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
