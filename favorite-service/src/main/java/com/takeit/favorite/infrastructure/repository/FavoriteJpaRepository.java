package com.takeit.favorite.infrastructure.repository;

import com.takeit.favorite.domain.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.UUID;

public interface FavoriteJpaRepository extends JpaRepository<Favorite, UUID>,
        QuerydslPredicateExecutor<Favorite> {

    Optional<Favorite> findByProductIdAndUserId(Long productId, Long userId);
}