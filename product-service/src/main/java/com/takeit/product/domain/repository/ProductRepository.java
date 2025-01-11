package com.takeit.product.domain.repository;

import com.querydsl.core.types.Predicate;
import com.takeit.product.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository{

    Product save(Product product);

    Optional<Product> findByUuidAndIsDeletedFalse(UUID uuid);

    Page<Product> findAll(Predicate predicate, Pageable pageable);

    List<Product> getProductEntities(List<Long> idList, Predicate predicate);

    Optional<Product> findById(Long productId);
}
