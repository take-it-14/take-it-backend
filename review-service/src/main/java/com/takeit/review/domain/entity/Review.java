package com.takeit.review.domain.entity;

import com.takeit.common.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "p_review")
@Builder
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid",unique = true, nullable = false)
    private UUID uuid;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "stars", nullable = false)
    private Integer stars;

    @Column(name = "comment", length = 255)
    private String comment;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id")
    private List<ReviewPhoto> photoList;

}
