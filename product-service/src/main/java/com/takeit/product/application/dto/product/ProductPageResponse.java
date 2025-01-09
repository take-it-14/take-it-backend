package com.takeit.product.application.dto.product;

import com.takeit.product.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;

import java.util.List;
import java.util.UUID;

public record ProductPageResponse(
    ProductPage productPage
) {
    public static ProductPageResponse from (Page < Product > productPage) {
        return new ProductPageResponse(new ProductPage(productPage));
    }

    public static class ProductPage extends PagedModel<ProductPage.Product> {

        public ProductPage(Page<com.takeit.product.domain.entity.Product> productPage) {
            super(
                    new PageImpl<>(
                            ProductPage.Product.from(productPage.getContent()),
                            productPage.getPageable(),
                            productPage.getTotalElements()
                    )
            );
        }

        public static record Product(
                UUID ProductId,
                String productName,
                Long price,
                Integer stock,
                Boolean isActive
        ) {

            public static List<Product> from(
                    List<com.takeit.product.domain.entity.Product> productList) {
                return productList.stream()
                        .map(Product::from)
                        .toList();
            }

            public static Product from(com.takeit.product.domain.entity.Product product) {
                return new Product(
                        product.getUuid(),
                        product.getProductName(),
                        product.getPrice(),
                        product.getStock(),
                        product.getIsActive()
                );
            }
        }
    }
}
