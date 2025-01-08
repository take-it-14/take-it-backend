package com.takeit.auth.presentation.controller;

import com.querydsl.core.types.Predicate;
import com.takeit.auth.application.dto.UserPageResponse;
import com.takeit.auth.application.dto.UserResponse;
import com.takeit.auth.application.service.UserService;
import com.takeit.auth.domain.entity.User;
import com.takeit.common.presentation.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    // 사용자 조회
    @GetMapping("/{username}")
    public ResponseEntity<CommonResponse<UserResponse>> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok().body(CommonResponse.ofSuccess("사용자 조회 성공", userService.getUserByUsername(username)));
    }

    // 사용자 목록 조회
    @GetMapping
    public ResponseEntity<CommonResponse<UserPageResponse>> getUsers(
            @QuerydslPredicate(root = User.class) Predicate predicate,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC, page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok().body(CommonResponse.ofSuccess("사용자 목록 조회 성공", userService.getUsers(predicate, pageable)));
    }

}
