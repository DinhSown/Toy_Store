package com.toystore.service;

import com.toystore.dto.request.ProductRequest;
import com.toystore.dto.response.PageResponse;
import com.toystore.dto.response.ProductResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    // Lấy danh sách sản phẩm phân trang và lọc theo danh mục, hãng, khoảng giá
    PageResponse<ProductResponse> getAllProducts(
            Long categoryId,
            String brand,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean onlyActive,
            Pageable pageable
    );

    // Lấy chi tiết sản phẩm theo ID
    ProductResponse getProductById(Long id);

    // Lấy chi tiết sản phẩm theo Slug
    ProductResponse getProductBySlug(String slug);

    // Tìm kiếm sản phẩm theo từ khóa (phân trang)
    PageResponse<ProductResponse> searchProducts(String keyword, Pageable pageable);

    // Lấy danh sách sản phẩm nổi bật
    List<ProductResponse> getFeaturedProducts();

    // Thêm mới sản phẩm
    ProductResponse createProduct(ProductRequest request);

    // Cập nhật sản phẩm
    ProductResponse updateProduct(Long id, ProductRequest request);

    // Xóa mềm sản phẩm
    void deleteProduct(Long id);
}
