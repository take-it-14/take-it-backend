package com.takeit.product.domain.repository;

import com.takeit.product.domain.entity.Category;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {
    Category save(Category category);

    Optional<Category> findByName(String name);
    Optional<Category> findByIdAndIsDeleteFalse(Long categoryId);
}
