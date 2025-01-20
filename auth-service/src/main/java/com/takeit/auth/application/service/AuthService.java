package com.takeit.auth.application.service;

import com.takeit.auth.application.dto.AuthResponse;
import com.takeit.auth.application.dto.UserAuthResponse;
import com.takeit.auth.domain.entity.User;
import com.takeit.auth.domain.entity.UserRole;
import com.takeit.auth.domain.repository.UserRepository;
import com.takeit.auth.jwt.JwtUtil;
import com.takeit.auth.presentation.request.SignInRequest;
import com.takeit.common.exception.CustomException;
import com.takeit.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserRedisService userRedisService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 로그인
    public AuthResponse login(final SignInRequest request) {
        // 사용자 확인
        User user = userRedisService.getUser(request.username());
        if(user == null) {
            user = userRepository.findByUsernameAndIsDeletedFalse(request.username()).orElseThrow(
                    () -> new CustomException(ErrorCode.USER_NOT_FOUND)
            );
            userRedisService.saveUser(user);
        }

        // 비밀번호 확인
        if(!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCHED);
        }

        // JWT 토큰 생성 및 반환
        String token = jwtUtil.createAccessToken(user.getUsername(), user.getRole());

        return new AuthResponse(token);
    }

    // 사용자 정보 조회
    public UserAuthResponse getUserByUsername(String username) {
        // 사용자 확인
        User user = userRedisService.getUser(username);
        if(user == null) {
            user = userRepository.findByUsernameAndIsDeletedFalse(username).orElseThrow(
                    () -> new CustomException(ErrorCode.USER_NOT_FOUND)
            );
        }

        return new UserAuthResponse(user.getId(), user.getUsername(), user.getNickname(), user.getEmail(), user.getRole());
    }

    // 권한 체크 내부용
    public UserRole getUserRoleByUsername(String requesterUsername) {
        User user = userRedisService.getUser(requesterUsername);
        if(user == null) {
            user = userRepository.findByUsernameAndIsDeletedFalse(requesterUsername).orElseThrow(
                    () -> new CustomException(ErrorCode.USER_NOT_FOUND)
            );
        }
        return user.getRole();
    }

}
