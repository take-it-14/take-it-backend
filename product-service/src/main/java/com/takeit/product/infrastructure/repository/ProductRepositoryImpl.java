package com.takeit.product.infrastructure.repository;

import com.takeit.product.domain.entity.Product;
import com.takeit.product.domain.repository.ProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepositoryImpl extends JpaRepository<Product, Long>, ProductRepository {

}
