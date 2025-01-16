package com.takeit.order.presentation.controller;

import com.takeit.common.presentation.dto.CommonResponse;
import com.takeit.order.application.dto.queue.QueueDto;
import com.takeit.order.application.service.QueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/queue")
public class QueueController {
    private final QueueService queueService;

    @PostMapping("/products/{productId}")
    public CommonResponse<?> joinQueue(
            @PathVariable("productId") UUID productId,
            @RequestHeader("X-Username") String username) {
        boolean result = queueService.joinQueue(productId, username);
        return CommonResponse.ofSuccess("대기열에 정상적으로 등록되었습니다.", result ? "즉시 주문 생성 api 호출 가능" : "대기열 페이지로 이동");
    }

    @GetMapping("/products/{productId}")
    public CommonResponse<QueueDto> getRankAndIsActive(@PathVariable("productId") UUID productId,
                                                       @RequestHeader("X-Username") String username) {
        return CommonResponse.ofSuccess("현재 대기 순위가 조회되었습니다.", queueService.getRankAndIsActive(productId, username));
    }

}
