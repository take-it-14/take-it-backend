package com.takeit.review.infrastructure.repository;

import com.takeit.review.application.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {
    private final JpaReviewRepository jpaReviewRepository;
}
