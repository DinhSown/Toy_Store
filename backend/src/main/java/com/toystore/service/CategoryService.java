package com.toystore.service;

import com.toystore.dto.request.CategoryRequest;
import com.toystore.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    // Lấy tất cả danh mục (dưới dạng cây phân cấp)
    List<CategoryResponse> getAllCategories(boolean onlyActive);

    // Lấy chi tiết danh mục theo ID
    CategoryResponse getCategoryById(Long id);

    // Lấy chi tiết danh mục theo slug
    CategoryResponse getCategoryBySlug(String slug);

    // Thêm mới danh mục
    CategoryResponse createCategory(CategoryRequest request);

    // Cập nhật danh mục
    CategoryResponse updateCategory(Long id, CategoryRequest request);

    // Soft delete danh mục (chuyển isActive sang false)
    void deleteCategory(Long id);
}
