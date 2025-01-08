package com.takeit.product.domain.repository;

import com.takeit.product.domain.entity.Product;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findByUuid(UUID uuid);

}
