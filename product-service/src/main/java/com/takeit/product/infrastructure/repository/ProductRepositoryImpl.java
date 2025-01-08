package com.takeit.product.infrastructure.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.takeit.product.domain.entity.Product;
import com.takeit.product.domain.entity.QProduct;
import com.takeit.product.domain.repository.ProductRepository;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

public interface ProductRepositoryImpl extends JpaRepository<Product, Long>, ProductRepository,
        QuerydslPredicateExecutor<Product>, QuerydslBinderCustomizer<QProduct> {

    @Override
    default void customize(QuerydslBindings querydslBindings, @NotNull QProduct qProduct) {
        // 모든 String 필드에 대해 동적 검색
        querydslBindings.bind(String.class).all((StringPath path, Collection<? extends String> values) -> {
            List<String> valueList = new ArrayList<>(values.stream().map(String::trim).toList());
            if (valueList.isEmpty()) {
                return Optional.empty();
            }
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            //booleanBuilder.and(qUser.isDeleted.eq(false)); // is_deleted = false

            for (String s : valueList) {
                booleanBuilder.or(path.containsIgnoreCase(s));
            }
            return Optional.of(booleanBuilder);
        });

        // 검색 제외 필드
        querydslBindings.excluding(qProduct.imageUrl);

    }

}
