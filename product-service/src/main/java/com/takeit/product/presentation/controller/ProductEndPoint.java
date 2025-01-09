package com.takeit.product.presentation.controller;

import com.takeit.product.application.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feign/v1/products")
public class ProductEndPoint {
    private final ProductService productService;

}
