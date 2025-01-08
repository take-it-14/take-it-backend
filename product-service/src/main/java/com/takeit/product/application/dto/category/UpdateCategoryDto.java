package com.takeit.product.application.dto.category;

public record UpdateCategoryDto(
        String name
) {
    public static UpdateCategoryDto from(String name) {
        return new UpdateCategoryDto(name);
    }
}
