package com.takeit.product.application.dto.category;

public record CreateCategoryDto(
        String name
) {
    public static CreateCategoryDto from(String name) {
        return new CreateCategoryDto(name);
    }
}
