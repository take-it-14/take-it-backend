package com.takeit.favorite.infrastructure.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.takeit.favorite.domain.entity.Favorite;
import com.takeit.favorite.domain.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Override
    public Page<Favorite> getUserFavorites(Long userId, Pageable pageable) {
        JPAQuery<Favorite> query = queryFactory
                .select(favorite)
                .from(favorite)
                .where(favorite.userId.eq(userId).and(favorite.isDeleted.eq(false)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order order : pageable.getSort()) {
            PathBuilder<Object> path = new PathBuilder<>(Object.class, order.getProperty());
            query.orderBy(new OrderSpecifier(com.querydsl.core.types.Order.valueOf(order.getDirection().name()), path));
        }

        long total = query.fetch().size();

        List<Favorite> results = query.fetch();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public List<Favorite> findByProductId(Long productId) {
        return jpaRepository.findAllByProductId(productId);
    }
}
