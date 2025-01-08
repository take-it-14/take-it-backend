package com.takeit.product.infrastructure.repository.category;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.takeit.product.domain.entity.Category;
import com.takeit.product.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static com.takeit.product.domain.entity.QCategory.category;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {
    private final CategoryJpaRepository jpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Category save(Category category) {
        return jpaRepository.save(category);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return jpaRepository.findByName(name);
    }

    @Override
    public Optional<Category> findByIdAndIsDeleteFalse(Long categoryId) {
        JPAQuery<Category> query = queryFactory
                .selectFrom(category)
                .where(category.id.eq(categoryId).and(category.isDeleted.eq(false)));

        return Optional.ofNullable(query.fetchOne());
    }
  
    @Override
    public Optional<Category> findByUuid(UUID categoryId) {
        return jpaRepository.findByUuid(categoryId);
    }
  
    @Override
    public Page<Category> getAllCategories(Pageable pageable) {
        JPAQuery<Category> query = queryFactory
                .selectFrom(category)
                .where(category.isDeleted.eq(false))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        return new PageImpl<>(query.fetch(), pageable, query.fetch().size());
    }
}
