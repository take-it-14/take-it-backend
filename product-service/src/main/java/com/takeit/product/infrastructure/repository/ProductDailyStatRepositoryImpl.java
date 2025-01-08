package com.takeit.product.infrastructure.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.takeit.product.domain.entity.ProductDailyStat;
import com.takeit.product.domain.repository.ProductDailyStatRepository;

public interface ProductDailyStatRepositoryImpl extends JpaRepository<ProductDailyStat, Long>,
	ProductDailyStatRepository {

	@Query("SELECT p FROM ProductDailyStat p WHERE p.productId = :productId AND p.baseDate BETWEEN :startDate AND :endDate ORDER BY p.baseDate DESC")
	Page<ProductDailyStat> findByProductIdAndBaseDateBetween(
		@Param("productId") Long productId,
		@Param("startDate") Date startDate,
		@Param("endDate") Date endDate,
		Pageable pageable
	);
}
