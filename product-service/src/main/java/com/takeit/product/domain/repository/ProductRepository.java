package com.takeit.product.domain.repository;

import com.querydsl.core.types.Predicate;
import com.takeit.product.domain.entity.Product;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findByUuidAndIsDeletedFalse(UUID uuid);

    Page<Product> findAll(Predicate predicate, Pageable pageable);

}
