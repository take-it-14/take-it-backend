package com.takeit.favorite.infrastructure.repository;

import com.takeit.favorite.domain.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteJpaRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByProductIdAndUserId(Long productId, Long userId);

    List<Favorite> findAllByProductId(Long productId);
}