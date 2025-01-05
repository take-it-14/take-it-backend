package com.takeit.review.domain.entity;

import com.takeit.common.domain.model.BaseEntity;
import com.takeit.s3.infrastructure.util.dto.S3UploadFile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "p_review_photo")
@Builder
public class ReviewPhoto extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid",unique = true, nullable = false)
    private UUID uuid;

    @JoinColumn(name = "review_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "uri", length = 255)
    private String uri;

    @Column(name = "is_s3_deleted")
    private boolean isS3Deleted;

    public static ReviewPhoto of(S3UploadFile file, Review review, Long userId) {
        return ReviewPhoto.builder()
                .uuid(UUID.randomUUID())
                .review(review)
                .fileName(file.filename())
                .uri(file.uri())
                .build();
    }
}
