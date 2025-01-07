package com.takeit.product.application.service;

import com.takeit.common.exception.CustomException;
import com.takeit.common.exception.ErrorCode;
import com.takeit.product.application.dto.category.CategoryEntityResponse;
import com.takeit.product.application.dto.category.CreateCategoryDto;
import com.takeit.product.application.dto.category.UpdateCategoryDto;
import com.takeit.product.domain.entity.Category;
import com.takeit.product.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryEntityResponse createCategory(CreateCategoryDto request, String username) {
        // TODO: username으로 권한체크

        Optional<Category> category = categoryRepository.findByName(request.name());
        if (category.isPresent()) {
            category.get().restore();
        } else {
            category = Optional.of(categoryRepository.save(
                    Category.create(request.name())
            ));
        }

        return CategoryEntityResponse.from(category.get());
    }

    @Transactional
    public CategoryEntityResponse updateCategory(UpdateCategoryDto request, UUID categoryId, String username) {
        // TODO: username으로 권한체크

        return categoryRepository.findByIdAndIsDeleteFalse(categoryId).map(category -> {
            category.update(request.name());
            return CategoryEntityResponse.from(category);
        }).orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

    }
}
