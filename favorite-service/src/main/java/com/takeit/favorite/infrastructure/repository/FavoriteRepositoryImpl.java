package com.takeit.favorite.infrastructure.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.takeit.favorite.domain.entity.Favorite;
import com.takeit.favorite.domain.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static com.takeit.favorite.domain.entity.QFavorite.favorite;

@RequiredArgsConstructor
@Repository
public class FavoriteRepositoryImpl implements FavoriteRepository {
    private final FavoriteJpaRepository jpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Favorite save(Favorite favorite) {
        return jpaRepository.save(favorite);
    }

    @Override
    public Optional<Favorite> findByProductIdAndUserId(Long productId, Long userId) {
        return jpaRepository.findByProductIdAndUserId(productId, userId);
    }

    @Override
    public Optional<Favorite> findByUuidIsDeleteFalse(UUID uuid) {
        JPAQuery<Favorite> query = queryFactory
                .select(favorite)
                .from(favorite)
                .where(favorite.uuid.eq(uuid).and(favorite.isDeleted.eq(false)));

        return Optional.ofNullable(query.fetchOne());
    }
}
