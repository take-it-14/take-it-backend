package com.takeit.coupon.infrastructure.client;

import com.takeit.coupon.application.dto.category.CategoryDto;
import com.takeit.coupon.application.service.CategoryService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "product-service")
public interface CategoryClient extends CategoryService {
    @GetMapping("/feign/v1/categories/uuid/{categoryId}")
    CategoryDto getCategory(@PathVariable UUID categoryId);

    @GetMapping("/feign/v1/categories/id/{categoryId}")
    CategoryDto getCategory(@PathVariable Long categoryId);
}
