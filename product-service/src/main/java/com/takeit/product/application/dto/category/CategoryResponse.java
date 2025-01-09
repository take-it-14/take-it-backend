package com.takeit.product.application.dto.category;

import java.util.UUID;

public record CategoryResponse(
        UUID categoryId,
        String name
){
    public static CategoryResponse of(UUID categoryId, String name) {
        return new CategoryResponse(categoryId, name);
    }
}
