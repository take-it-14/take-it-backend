package com.takeit.review.application.service;

import com.querydsl.core.types.Predicate;
import com.takeit.common.exception.CustomException;
import com.takeit.common.utils.AccessValidator;
import com.takeit.review.application.dto.CreateReviewResponse;
import com.takeit.review.application.dto.ReviewDetailResponse;
import com.takeit.review.application.dto.ReviewPageResponse;
import com.takeit.review.application.dto.UpdateReviewResponse;
import com.takeit.review.application.dto.product.ProductDto;
import com.takeit.review.application.dto.user.UserDto;
import com.takeit.review.domain.repository.ReviewPhotoRepository;
import com.takeit.review.domain.repository.ReviewRepository;
import com.takeit.review.domain.entity.Review;
import com.takeit.review.domain.entity.ReviewPhoto;
import com.takeit.review.presentation.request.CreateReviewRequest;
import com.takeit.review.presentation.request.UpdateReviewRequest;
import com.takeit.s3.infrastructure.util.FileUpload;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.takeit.common.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewPhotoRepository reviewPhotoRepository;
    private final FileUpload fileUpload;

    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;

    @Transactional
    public CreateReviewResponse createReview(CreateReviewRequest request, String username) {
        UserDto userDto = userService.getUser(username);

        Long productId = orderService.getProductId(request.orderId(), userDto.id());

        Review review = reviewRepository.save(Review.of(request, productId));

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
        UserDto userDto = userService.getUser(username);

        Review review = findByUuid(reviewId);

        if(checkMasterAndManager(userDto.role()))
            validateUser(review, username);

        ProductDto productDto = productService.getProducts(List.of(review.getProductId())).get(0);

        review.updateReview(request);
        review = reviewRepository.save(review);

        int deleteFileSize = 0;
        if(request.deleteFileNames() != null && !request.deleteFileNames().isEmpty()) {
            List<ReviewPhoto> deletePhotos = reviewPhotoRepository.findByUuidInAndIsDeletedIsFalse(request.deleteFileNames());

            deletePhotos.forEach(reviewPhoto -> reviewPhoto.deleted(username));

            deleteFileSize = deletePhotos.size();
            reviewPhotoRepository.saveAll(deletePhotos);
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

        return UpdateReviewResponse.of(review, productDto.productName());
    }

    public ReviewDetailResponse getReview(String username, UUID reviewId) {
        Review review = reviewRepository.findReviewAndReviewPhotosByUuid(reviewId).orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND));

        List<ProductDto> productDto = productService.getProducts(List.of(review.getProductId()));

        if(productDto != null && !productDto.isEmpty()) {
            return ReviewDetailResponse.of(review, productDto.get(0));
        }

        return ReviewDetailResponse.from(review);
    }

    public ReviewPageResponse getReviews(Pageable pageable, Predicate predicate, String username) {
        return reviewRepository.findAll(predicate, pageable);
    }

    @Transactional
    public void deleteReview(UUID reviewId, String username) {
        UserDto userDto = userService.getUser(username);

        Review review = findByUuid(reviewId);

        if(checkMasterAndManager(userDto.role()))
            validateUser(review, username);

        review.deleted(username);

        review = reviewRepository.save(review);

        List<ReviewPhoto> reviewPhotos = reviewPhotoRepository.findByReviewAndDeletedIsFalse(review);

        reviewPhotos.forEach(reviewPhoto -> {
            reviewPhoto.deleted(username);
        });

        reviewPhotoRepository.saveAll(reviewPhotos);

    }

    private Review findByUuid(UUID reviewId) {
        return reviewRepository.findByUuid(reviewId).orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND));
    }

    private void validateUser(Review review, String username) {
        if(!review.getCreatedBy().equals(username)) {
            throw new CustomException(UNAUTHORIZED);
        }
    }

    private boolean checkMasterAndManager(String role) {
        return !AccessValidator.isManager(role) && !AccessValidator.isMaster(role);
    }

}
