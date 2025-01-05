package com.takeit.favorite.infrastructure.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.takeit.favorite.domain.entity.Favorite;
import com.takeit.favorite.domain.entity.QFavorite;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.*;

public interface FavoriteJpaRepository extends JpaRepository<Favorite, UUID>,
        QuerydslPredicateExecutor<Favorite>,
        QuerydslBinderCustomizer<QFavorite> {
    @Override
    default void customize(QuerydslBindings querydslBindings, @NotNull QFavorite qFavorite) {

    }

    Optional<Favorite> findByProductIdAndUserId(Long productId, Long userId);
}