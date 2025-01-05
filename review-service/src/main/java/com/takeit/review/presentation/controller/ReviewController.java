package com.takeit.review.presentation.controller;

import com.takeit.common.presentation.dto.CommonResponse;
import com.takeit.review.application.dto.CreateReviewResponse;
import com.takeit.review.application.dto.ReviewResponse;
import com.takeit.review.application.dto.UpdateReviewResponse;
import com.takeit.review.application.service.ReviewService;
import com.takeit.review.presentation.request.CreateReviewRequest;
import com.takeit.review.presentation.request.UpdateReviewRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public CommonResponse<ReviewResponse> getReview(
            @RequestHeader(name = "X-Username", required = false) String username,
            @PathVariable UUID reviewId
    ) {
        return CommonResponse.ofSuccess("리뷰 상세 조회에 성공했습니다.", reviewService.getReview(username, reviewId));
    }
}
