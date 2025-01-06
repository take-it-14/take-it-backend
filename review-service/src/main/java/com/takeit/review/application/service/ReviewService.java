package com.takeit.review.application.service;

import com.takeit.common.exception.CustomException;
import com.takeit.review.application.dto.CreateReviewResponse;
import com.takeit.review.application.dto.UpdateReviewResponse;
import com.takeit.review.application.repository.ReviewPhotoRepository;
import com.takeit.review.application.repository.ReviewRepository;
import com.takeit.review.domain.entity.Review;
import com.takeit.review.domain.entity.ReviewPhoto;
import com.takeit.review.presentation.request.CreateReviewRequest;
import com.takeit.review.presentation.request.UpdateReviewRequest;
import com.takeit.s3.infrastructure.util.FileUpload;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;

import static com.takeit.common.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewPhotoRepository reviewPhotoRepository;
    private final FileUpload fileUpload;

    @Transactional
    public CreateReviewResponse createReview(CreateReviewRequest request, String username) {
        // todo : username 으로 user 권한 체크 및 order uuid로 productId와 userid 가 일치하는지 체크
        Long productId = 1L;

        Review review = reviewRepository.save(Review.of(request, username, productId));

        if(request.files() != null && !request.files().isEmpty()) {
            if(request.files().size() > 3) {
                throw new CustomException(TOO_MANY_PHOTOS);
            }

            try {
                review.addPhotos(reviewPhotoRepository.saveAll(
                        fileUpload.uploadMultipleFile(request.files(), "review")
                                .stream().map(file -> ReviewPhoto.of(file, review, username)).toList()
                ));
            } catch (IOException e) {
                throw new CustomException(FILE_UPLOAD_ERROR);
            }
        }

        return CreateReviewResponse.of(review, request.orderId());
    }

    @Transactional
    public UpdateReviewResponse updateReview(@Valid UpdateReviewRequest request, UUID reviewId, String username) {
        // todo : username 으로 user 권한 체크 및 order uuid로 productId가 일치하는지 체크
        Long productId = 1L;

        Review review = reviewRepository.findByUuid(reviewId).orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND));

        review.updateReview(request);
        review = reviewRepository.save(review);

        int deleteFileSize = 0;
        if(request.deleteFileNames() != null && !request.deleteFileNames().isEmpty()) {
            deleteFileSize = reviewPhotoRepository.deletedAll(request.deleteFileNames(), username);
        }

        if(request.files() != null && !request.files().isEmpty()) {
            int count = reviewPhotoRepository.findByReviewAndDeletedIsFalse(review).size();

            // 총 보여질 사진 개수 (현재 저장되어 있는 사진 개수 - 지울 사진 개수 + 새로 등록할 사진)
            if(count - deleteFileSize + request.files().size() > 3) {
                throw new CustomException(TOO_MANY_PHOTOS);
            }

            try {
                Review finalReview = review;
                reviewPhotoRepository.saveAll(
                        fileUpload.uploadMultipleFile(request.files(), "review")
                                .stream().map(file -> ReviewPhoto.of(file, finalReview, username)).toList()
                );
            } catch (IOException e) {
                throw new CustomException(FILE_UPLOAD_ERROR);
            }
        }

        review.getPhotoList().clear();
        review.addPhotos(reviewPhotoRepository.findAllByReview(review));

        return UpdateReviewResponse.of(review, request.orderId());
    }


}
