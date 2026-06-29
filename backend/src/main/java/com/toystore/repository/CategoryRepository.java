package com.toystore.repository;

import com.toystore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Lấy tất cả danh mục gốc (không có parent)
    List<Category> findByParentIsNull();

    // Lấy tất cả danh mục gốc đang hoạt động
    List<Category> findByParentIsNullAndIsActiveTrue();

    // Tìm danh mục theo slug
    Optional<Category> findBySlug(String slug);

    // Tìm danh mục hoạt động theo slug
    Optional<Category> findBySlugAndIsActiveTrue(String slug);

    // Kiểm tra trùng lặp slug
    boolean existsBySlug(String slug);

    // Kiểm tra trùng lặp tên danh mục
    boolean existsByName(String name);
}
