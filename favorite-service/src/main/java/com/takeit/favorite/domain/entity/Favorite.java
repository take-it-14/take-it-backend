package com.takeit.favorite.domain.entity;

import com.takeit.common.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@Entity
@Table(name = "p_favorite")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @Builder.Default
    private UUID uuid = UUID.randomUUID();

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "productId", nullable = false)
    private Long productId;

}