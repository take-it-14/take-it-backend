package com.takeit.review.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;

import java.util.UUID;

public record ReviewPageResponse(
        ReviewPage reviewPage
) {
    public static ReviewPageResponse from(Page<ReviewPage.Review> reviews) {
        return new ReviewPageResponse(
                new ReviewPage(reviews)
        );
    }

    public static class ReviewPage extends PagedModel<ReviewPage.Review> {
        public ReviewPage(Page<Review> page) {
            super(new PageImpl<>(
                    page.getContent(),
                    page.getPageable(),
                    page.getTotalElements()
            ));
        }

        @Getter
        @Builder
        public static class Review {
            private UUID id;
            private int stars;
            private String comment;

            @QueryProjection
            public Review(UUID id, int stars, String comment) {
                this.id = id;
                this.stars = stars;
                this.comment = comment;
            }
        }

        public static Review from(com.takeit.review.domain.entity.Review review) {
            return Review.builder()
                    .id(review.getUuid())
                    .stars(review.getStars())
                    .comment(review.getComment())
                    .build();
        }
    }
}