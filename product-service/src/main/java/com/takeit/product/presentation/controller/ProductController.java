package com.takeit.product.presentation.controller;

import com.querydsl.core.types.Predicate;
import com.takeit.common.exception.CustomException;
import com.takeit.common.exception.ErrorCode;
import com.takeit.common.presentation.dto.CommonResponse;
import com.takeit.product.application.dto.product.ProductDailyStatResponse;
import com.takeit.product.application.dto.product.ProductDetailResponse;
import com.takeit.product.application.dto.product.ProductPageResponse;
import com.takeit.product.application.dto.product.ProductResponse;
import com.takeit.product.application.service.ProductService;
import com.takeit.product.domain.entity.Product;
import com.takeit.product.presentation.request.CreateProductRequest;
import com.takeit.product.presentation.request.UpdateProductRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

	private final ProductService productService;



    // 상품 등록
    @PostMapping
    public ResponseEntity<CommonResponse<ProductResponse>> createProduct(@Valid CreateProductRequest request,
                                                                         @RequestHeader(name = "X-Username") String requesterUsername) {
        return ResponseEntity.ok().body(CommonResponse.ofSuccess("상품 등록 성공", productService.createProduct(request.toDto(), requesterUsername)));
    }

    // 상품 수정
    @PutMapping("/{productId}")
    public ResponseEntity<CommonResponse<ProductResponse>> updateProduct(
            @RequestHeader(name = "X-Username") String username,
            @PathVariable UUID productId,
            @Valid UpdateProductRequest request) {
        return ResponseEntity.ok().body(CommonResponse.ofSuccess("상품 수정 성공", productService.updateProduct(productId, request.toDto(), username)));
    }

    // 상품 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<CommonResponse<ProductResponse>> deleteProduct(
            @PathVariable UUID productId,
            @RequestHeader(value = "X-Username") String username) {
        return ResponseEntity.ok().body(CommonResponse.ofSuccess("상품 삭제 성공", productService.deleteProduct(productId, username)));
    }

    // 상품 단건 조회
    @GetMapping("/{productId}")
    public ResponseEntity<CommonResponse<ProductDetailResponse>> getProduct(@PathVariable UUID productId) {
        return ResponseEntity.ok().body(CommonResponse.ofSuccess("상품 조회 성공", productService.getProduct(productId)));
    }

    // 상품 목록 조회
    @GetMapping
    public ResponseEntity<CommonResponse<ProductPageResponse>> getProducts(
            @QuerydslPredicate(root = Product.class) Predicate predicate,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(CommonResponse.ofSuccess("상품 목록 조회 성공", productService.getProducts(predicate, pageable)));
    }

	@GetMapping("/board/{productId}")
	public CommonResponse<Page<ProductDailyStatResponse>> getProductDailyStat(
		@PathVariable UUID productId,
		@RequestParam String startDate,
		@RequestParam String endDate,
		Pageable pageable
	) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date start = sdf.parse(startDate);
			Date end = sdf.parse(endDate);

			return CommonResponse.ofSuccess("상품 통계 조회",
				productService.getProductDailyStat(productId, start, end, pageable));
		} catch (ParseException e) {
			throw new CustomException(ErrorCode.INVALID_DATE_FORMAT);
		}
	}
}
