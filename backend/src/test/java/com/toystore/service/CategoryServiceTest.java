package com.toystore.service;

import com.toystore.dto.request.CategoryRequest;
import com.toystore.dto.response.CategoryResponse;
import com.toystore.entity.Category;
import com.toystore.exception.BadRequestException;
import com.toystore.exception.DuplicateResourceException;
import com.toystore.exception.ResourceNotFoundException;
import com.toystore.mapper.CategoryMapper;
import com.toystore.repository.CategoryRepository;
import com.toystore.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category rootCategory;
    private Category childCategory;
    private CategoryResponse rootResponse;
    private CategoryResponse childResponse;

    @BeforeEach
    void setUp() {
        rootCategory = Category.builder()
                .name("Root Category")
                .slug("root-category")
                .description("Root Desc")
                .isActive(true)
                .children(new ArrayList<>())
                .build();
        rootCategory.setId(1L);

        childCategory = Category.builder()
                .name("Child Category")
                .slug("child-category")
                .description("Child Desc")
                .parent(rootCategory)
                .isActive(true)
                .children(new ArrayList<>())
                .build();
        childCategory.setId(2L);
        rootCategory.getChildren().add(childCategory);

        rootResponse = CategoryResponse.builder()
                .id(1L)
                .name("Root Category")
                .slug("root-category")
                .description("Root Desc")
                .isActive(true)
                .children(new ArrayList<>())
                .build();

        childResponse = CategoryResponse.builder()
                .id(2L)
                .name("Child Category")
                .slug("child-category")
                .description("Child Desc")
                .parentId(1L)
                .parentName("Root Category")
                .isActive(true)
                .children(new ArrayList<>())
                .build();
    }

    @Test
    void getAllCategories_ShouldReturnRootCategoriesWithChildren() {
        when(categoryRepository.findByParentIsNullAndIsActiveTrue()).thenReturn(List.of(rootCategory));
        when(categoryMapper.toResponse(rootCategory)).thenReturn(rootResponse);
        when(categoryMapper.toResponse(childCategory)).thenReturn(childResponse);

        List<CategoryResponse> result = categoryService.getAllCategories(true);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Root Category", result.get(0).getName());
        assertEquals(1, result.get(0).getChildren().size());
        assertEquals("Child Category", result.get(0).getChildren().get(0).getName());
        verify(categoryRepository, times(1)).findByParentIsNullAndIsActiveTrue();
    }

    @Test
    void getCategoryById_WhenFound_ShouldReturnResponse() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(rootCategory));
        when(categoryMapper.toResponse(rootCategory)).thenReturn(rootResponse);
        when(categoryMapper.toResponse(childCategory)).thenReturn(childResponse);

        CategoryResponse result = categoryService.getCategoryById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Root Category", result.getName());
    }

    @Test
    void getCategoryById_WhenNotFound_ShouldThrowException() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.getCategoryById(99L));
    }

    @Test
    void createCategory_WhenValid_ShouldSaveAndReturnResponse() {
        CategoryRequest request = CategoryRequest.builder()
                .name("New Category")
                .description("New Desc")
                .isActive(true)
                .build();

        Category savedEntity = Category.builder()
                .name("New Category")
                .slug("new-category")
                .description("New Desc")
                .isActive(true)
                .children(new ArrayList<>())
                .build();
        savedEntity.setId(3L);

        CategoryResponse response = CategoryResponse.builder()
                .id(3L)
                .name("New Category")
                .slug("new-category")
                .description("New Desc")
                .isActive(true)
                .children(new ArrayList<>())
                .build();

        when(categoryRepository.existsByName(request.getName())).thenReturn(false);
        when(categoryRepository.existsBySlug("new-category")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(savedEntity);
        when(categoryMapper.toResponse(savedEntity)).thenReturn(response);

        CategoryResponse result = categoryService.createCategory(request);

        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("New Category", result.getName());
        assertEquals("new-category", result.getSlug());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void createCategory_WhenDuplicateName_ShouldThrowException() {
        CategoryRequest request = CategoryRequest.builder()
                .name("Root Category")
                .build();

        when(categoryRepository.existsByName(request.getName())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> categoryService.createCategory(request));
    }

    @Test
    void updateCategory_WhenSelfParentCycle_ShouldThrowException() {
        CategoryRequest request = CategoryRequest.builder()
                .name("Root Category")
                .parentId(1L) // self parent
                .build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(rootCategory));

        assertThrows(BadRequestException.class, () -> categoryService.updateCategory(1L, request));
    }

    @Test
    void updateCategory_WhenDescendantCycle_ShouldThrowException() {
        CategoryRequest request = CategoryRequest.builder()
                .name("Root Category")
                .parentId(2L) // 2L is a child of 1L
                .build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(rootCategory));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(childCategory));

        assertThrows(BadRequestException.class, () -> categoryService.updateCategory(1L, request));
    }

    @Test
    void deleteCategory_ShouldCascadeDeactivate() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(rootCategory));

        categoryService.deleteCategory(1L);

        assertFalse(rootCategory.getIsActive());
        assertFalse(childCategory.getIsActive());
        verify(categoryRepository, times(2)).save(any(Category.class));
    }
}
