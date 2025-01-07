package com.takeit.product.domain.entity;

import com.takeit.common.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_category")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private UUID uuid = UUID.randomUUID();

    @Column(nullable = false, length = 50, unique = true)
    private String name;

    public static Category create(String name) {
        return Category.builder()
                .name(name)
                .build();
    }

    public void restore() {
        this.isDeleted = false;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = null;
    }

    public void update(String name) {
        this.name = name;
    }
}
