package com.takeit.review.infrastructure.client;

import com.takeit.review.application.service.OrderService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "order-service")
public interface OrderClient extends OrderService {
    @GetMapping("feign/v1/orders/{orderId}/productId")
    Long getProductId(@PathVariable UUID orderId, @RequestParam Long userId);
}
