package com.takeit.product.presentation.request;

import com.takeit.product.application.dto.category.UpdateCategoryDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateCategoryRequest(
        @NotNull
        @Size(max = 50)
        String name
) {
    public UpdateCategoryDto toDto() {
        return UpdateCategoryDto.from(this.name);
    }
}
