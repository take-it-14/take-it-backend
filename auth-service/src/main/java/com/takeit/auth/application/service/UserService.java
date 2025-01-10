package com.takeit.auth.application.service;

import static com.takeit.common.utils.AccessValidator.isManager;
import static com.takeit.common.utils.AccessValidator.isMaster;
import static com.takeit.common.utils.AccessValidator.isRequesterAuthorized;

import com.querydsl.core.types.Predicate;
import com.takeit.auth.application.dto.CreateSellerDto;
import com.takeit.auth.application.dto.CreateUserDto;
import com.takeit.auth.application.dto.SellerResponse;
import com.takeit.auth.application.dto.UserPageResponse;
import com.takeit.auth.application.dto.UserResponse;
import com.takeit.auth.domain.entity.SellerInfo;
import com.takeit.auth.domain.entity.SellerInfoStatus;
import com.takeit.auth.domain.entity.User;
import com.takeit.auth.domain.repository.SellerInfoRepository;
import com.takeit.auth.domain.repository.UserRepository;
import com.takeit.common.exception.CustomException;
import com.takeit.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final SellerInfoRepository sellerInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    // 사용자 등록
    @Transactional
    public UserResponse createUser(CreateUserDto request) {
        // 중복 체크
        duplicateCheckForUserInfo(request.username(), request.email());

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

        return UserResponse.from(user);
    }

    // 판매자 등록
    @Transactional
    public SellerResponse createSeller(CreateSellerDto request) {
        // 중복 체크
        duplicateCheckForUserInfo(request.username(), request.email());
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

        sellerInfoRepository.save(sellerInfo);

        return SellerResponse.from(sellerInfo);
    }

    // 사용자 단건 조회
    public UserResponse getUserByUsername(String username, String requesterUsername) {
        // 권한 체크
        String requesterUserRole = authService.getUserRoleByUsername(requesterUsername).getRoleName();
        if(!isMaster(requesterUserRole) && !isManager(requesterUserRole) && !isRequesterAuthorized(username, requesterUsername)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        // 사용자 확인
        User user = userRepository.findByUsernameAndIsDeletedFalse(username).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        return UserResponse.from(user);
    }

    // 사용자 목록 조회
    public UserPageResponse getUsers(Predicate predicate, Pageable pageable, String requesterUsername) {
        // 권한 체크
        String requesterUserRole = authService.getUserRoleByUsername(requesterUsername).getRoleName();
        if(!isMaster(requesterUserRole) && !isManager(requesterUserRole)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        Page<User> userPage = userRepository.findAll(predicate, pageable);

        if (userPage.isEmpty()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        return UserPageResponse.from(userPage);
    }

    // 사용자 정보 중복 체크
    public void duplicateCheckForUserInfo(String username, String email) {
        if (isUsernameExists(username)) { // username 중복 확인
            throw new CustomException(ErrorCode.USERNAME_ALREADY_EXISTS);
        } else if (isEmailExists(email)) { // email 중복 확인
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }

    // Username 존재 여부 확인
    private Boolean isUsernameExists(String username) {
        // username 으로 User 를 조회 후 isPresent() 로 존재유무를 리턴함
        return userRepository.findByUsernameAndIsDeletedFalse(username).isPresent();
    }

    // Email 존재 여부 확인
    private Boolean isEmailExists(String email) {
        // email 로 User 를 조회 후 isPresent() 로 존재유무를 리턴함
        return userRepository.findByEmailAndIsDeletedFalse(email).isPresent();
    }

}
