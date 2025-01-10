package com.takeit.coupon.application.service;

import com.takeit.coupon.application.dto.category.CategoryDto;

import java.util.UUID;

public interface CategoryService {
    CategoryDto getCategory(UUID categoryId);

    CategoryDto getCategory(Long categoryId);
}
