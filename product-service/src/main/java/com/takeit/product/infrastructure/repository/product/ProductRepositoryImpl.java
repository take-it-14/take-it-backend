package com.takeit.product.infrastructure.repository.product;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.takeit.product.domain.entity.Product;
import com.takeit.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.takeit.product.domain.entity.QProduct.product;
@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
    private final JPAQueryFactory queryFactory;
    private final ProductJpaRepository jpaRepository;

    @Override
    public Product save(Product product) {
        return jpaRepository.save(product);
    }

    @Override
    public Optional<Product> findByUuidAndIsDeletedFalse(UUID uuid) {
        JPAQuery<Product> query = queryFactory
                .selectFrom(product)
                .where(product.uuid.eq(uuid).and(product.isDeleted.isFalse()));

        return Optional.ofNullable(query.fetchOne());
    }

    @Override
    public Page<Product> findAll(Predicate predicate, Pageable pageable) {
        return jpaRepository.findAll(predicate, pageable);
    }

    public List<Product> getProductEntities(List<Long> idList, Predicate predicate) {
        BooleanBuilder booleanBuilder = new BooleanBuilder(predicate);
        if(idList != null && !idList.isEmpty()) {
            booleanBuilder.and(product.id.in(idList));
        }
        return (List<Product>) jpaRepository.findAll(booleanBuilder);
    }

    @Override
    public Optional<Product> findById(Long productId) {
        return jpaRepository.findById(productId);
    }

}
