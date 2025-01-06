package com.takeit.auth.presentation.controller;

import com.takeit.auth.application.dto.AuthResponse;
import com.takeit.auth.application.dto.SellerResponse;
import com.takeit.auth.application.dto.UserResponse;
import com.takeit.auth.application.dto.UserRoleResponse;
import com.takeit.auth.application.service.AuthService;
import com.takeit.auth.application.service.UserService;
import com.takeit.auth.presentation.request.CustomerSignUpRequest;
import com.takeit.auth.presentation.request.SellerSignUpRequest;
import com.takeit.auth.presentation.request.SignInRequest;
import com.takeit.common.presentation.dto.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auths")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<CommonResponse<AuthResponse>> signIn(@RequestBody @Valid SignInRequest request) {
        return ResponseEntity.ok().body(CommonResponse.ofSuccess("로그인 성공", authService.login(request)));
    }

    // 일반 회원 가입
    @PostMapping("/signup/customer")
    public ResponseEntity<CommonResponse<UserResponse>> signUp(@RequestBody @Valid CustomerSignUpRequest request) {
        return ResponseEntity.ok().body(CommonResponse.ofSuccess("회원 가입 성공", userService.createUser(request.toDto())));
    }

    // 판매자 회원 가입
    @PostMapping("/signup/seller")
    public ResponseEntity<CommonResponse<SellerResponse>> signUpSeller(@RequestBody @Valid SellerSignUpRequest request) {
        return ResponseEntity.ok().body(CommonResponse.ofSuccess("회원 가입 성공", userService.createSeller(request.toDto())));
    }

    // 사용자 권한 조회
    @GetMapping("/role/{username}")
    public ResponseEntity<CommonResponse<UserRoleResponse>> getUserRoleByUsername(@PathVariable String username) {
        return ResponseEntity.ok().body(CommonResponse.ofSuccess("권한 조회 성공", authService.getUserRoleByUsername(username)));
    }

}
