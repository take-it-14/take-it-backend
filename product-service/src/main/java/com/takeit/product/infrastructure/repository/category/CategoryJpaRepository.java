package com.takeit.product.infrastructure.repository.category;

import com.takeit.product.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<Category, Long>{
}
