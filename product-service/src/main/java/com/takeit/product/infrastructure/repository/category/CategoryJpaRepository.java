package com.takeit.product.infrastructure.repository.category;

import com.takeit.product.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryJpaRepository extends JpaRepository<Category, Long>{
    Optional<Category> findByName(String name);
    Optional<Category> findByUuid(UUID categoryId);
}
