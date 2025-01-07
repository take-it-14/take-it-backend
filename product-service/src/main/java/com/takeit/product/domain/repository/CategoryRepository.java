package com.takeit.product.domain.repository;

import com.takeit.product.domain.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoryRepository {
    Category save(Category category);

    Optional<Category> findByName(String name);
    Optional<Category> findByIdAndIsDeleteFalse(Long categoryId);

    Page<Category> getAllCategories(Pageable pageable);
}
