package com.takeit.auth.presentation.controller;

import com.takeit.auth.application.dto.SellerPageResponse;
import com.takeit.auth.application.dto.SellerResponse;
import com.takeit.auth.application.service.AdminService;
import com.takeit.common.presentation.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admins")
public class AdminController {

    private final AdminService adminService;

    // TODO 관리자 권한 체크

    // 승인 요청된 판매자 목록 조회
    @GetMapping("/sellers")
    public ResponseEntity<CommonResponse<SellerPageResponse>> getSellers(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC, page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok().body(CommonResponse.ofSuccess("승인 요청된 판매자 목록 조회 성공", adminService.getSellers(pageable)));
    }

    // 판매자 승인
    @PatchMapping("/sellers/{sellerInfoId}/approve")
    public ResponseEntity<CommonResponse<SellerResponse>> approveSeller(@PathVariable Long sellerInfoId) {
        return ResponseEntity.ok().body(CommonResponse.ofSuccess("판매자 승인 처리 성공", adminService.approveSeller(sellerInfoId)));
    }

}
