package com.takeit.review.application.service;

import com.takeit.common.exception.CustomException;
import com.takeit.review.application.dto.CreateReviewResponse;
import com.takeit.review.application.repository.ReviewPhotoRepository;
import com.takeit.review.application.repository.ReviewRepository;
import com.takeit.review.domain.entity.Review;
import com.takeit.review.domain.entity.ReviewPhoto;
import com.takeit.review.presentation.request.CreateReviewRequest;
import com.takeit.s3.infrastructure.util.FileUpload;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static com.takeit.common.exception.ErrorCode.FILE_UPLOAD_ERROR;
import static com.takeit.common.exception.ErrorCode.TOO_MANY_PHOTOS;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewPhotoRepository reviewPhotoRepository;
    private final FileUpload fileUpload;

    @Transactional
    public CreateReviewResponse createReview(@Valid CreateReviewRequest request, String username) {
        // todo : username 으로 user 권한 체크 및 order uuid로 productId와 userid 가 일치하는지 체크
        Long userId = 1L;
        Long productId = 1L;

        Review review = reviewRepository.save(Review.of(request, userId, productId));

        if(request.files() != null && !request.files().isEmpty()) {
            if(request.files().size() > 3) {
                throw new CustomException(TOO_MANY_PHOTOS);
            }

            try {
                review.addPhotos(reviewPhotoRepository.saveAll(
                        fileUpload.uploadMultipleFile(request.files(), "review")
                                .stream().map(file -> ReviewPhoto.of(file, review, userId)).toList()
                ));
            } catch (IOException e) {
                throw new CustomException(FILE_UPLOAD_ERROR);
            }
        }

        return CreateReviewResponse.of(review, request.orderId());
    }
}
