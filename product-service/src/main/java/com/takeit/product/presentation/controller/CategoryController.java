package com.takeit.product.presentation.controller;

import com.takeit.product.application.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class CategoryController {
    private final CategoryService categoryService;
}
