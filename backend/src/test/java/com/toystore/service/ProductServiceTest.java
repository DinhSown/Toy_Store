package com.toystore.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toystore.dto.request.ProductRequest;
import com.toystore.dto.response.PageResponse;
import com.toystore.dto.response.ProductResponse;
import com.toystore.entity.Category;
import com.toystore.entity.Product;
import com.toystore.exception.BadRequestException;
import com.toystore.exception.DuplicateResourceException;
import com.toystore.exception.ResourceNotFoundException;
import com.toystore.mapper.ProductMapper;
import com.toystore.mapper.ProductMapperImpl;
import com.toystore.repository.CategoryRepository;
import com.toystore.repository.ProductRepository;
import com.toystore.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ProductMapper productMapper;
    private ProductServiceImpl productService;

    private Category category;
    private Category childCategory;
    private Product product;
    private ProductRequest productRequest;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        categoryRepository = mock(CategoryRepository.class);

        // Khởi tạo Mapper thật với ObjectMapper thật
        ProductMapperImpl mapperImpl = new ProductMapperImpl();
        mapperImpl.objectMapper = new ObjectMapper();
        productMapper = mapperImpl;

        productService = new ProductServiceImpl(productRepository, categoryRepository, productMapper);

        category = Category.builder()
                .name("Gundam")
                .slug("gundam")
                .isActive(true)
                .children(new ArrayList<>())
                .build();
        category.setId(1L);

        childCategory = Category.builder()
                .name("Gundam RG")
                .slug("gundam-rg")
                .parent(category)
                .isActive(true)
                .children(new ArrayList<>())
                .build();
        childCategory.setId(2L);
        category.getChildren().add(childCategory);

        product = Product.builder()
                .name("Gundam RX-78-2")
                .slug("gundam-rx-78-2")
                .price(BigDecimal.valueOf(500000))
                .salePrice(BigDecimal.valueOf(450000))
                .stock(10)
                .brand("Bandai")
                .category(category)
                .isActive(true)
                .isFeatured(true)
                .build();
        product.setId(1L);

        productRequest = ProductRequest.builder()
                .name("Gundam RX-78-2")
                .price(BigDecimal.valueOf(500000))
                .salePrice(BigDecimal.valueOf(450000))
                .stock(10)
                .brand("Bandai")
                .categoryId(1L)
                .isActive(true)
                .isFeatured(true)
                .build();
    }

    @Test
    void getAllProducts_WithCategory_ShouldResolveDescendantsAndFilter() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(product));
        
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.filterProducts(
                argThat(list -> list.contains(1L) && list.contains(2L)),
                eq("Bandai"),
                eq(BigDecimal.valueOf(100000)),
                eq(BigDecimal.valueOf(600000)),
                eq(true),
                eq(pageable)
        )).thenReturn(page);

        PageResponse<ProductResponse> result = productService.getAllProducts(
                1L, "Bandai", BigDecimal.valueOf(100000), BigDecimal.valueOf(600000), true, pageable
        );

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Gundam RX-78-2", result.getContent().get(0).getName());
        assertEquals("Gundam", result.getContent().get(0).getCategoryName());
        verify(categoryRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).filterProducts(any(), any(), any(), any(), any(), any());
    }

    @Test
    void getAllProducts_WithInvalidCategory_ShouldThrowException() {
        Pageable pageable = PageRequest.of(0, 10);
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getAllProducts(
                99L, "Bandai", null, null, true, pageable
        ));
    }

    @Test
    void getProductById_WhenFound_ShouldReturnResponse() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Gundam RX-78-2", result.getName());
    }

    @Test
    void getProductById_WhenNotFound_ShouldThrowException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(99L));
    }

    @Test
    void getProductBySlug_WhenFoundAndActive_ShouldReturnResponse() {
        when(productRepository.findBySlugAndIsActiveTrue("gundam-rx-78-2")).thenReturn(Optional.of(product));

        ProductResponse result = productService.getProductBySlug("gundam-rx-78-2");

        assertNotNull(result);
        assertEquals("gundam-rx-78-2", result.getSlug());
    }

    @Test
    void getProductBySlug_WhenNotFound_ShouldThrowException() {
        when(productRepository.findBySlugAndIsActiveTrue("non-existent")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getProductBySlug("non-existent"));
    }

    @Test
    void searchProducts_WhenKeywordIsEmpty_ShouldReturnEmptyPage() {
        Pageable pageable = PageRequest.of(0, 10);
        PageResponse<ProductResponse> result = productService.searchProducts("", pageable);
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void searchProducts_WhenKeywordValid_ShouldReturnResults() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(product));
        when(productRepository.searchProducts("Gundam", pageable)).thenReturn(page);

        PageResponse<ProductResponse> result = productService.searchProducts("Gundam", pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Gundam RX-78-2", result.getContent().get(0).getName());
    }

    @Test
    void getFeaturedProducts_ShouldReturnList() {
        Page<Product> page = new PageImpl<>(List.of(product));
        when(productRepository.findByIsFeaturedTrueAndIsActiveTrue(any(Pageable.class))).thenReturn(page);

        List<ProductResponse> result = productService.getFeaturedProducts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Gundam RX-78-2", result.get(0).getName());
    }

    @Test
    void createProduct_WhenValid_ShouldSaveAndReturnResponse() {
        when(productRepository.existsByName(productRequest.getName())).thenReturn(false);
        when(productRepository.existsBySlug("gundam-rx-78-2")).thenReturn(false);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse result = productService.createProduct(productRequest);

        assertNotNull(result);
        assertEquals("Gundam RX-78-2", result.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void createProduct_WhenDuplicateName_ShouldThrowException() {
        when(productRepository.existsByName(productRequest.getName())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> productService.createProduct(productRequest));
    }

    @Test
    void createProduct_WhenSalePriceHigherThanPrice_ShouldThrowException() {
        productRequest.setSalePrice(BigDecimal.valueOf(600000)); // Price is 500000

        when(productRepository.existsByName(productRequest.getName())).thenReturn(false);
        when(productRepository.existsBySlug("gundam-rx-78-2")).thenReturn(false);

        assertThrows(BadRequestException.class, () -> productService.createProduct(productRequest));
    }

    @Test
    void updateProduct_WhenValid_ShouldUpdateAndReturnResponse() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.existsByName(productRequest.getName())).thenReturn(false);
        when(productRepository.existsBySlug("gundam-rx-78-2")).thenReturn(false);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse result = productService.updateProduct(1L, productRequest);

        assertNotNull(result);
        assertEquals("Gundam RX-78-2", result.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void deleteProduct_ShouldSoftDelete() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.deleteProduct(1L);

        assertFalse(product.getIsActive());
        verify(productRepository, times(1)).save(any(Product.class));
    }
}
