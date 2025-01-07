package com.takeit.product.application.dto.category;

import com.takeit.product.domain.entity.Category;

import java.util.UUID;

public record CategoryResponse(
        Long id,
        UUID uuid,
        String name
){
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getUuid(), category.getName());
    }
}
