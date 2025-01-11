package com.takeit.auth.application.service;

import static com.takeit.common.utils.AccessValidator.isManager;
import static com.takeit.common.utils.AccessValidator.isMaster;

import com.takeit.auth.application.dto.CreateUserDto;
import com.takeit.auth.application.dto.SellerPageResponse;
import com.takeit.auth.application.dto.SellerResponse;
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
public class AdminService {

    private final UserRepository userRepository;
    private final SellerInfoRepository sellerInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final AuthService authService;

    // 승인 요청된 판매자 목록 조회
    public SellerPageResponse getSellers(Pageable pageable, String requesterUsername) {
        // 권한 체크
        checkAdminAuthority(requesterUsername);

        Page<SellerInfo> sellerPage = sellerInfoRepository.findByStatusAndIsDeletedFalseOrderByIdAsc(SellerInfoStatus.PENDING, pageable);

        if (sellerPage.isEmpty()) {
            throw new CustomException(ErrorCode.SELLERINFO_NOT_FOUND);
        }

        return SellerPageResponse.from(sellerPage);
    }

    // 판매자 상태 승인
    @Transactional
    public SellerResponse approveSeller(Long sellerInfoId, String requesterUsername) {
        // 권한 체크
        checkAdminAuthority(requesterUsername);

        // 판매자 정보 확인
        SellerInfo sellerInfo = sellerInfoRepository.findByIdAndIsDeletedFalse(sellerInfoId).orElseThrow(
                () -> new CustomException(ErrorCode.SELLERINFO_NOT_FOUND)
        );

        sellerInfo.update(null, null, null, null, SellerInfoStatus.APPROVED);

        return SellerResponse.from(sellerInfo);
    }

    // 관리자 등록
    @Transactional
    public UserResponse createManager(CreateUserDto request, String requesterUsername) {
        // 권한 체크
        checkAdminAuthority(requesterUsername);

        // 중복 체크
        userService.duplicateCheckForUserInfo(request.username(), request.email());

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

    // 권한 체크
    private void checkAdminAuthority(String requesterUsername) {
        String requesterUserRole = authService.getUserRoleByUsername(requesterUsername).getRoleName();
        if(!isMaster(requesterUserRole) && !isManager(requesterUserRole)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }
}
