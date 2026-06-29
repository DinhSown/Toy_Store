package com.toystore.repository;

import com.toystore.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Tìm sản phẩm hoạt động theo slug
    Optional<Product> findBySlugAndIsActiveTrue(String slug);

    // Tìm sản phẩm theo slug
    Optional<Product> findBySlug(String slug);

    // Kiểm tra trùng lặp slug
    boolean existsBySlug(String slug);

    // Kiểm tra trùng lặp tên sản phẩm
    boolean existsByName(String name);

    // Lấy danh sách sản phẩm nổi bật đang hoạt động
    Page<Product> findByIsFeaturedTrueAndIsActiveTrue(Pageable pageable);

    // Tìm kiếm sản phẩm theo từ khóa (LIKE trên tên, mô tả, thương hiệu)
    @Query("SELECT p FROM Product p WHERE " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "(p.description IS NOT NULL AND LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
           "(p.brand IS NOT NULL AND LOWER(p.brand) LIKE LOWER(CONCAT('%', :keyword, '%')))) AND " +
           "(p.isActive = true)")
    Page<Product> searchProducts(@Param("keyword") String keyword, Pageable pageable);

    // Lọc sản phẩm nâng cao (theo danh sách danh mục, thương hiệu, khoảng giá và trạng thái)
    @Query("SELECT p FROM Product p WHERE " +
           "(:categoryIds IS NULL OR p.category.id IN :categoryIds) AND " +
           "(:brand IS NULL OR p.brand = :brand) AND " +
           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
           "(:isActive IS NULL OR p.isActive = :isActive)")
    Page<Product> filterProducts(
            @Param("categoryIds") List<Long> categoryIds,
            @Param("brand") String brand,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("isActive") Boolean isActive,
            Pageable pageable);
}
