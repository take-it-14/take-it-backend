package com.takeit.product.domain.repository;

import com.takeit.product.domain.entity.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository {

    Product save(Product product);

}
