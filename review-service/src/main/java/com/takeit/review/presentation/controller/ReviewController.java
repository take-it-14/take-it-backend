package com.takeit.review.presentation.controller;

import com.takeit.common.presentation.dto.CommonResponse;
import com.takeit.review.application.dto.CreateReviewResponse;
import com.takeit.review.application.service.ReviewService;
import com.takeit.review.presentation.request.CreateReviewRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
