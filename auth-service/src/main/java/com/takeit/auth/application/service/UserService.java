package com.takeit.auth.application.service;

import com.takeit.auth.application.dto.CreateSellerDto;
import com.takeit.auth.application.dto.CreateUserDto;
import com.takeit.auth.application.dto.UserResponse;
import com.takeit.auth.domain.entity.SellerInfo;
import com.takeit.auth.domain.entity.SellerInfoStatus;
import com.takeit.auth.domain.entity.User;
import com.takeit.auth.domain.repository.SellerInfoRepository;
import com.takeit.auth.domain.repository.UserRepository;
import com.takeit.common.exception.CustomException;
import com.takeit.common.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SellerInfoRepository sellerInfoRepository;
    private final PasswordEncoder passwordEncoder;

    // 사용자 등록
    @Transactional
    public UserResponse createUser(CreateUserDto request) {
        // validation
        if (isUsernameExists(request.username())) { // username 중복 확인
            throw new CustomException(ErrorCode.USERNAME_ALEADY_EXISTS);
        } else if (isEmailExists(request.email())) { // email 중복 확인
            throw new CustomException(ErrorCode.EMAIL_ALEADY_EXISTS);
        }

        // 비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(request.password());

        User user = User.create(
                request.username(),
                request.nickname(),
                request.email(),
                encryptedPassword,
                request.role()
        );

        userRepository.save(user);

        return UserResponse.of(user);
    }

    // 판매자 등록
    @Transactional
    public UserResponse createSeller(CreateSellerDto request) {
        // validation
        if (isUsernameExists(request.username())) { // username 중복 확인
            throw new CustomException(ErrorCode.USERNAME_ALEADY_EXISTS);
        } else if (isEmailExists(request.email())) { // email 중복 확인
            throw new CustomException(ErrorCode.EMAIL_ALEADY_EXISTS);
        }
        // TODO 사업자등록번호 검사

        // 비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(request.password());

        User user = User.create(
                request.username(),
                request.nickname(),
                request.email(),
                encryptedPassword,
                request.role()
        );

        userRepository.save(user);

        // 판매자 정보
        SellerInfo sellerInfo = SellerInfo.create(
                user,
                request.businessNumber(),
                request.businessName(),
                request.phoneNumber(),
                request.address(),
                SellerInfoStatus.PENDING
        );

        System.out.println("businessNumber = " + request.businessNumber());
        sellerInfoRepository.save(sellerInfo);

        return UserResponse.of(user);
    }

    // Username 존재 여부 확인
    public Boolean isUsernameExists(String username) {
        // username 으로 User 를 조회 후 isPresent() 로 존재유무를 리턴함
        return userRepository.findByUsernameAndIsDeletedFalse(username).isPresent();
    }

    // Email 존재 여부 확인
    public Boolean isEmailExists(String email) {
        // email 로 User 를 조회 후 isPresent() 로 존재유무를 리턴함
        return userRepository.findByEmailAndIsDeletedFalse(email).isPresent();
    }

}
