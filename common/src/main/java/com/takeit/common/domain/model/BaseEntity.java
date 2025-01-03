package com.takeit.common.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    // TODO: 테스트를 위해 nullable 설정 보류 AuditionEntityListener 활성화 하면 적용
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(name = "updated_by")
    protected String updatedBy;

    @Column(name = "deleted_at")
    protected LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    protected String deletedBy;

    @Column(name = "is_deleted")
    protected Boolean isDeleted;

    @PrePersist
    protected void onCreate() {
        if (this.isDeleted == null) {
            this.isDeleted = false;
        }
    }
}
