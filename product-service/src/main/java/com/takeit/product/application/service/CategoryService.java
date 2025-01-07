package com.takeit.product.application.service;

import com.takeit.product.application.dto.category.CategoryResponse;
import com.takeit.product.application.dto.category.CreateCategoryDto;
import com.takeit.product.domain.entity.Category;
import com.takeit.product.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponse createCategory(CreateCategoryDto request, String username) {
        // TODO: username으로 권한체크
        return CategoryResponse.from(categoryRepository.createCategory(Category.create(request.name())));
    }
}
