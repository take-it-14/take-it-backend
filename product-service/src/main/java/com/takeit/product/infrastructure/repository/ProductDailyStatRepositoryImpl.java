package com.takeit.product.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.takeit.product.domain.entity.ProductDailyStat;
import com.takeit.product.domain.repository.ProductRepository;

public interface ProductDailyStatRepositoryImpl extends JpaRepository<ProductDailyStat, Long>, ProductRepository {
}
