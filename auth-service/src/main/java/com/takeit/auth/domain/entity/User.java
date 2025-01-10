package com.takeit.auth.domain.entity;

import com.takeit.common.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_user")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10, updatable = false)
    private String username;

    @Column(nullable = false, length = 100)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    // 유저 생성 메서드
    public static User create(
            String username,
            String nickname,
            String email,
            String encryptedPassword,
            UserRole role
    ) {
        return User.builder()
                .username(username)
                .nickname(nickname)
                .email(email)
                .password(encryptedPassword)
                .role(role)
                .build();
    }

    // 유저 정보 수정 메서드
    public void update(
            String nickname,
            String email,
            String password,
            UserRole role
    ) {
        if (nickname != null) this.nickname = nickname;
        if (email != null) this.email = email;
        if (password != null) this.password = password;
        if (role != null) this.role = role;
    }

    // 유저 삭제 메서드
    public void delete(String deletedBy) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
        this.isDeleted = true;
    }
}
