package com.takeit.review.application.repository;

import com.takeit.review.domain.entity.Review;

public interface ReviewRepository {
    Review save(Review review);
}
