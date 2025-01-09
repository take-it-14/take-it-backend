package com.takeit.product.domain.repository;

import com.takeit.product.domain.entity.ProductDailyStat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ProductDailyStatRepository {
	Page<ProductDailyStat> findByProductIdAndBaseDateBetween(Long productId, Date startDate, Date endDate, Pageable pageable);
}
