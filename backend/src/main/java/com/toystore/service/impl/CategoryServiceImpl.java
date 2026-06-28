package com.toystore.service.impl;

import com.toystore.dto.request.CategoryRequest;
import com.toystore.dto.response.CategoryResponse;
import com.toystore.entity.Category;
import com.toystore.exception.BadRequestException;
import com.toystore.exception.DuplicateResourceException;
import com.toystore.exception.ResourceNotFoundException;
import com.toystore.mapper.CategoryMapper;
import com.toystore.repository.CategoryRepository;
import com.toystore.service.CategoryService;
import com.toystore.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories(boolean onlyActive) {
        List<Category> rootCategories = onlyActive 
                ? categoryRepository.findByParentIsNullAndIsActiveTrue() 
                : categoryRepository.findByParentIsNull();

        return rootCategories.stream()
                .map(category -> mapToResponse(category, onlyActive))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Danh mục với ID " + id + " không tồn tại."));
        return mapToResponse(category, false);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Danh mục với slug '" + slug + "' không tồn tại."));
        return mapToResponse(category, false);
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Tên danh mục '" + request.getName() + "' đã tồn tại.");
        }

        String slug = SlugUtil.toSlug(request.getName());
        if (categoryRepository.existsBySlug(slug)) {
            throw new DuplicateResourceException("Slug '" + slug + "' đã tồn tại.");
        }

        Category parent = null;
        if (request.getParentId() != null) {
            parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Danh mục cha với ID " + request.getParentId() + " không tồn tại."));
        }

        Category category = Category.builder()
                .name(request.getName())
                .slug(slug)
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .parent(parent)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        Category savedCategory = categoryRepository.save(category);
        return mapToResponse(savedCategory, false);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Danh mục với ID " + id + " không tồn tại."));

        // Kiểm tra trùng tên
        if (!category.getName().equalsIgnoreCase(request.getName()) && 
                categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Tên danh mục '" + request.getName() + "' đã tồn tại.");
        }

        // Tạo và kiểm tra trùng slug
        String slug = SlugUtil.toSlug(request.getName());
        if (!category.getSlug().equals(slug) && categoryRepository.existsBySlug(slug)) {
            throw new DuplicateResourceException("Slug '" + slug + "' đã tồn tại.");
        }

        // Xử lý danh mục cha
        if (request.getParentId() != null) {
            if (request.getParentId().equals(id)) {
                throw new BadRequestException("Danh mục không thể làm danh mục cha của chính nó.");
            }
            // Tránh chu kỳ: kiểm tra xem danh mục cha mới có phải là con cháu của danh mục hiện tại không
            Category newParent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Danh mục cha với ID " + request.getParentId() + " không tồn tại."));
            
            if (isDescendantOf(newParent, category)) {
                throw new BadRequestException("Không thể chọn danh mục con cháu làm danh mục cha (tránh vòng lặp tuần hoàn).");
            }
            category.setParent(newParent);
        } else {
            category.setParent(null);
        }

        category.setName(request.getName());
        category.setSlug(slug);
        category.setDescription(request.getDescription());
        category.setImageUrl(request.getImageUrl());
        if (request.getIsActive() != null) {
            category.setIsActive(request.getIsActive());
        }

        Category updatedCategory = categoryRepository.save(category);
        return mapToResponse(updatedCategory, false);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Danh mục với ID " + id + " không tồn tại."));
        
        // Đệ quy hủy kích hoạt danh mục này và tất cả con cháu của nó (soft delete)
        deactivateCategoryAndChildren(category);
    }

    // Helper đệ quy map từ Entity sang Response DTO và lọc các con cháu đang hoạt động nếu được yêu cầu
    private CategoryResponse mapToResponse(Category category, boolean onlyActive) {
        if (category == null) {
            return null;
        }

        CategoryResponse response = categoryMapper.toResponse(category);

        List<CategoryResponse> activeChildren = category.getChildren().stream()
                .filter(child -> !onlyActive || child.getIsActive())
                .map(child -> mapToResponse(child, onlyActive))
                .toList();

        response.setChildren(activeChildren);
        return response;
    }

    // Helper đệ quy kiểm tra xem node A có phải là con cháu của node B hay không
    private boolean isDescendantOf(Category target, Category ancestor) {
        if (target == null || ancestor == null) {
            return false;
        }
        if (target.getParent() == null) {
            return false;
        }
        if (target.getParent().getId().equals(ancestor.getId())) {
            return true;
        }
        return isDescendantOf(target.getParent(), ancestor);
    }

    // Helper đệ quy hủy kích hoạt danh mục và toàn bộ con cháu của nó
    private void deactivateCategoryAndChildren(Category category) {
        category.setIsActive(false);
        categoryRepository.save(category); // Lưu trạng thái hiện tại
        for (Category child : category.getChildren()) {
            deactivateCategoryAndChildren(child);
        }
    }
}
