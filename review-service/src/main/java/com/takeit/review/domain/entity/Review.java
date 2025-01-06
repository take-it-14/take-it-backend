package com.takeit.review.domain.entity;

import com.takeit.common.domain.model.BaseEntity;
import com.takeit.review.presentation.request.CreateReviewRequest;
import com.takeit.review.presentation.request.UpdateReviewRequest;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
@Table(name = "p_review")
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "review")
    @Builder.Default
    private List<ReviewPhoto> photoList = new ArrayList<>();

    public static Review of(CreateReviewRequest request, String username, Long productId) {
        return Review.builder()
               .uuid(UUID.randomUUID())
               .productId(productId)
               .stars(request.stars())
               .comment(request.comment())
               .build();
    }

    public void addPhotos(List<ReviewPhoto> reviewPhotos) {
        photoList = new ArrayList<>(reviewPhotos);
    }

    public void updateReview(UpdateReviewRequest request) {
        this.stars = request.stars();
        this.comment = request.comment();
    }

    public void deleted(String username) {
        this.isDeleted = true;
        this.deletedBy = username;
        this.deletedAt = LocalDateTime.now();
    }
}
