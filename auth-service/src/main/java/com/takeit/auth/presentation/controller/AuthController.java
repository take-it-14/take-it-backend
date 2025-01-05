package com.takeit.auth.presentation.controller;

import com.takeit.auth.application.dto.UserResponse;
import com.takeit.auth.application.service.UserService;
import com.takeit.auth.presentation.request.CustomerSignUpRequest;
import com.takeit.common.presentation.dto.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auths")
public class AuthController {

    private final UserService userService;

    // 일반 회원 가입
    @PostMapping("/signup/customer")
    public ResponseEntity<CommonResponse<UserResponse>> signUp(@RequestBody @Valid CustomerSignUpRequest request) {
        return ResponseEntity.ok().body(CommonResponse.ofSuccess("회원 가입 성공", userService.createUser(request.toDto())));
    }

}
