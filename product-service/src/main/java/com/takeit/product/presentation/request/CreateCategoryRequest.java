package com.takeit.product.presentation.request;

import com.takeit.product.application.dto.category.CreateCategoryDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequest(
        @NotNull
        @Size(max = 50)
        String name
) {
    public CreateCategoryDto toDto() {
        return CreateCategoryDto.from(this.name);
    }
}
