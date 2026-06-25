package com.toystore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity đại diện cho sản phẩm đồ chơi mô hình.
 * Giá lưu dạng BigDecimal (VNĐ, không dùng cent).
 * Danh sách ảnh phụ lưu dạng JSON string.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, unique = true, length = 300)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Giá gốc (VNĐ)
    @Column(nullable = false, precision = 15, scale = 0)
    private BigDecimal price;

    // Giá khuyến mãi (null = không giảm giá)
    @Column(name = "sale_price", precision = 15, scale = 0)
    private BigDecimal salePrice;

    @Column(nullable = false)
    @Builder.Default
    private Integer stock = 0;

    @Column(length = 100)
    private String brand;

    // Ảnh đại diện chính
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    // Danh sách URL ảnh phụ, lưu JSON: ["url1","url2",...]
    @Column(columnDefinition = "JSON")
    private String images;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "is_featured", nullable = false)
    @Builder.Default
    private Boolean isFeatured = false;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    /**
     * Trả về giá hiển thị: salePrice nếu có, ngược lại price.
     */
    @Transient
    public BigDecimal getEffectivePrice() {
        return (salePrice != null) ? salePrice : price;
    }
}
