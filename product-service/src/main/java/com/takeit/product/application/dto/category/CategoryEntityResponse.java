package com.takeit.product.application.dto.category;

import com.takeit.product.domain.entity.Category;

import java.util.UUID;

public record CategoryEntityResponse(
        Long id,
        UUID uuid,
        String name
){
    public static CategoryEntityResponse from(Category category) {
        return new CategoryEntityResponse(category.getId(), category.getUuid(), category.getName());
    }
}
