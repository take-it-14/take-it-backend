package com.takeit.product.domain.entity;

import com.takeit.common.domain.model.BaseEntity;
import com.takeit.s3.infrastructure.util.dto.S3UploadFile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "p_product_photo")
@Builder
public class ProductPhoto extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid",unique = true, nullable = false)
    private UUID uuid;

    @JoinColumn(name = "product_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "uri", length = 255)
    private String uri;

    @Column(name = "is_s3_deleted")
    private boolean isS3Deleted;

    public static ProductPhoto create(S3UploadFile file, Product product) {
        return ProductPhoto.builder()
                .uuid(UUID.randomUUID())
                .product(product)
                .fileName(file.filename())
                .uri(file.uri())
                .build();
    }

    public void delete(String username) {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = username;
    }
}
