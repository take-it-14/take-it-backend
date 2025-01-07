package com.takeit.product.domain.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.takeit.product.domain.entity.ProductDailyStat;

@Repository
public interface ProductDailyStatRepository {
	Page<ProductDailyStat> findByProductIdAndBaseDateBetween(Long productId, Date startDate, Date endDate, Pageable pageable);
}
