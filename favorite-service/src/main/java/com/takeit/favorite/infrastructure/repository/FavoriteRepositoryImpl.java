package com.takeit.favorite.infrastructure.repository;

import com.takeit.favorite.domain.entity.Favorite;
import com.takeit.favorite.domain.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class FavoriteRepositoryImpl implements FavoriteRepository {
    private final FavoriteJpaRepository jpaRepository;

    @Override
    public Favorite save(Favorite favorite) {
        return jpaRepository.save(favorite);
    }

    @Override
    public Optional<Favorite> findByProductIdAndUserId(Long productId, Long userId) {
        return Optional.empty();
    }
}
