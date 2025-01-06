package com.takeit.favorite.domain.repository;

import com.takeit.favorite.domain.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface FavoriteRepository {
    Favorite save(Favorite favorite);

    Optional<Favorite> findByProductIdAndUserId(Long productId, Long userId);

    Optional<Favorite> findByUuidIsDeleteFalse(UUID uuid);

    Page<Favorite> getUserFavorites(Long userId, Pageable pageable);
}
