package com.takeit.product.presentation.controller;

import com.takeit.common.presentation.dto.CommonResponse;
import com.takeit.product.application.dto.category.CategoryEntityResponse;
import com.takeit.product.application.dto.category.CategoryResponse;
import com.takeit.product.application.service.CategoryService;
import com.takeit.product.presentation.request.CreateCategoryRequest;
import com.takeit.product.presentation.request.UpdateCategoryRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @PatchMapping("/{categoryId}")
    public CommonResponse<CategoryEntityResponse> updateCategory(@PathVariable UUID categoryId,
                                                                 @Valid @RequestBody UpdateCategoryRequest request,
                                                                 @RequestHeader(value = "X-Username") String username) {
        return CommonResponse.ofSuccess("카테고리 수정", categoryService.updateCategory(request.toDto(), categoryId, username));
    }

    @DeleteMapping("/{categoryId}")
    public CommonResponse<?> deleteCategory(@PathVariable Long categoryId,
                                            @RequestHeader(value = "X-Username") String username) {

        categoryService.deleteCategory(categoryId, username);

        return CommonResponse.ofSuccess("카테고리 삭제",null);
    }

    @GetMapping("/{categoryId}")
    public CommonResponse<CategoryEntityResponse> getCategory(@PathVariable Long categoryId,
                                                        @RequestHeader(value = "X-Username") String username) {
        return CommonResponse.ofSuccess("카테고리 조회", categoryService.getCategory(categoryId, username));
    }

    @GetMapping("/all")
    public CommonResponse<PagedModel<CategoryResponse>> getAllCategories(Pageable pageable) {
        return CommonResponse.ofSuccess("카테고리 전체 목록 조회", categoryService.getAllCategories(pageable));
    }
}
