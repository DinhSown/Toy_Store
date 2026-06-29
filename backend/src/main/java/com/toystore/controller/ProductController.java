package com.toystore.controller;

import com.toystore.dto.response.ApiResponse;
import com.toystore.dto.response.PageResponse;
import com.toystore.dto.response.ProductResponse;
import com.toystore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controller cung cấp các API public cho sản phẩm.
 * Không yêu cầu xác thực (public endpoints).
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Lấy danh sách sản phẩm có phân trang, sắp xếp và lọc.
     * Ví dụ: GET /api/products?page=0&size=12&sort=createdAt,desc&categoryId=1&brand=Bandai&minPrice=100000&maxPrice=500000
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getAllProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {

        Pageable pageable = buildPageable(page, size, sort);
        PageResponse<ProductResponse> result = productService.getAllProducts(
                categoryId, brand, minPrice, maxPrice, true, pageable);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách sản phẩm thành công", result));
    }

    /**
     * Lấy chi tiết sản phẩm theo ID.
     * GET /api/products/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success("Lấy chi tiết sản phẩm thành công", product));
    }

    /**
     * Lấy chi tiết sản phẩm theo slug (SEO-friendly URL).
     * GET /api/products/slug/{slug}
     */
    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductBySlug(@PathVariable String slug) {
        ProductResponse product = productService.getProductBySlug(slug);
        return ResponseEntity.ok(ApiResponse.success("Lấy sản phẩm theo slug thành công", product));
    }

    /**
     * Tìm kiếm sản phẩm theo từ khóa (tìm trong tên, mô tả, thương hiệu).
     * GET /api/products/search?keyword=gundam&page=0&size=12
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        PageResponse<ProductResponse> result = productService.searchProducts(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success("Tìm kiếm sản phẩm thành công", result));
    }

    /**
     * Lấy danh sách sản phẩm nổi bật (tối đa 12 sản phẩm).
     * GET /api/products/featured
     */
    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getFeaturedProducts() {
        List<ProductResponse> products = productService.getFeaturedProducts();
        return ResponseEntity.ok(ApiResponse.success("Lấy sản phẩm nổi bật thành công", products));
    }

    /**
     * Lấy danh sách sản phẩm theo danh mục (bao gồm cả danh mục con).
     * GET /api/products/category/{categoryId}?page=0&size=12
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {

        Pageable pageable = buildPageable(page, size, sort);
        PageResponse<ProductResponse> result = productService.getAllProducts(
                categoryId, null, null, null, true, pageable);
        return ResponseEntity.ok(ApiResponse.success("Lấy sản phẩm theo danh mục thành công", result));
    }

    /**
     * Xây dựng Pageable từ các tham số phân trang và sắp xếp.
     * Format sort: "field,direction" (ví dụ: "createdAt,desc" hoặc "price,asc")
     */
    private Pageable buildPageable(int page, int size, String sort) {
        String[] sortParts = sort.split(",");
        String sortField = sortParts[0].trim();
        Sort.Direction direction = (sortParts.length > 1 && sortParts[1].trim().equalsIgnoreCase("asc"))
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        return PageRequest.of(page, size, Sort.by(direction, sortField));
    }
}
