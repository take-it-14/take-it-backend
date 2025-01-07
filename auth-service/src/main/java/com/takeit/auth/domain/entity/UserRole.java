package com.takeit.auth.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    MASTER("MASTER", "마스터 관리자"),
    MANAGER("MANAGER", "관리자"),
    SELLER("SELLER", "판매자"),
    CUSTOMER("CUSTOMER", "일반 회원 (소비자)");

    private final String roleName;
    private final String description;

    public static UserRole from(String roleName) {
        for (UserRole role : UserRole.values()) {
            if (role.roleName.equals(roleName)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid roleName: " + roleName);
    }

    // 본인 정보만 조회 가능하도록 역할 제한
    public boolean isRestrictedRole() {
        return this == SELLER || this == CUSTOMER;
    }
}
