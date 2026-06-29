package com.toystore.service.impl;

import com.toystore.dto.request.ProductRequest;
import com.toystore.dto.response.PageResponse;
import com.toystore.dto.response.ProductResponse;
import com.toystore.entity.Category;
import com.toystore.entity.Product;
import com.toystore.exception.BadRequestException;
import com.toystore.exception.DuplicateResourceException;
import com.toystore.exception.ResourceNotFoundException;
import com.toystore.mapper.ProductMapper;
import com.toystore.repository.CategoryRepository;
import com.toystore.repository.ProductRepository;
import com.toystore.service.ProductService;
import com.toystore.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getAllProducts(
            Long categoryId,
            String brand,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean onlyActive,
            Pageable pageable) {

        List<Long> categoryIds = null;
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Danh mục với ID " + categoryId + " không tồn tại."));
            categoryIds = new ArrayList<>();
            collectCategoryIds(category, categoryIds, onlyActive != null && onlyActive);
            if (categoryIds.isEmpty()) {
                // Nếu không tìm thấy danh mục con nào hợp lệ (ví dụ: chỉ lấy active mà cha bị inactive)
                categoryIds = null;
            }
        }

        String brandQuery = (brand != null && !brand.trim().isEmpty()) ? brand.trim() : null;
        Boolean activeFilter = (onlyActive != null && onlyActive) ? true : null;

        Page<Product> productPage = productRepository.filterProducts(
                categoryIds,
                brandQuery,
                minPrice,
                maxPrice,
                activeFilter,
                pageable
        );

        return PageResponse.of(productPage.map(productMapper::toResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sản phẩm với ID " + id + " không tồn tại."));
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductBySlug(String slug) {
        Product product = productRepository.findBySlugAndIsActiveTrue(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Sản phẩm với slug '" + slug + "' không tồn tại hoặc đã bị ẩn."));
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> searchProducts(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            Page<Product> emptyPage = Page.empty(pageable);
            return PageResponse.of(emptyPage.map(productMapper::toResponse));
        }
        Page<Product> productPage = productRepository.searchProducts(keyword.trim(), pageable);
        return PageResponse.of(productPage.map(productMapper::toResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getFeaturedProducts() {
        // Lấy tối đa 12 sản phẩm nổi bật mới nhất
        Pageable limit = PageRequest.of(0, 12, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Product> productPage = productRepository.findByIsFeaturedTrueAndIsActiveTrue(limit);
        return productPage.getContent().stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Tên sản phẩm '" + request.getName() + "' đã tồn tại.");
        }

        String slug = SlugUtil.toSlug(request.getName());
        if (productRepository.existsBySlug(slug)) {
            throw new DuplicateResourceException("Slug sản phẩm '" + slug + "' đã tồn tại.");
        }

        if (request.getSalePrice() != null && request.getPrice() != null && request.getSalePrice().compareTo(request.getPrice()) > 0) {
            throw new BadRequestException("Giá khuyến mãi không được lớn hơn giá gốc.");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Danh mục với ID " + request.getCategoryId() + " không tồn tại."));

        Product product = productMapper.toEntity(request);
        product.setSlug(slug);
        product.setCategory(category);

        if (product.getIsActive() == null) {
            product.setIsActive(true);
        }
        if (product.getIsFeatured() == null) {
            product.setIsFeatured(false);
        }

        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sản phẩm với ID " + id + " không tồn tại."));

        if (!product.getName().equalsIgnoreCase(request.getName()) && productRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Tên sản phẩm '" + request.getName() + "' đã tồn tại.");
        }

        String slug = SlugUtil.toSlug(request.getName());
        if (!product.getSlug().equals(slug) && productRepository.existsBySlug(slug)) {
            throw new DuplicateResourceException("Slug sản phẩm '" + slug + "' đã tồn tại.");
        }

        if (request.getSalePrice() != null && request.getPrice() != null && request.getSalePrice().compareTo(request.getPrice()) > 0) {
            throw new BadRequestException("Giá khuyến mãi không được lớn hơn giá gốc.");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Danh mục với ID " + request.getCategoryId() + " không tồn tại."));

        product.setName(request.getName());
        product.setSlug(slug);
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setSalePrice(request.getSalePrice());
        product.setStock(request.getStock());
        product.setBrand(request.getBrand());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);

        if (request.getImages() != null) {
            product.setImages(productMapper.listToJson(request.getImages()));
        }

        if (request.getIsActive() != null) {
            product.setIsActive(request.getIsActive());
        }
        if (request.getIsFeatured() != null) {
            product.setIsFeatured(request.getIsFeatured());
        }

        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sản phẩm với ID " + id + " không tồn tại."));
        product.setIsActive(false);
        productRepository.save(product);
    }

    // Helper đệ quy thu thập tất cả ID của danh mục con cháu
    private void collectCategoryIds(Category category, List<Long> ids, boolean onlyActive) {
        if (category == null) {
            return;
        }
        if (onlyActive && !category.getIsActive()) {
            return;
        }
        ids.add(category.getId());
        if (category.getChildren() != null) {
            for (Category child : category.getChildren()) {
                collectCategoryIds(child, ids, onlyActive);
            }
        }
    }
}
