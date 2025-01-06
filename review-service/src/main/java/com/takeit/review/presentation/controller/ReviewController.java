package com.takeit.review.presentation.controller;

import com.querydsl.core.types.Predicate;
import com.takeit.common.presentation.dto.CommonResponse;
import com.takeit.review.application.dto.CreateReviewResponse;
import com.takeit.review.application.dto.ReviewDetailResponse;
import com.takeit.review.application.dto.ReviewPageResponse;
import com.takeit.review.application.dto.UpdateReviewResponse;
import com.takeit.review.application.service.ReviewService;
import com.takeit.review.domain.entity.Review;
import com.takeit.review.presentation.request.CreateReviewRequest;
import com.takeit.review.presentation.request.UpdateReviewRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public CommonResponse<CreateReviewResponse> createReview(
            @RequestHeader(name = "X-Username") String username,
            @Valid CreateReviewRequest request) {

        return CommonResponse.ofSuccess("리뷰 생성에 성공했습니다.", reviewService.createReview(request, username));

    }

    @PutMapping("/{reviewId}")
    public CommonResponse<UpdateReviewResponse> updateReview(
            @RequestHeader(name = "X-Username") String username,
            @Valid UpdateReviewRequest request,
            @PathVariable UUID reviewId) {
        return CommonResponse.ofSuccess("리뷰 수정에 성공했습니다.", reviewService.updateReview(request, reviewId, username));
    }

    @GetMapping("/{reviewId}")
    public CommonResponse<ReviewDetailResponse> getReview(
            @RequestHeader(name = "X-Username", required = false) String username,
            @PathVariable UUID reviewId
    ) {
        return CommonResponse.ofSuccess("리뷰 상세 조회에 성공했습니다.", reviewService.getReview(username, reviewId));
    }

    @GetMapping
    public CommonResponse<ReviewPageResponse> getReviews(
            @RequestHeader(name = "X-Username", required = false) String username,
            @QuerydslPredicate(root = Review.class) Predicate predicate,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "direction", defaultValue = "desc") String direction,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Pageable pageable
    ) {

        return CommonResponse.ofSuccess("리뷰 목록 조회에 성공했습니다.", reviewService.getReviews(
                PageRequest.of(
                        pageable.getPageNumber(),
                        size,
                        Sort.by(Sort.Order.by(sort).with(Sort.Direction.fromString(direction)))),
                predicate,
                username)
        );
    }
}
