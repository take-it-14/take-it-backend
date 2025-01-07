package com.takeit.product.presentation.controller;

import com.takeit.common.presentation.dto.CommonResponse;
import com.takeit.product.application.dto.category.CategoryEntityResponse;
import com.takeit.product.application.service.CategoryService;
import com.takeit.product.presentation.request.CreateCategoryRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public CommonResponse<CategoryEntityResponse> createCategory(@Valid @RequestBody CreateCategoryRequest request,
                                                                 @RequestHeader(value = "X-Username") String username) {
        return CommonResponse.ofSuccess("카테고리 생성", categoryService.createCategory(request.toDto(), username));
    }
}
