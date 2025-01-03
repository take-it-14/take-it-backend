package com.takeit.favorite.domain.repository;

import com.takeit.favorite.domain.entity.Favorite;

import java.util.Optional;

public interface FavoriteRepository {
    Favorite save(Favorite favorite);

    Optional<Favorite> findByProductIdAndUserId(Long productId, Long userId);
}
