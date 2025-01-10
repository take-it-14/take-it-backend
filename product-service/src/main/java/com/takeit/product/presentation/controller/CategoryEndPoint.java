package com.takeit.product.presentation.controller;

import com.takeit.product.application.dto.category.CategoryEntityResponse;
import com.takeit.product.application.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feign/v1/categories")
public class CategoryEndPoint {
    private final CategoryService categoryService;

    @GetMapping("/uuid/{categoryId}")
    public CategoryEntityResponse getCategoryByUuid(@PathVariable UUID categoryId) {
        return categoryService.getCategoryByUuid(categoryId);
    }

    @GetMapping("/id/{categoryId}")
    public CategoryEntityResponse getCategoryById(@PathVariable Long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }
}
