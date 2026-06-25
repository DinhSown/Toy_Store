package com.toystore.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity đại diện cho đánh giá sản phẩm.
 * Ràng buộc: 1 user chỉ đánh giá 1 sản phẩm 1 lần (unique constraint ở DB).
 * Rating từ 1 đến 5 sao.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "reviews",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_review_user_product",
        columnNames = {"user_id", "product_id"}
    )
)
public class Review extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer rating;  // 1 → 5

    @Column(columnDefinition = "TEXT")
    private String comment;
}
