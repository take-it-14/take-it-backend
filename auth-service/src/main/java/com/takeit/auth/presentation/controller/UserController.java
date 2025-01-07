package com.takeit.auth.presentation.controller;

import com.takeit.auth.application.dto.UserResponse;
import com.takeit.auth.application.service.UserService;
import com.takeit.common.presentation.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    // 사용자 조회
    @GetMapping("/{username}")
    public ResponseEntity<CommonResponse<UserResponse>> getUserByUsername(
            @PathVariable String username,
            @RequestHeader(value = "X-Role", required = false) String requesterRole,
            @RequestHeader(value = "X-Username", required = false) String requesterUsername) {
        return ResponseEntity.ok().body(CommonResponse.ofSuccess("사용자 조회 성공", userService.getUserByUsername(username, requesterRole, requesterUsername)));
    }

}
