package com.toystore.controller.admin;

import com.toystore.dto.request.ProductRequest;
import com.toystore.dto.response.ApiResponse;
import com.toystore.dto.response.PageResponse;
import com.toystore.dto.response.ProductResponse;
import com.toystore.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * Controller quản lý sản phẩm dành cho Admin.
 * Base path: /api/admin/products
 * Yêu cầu quyền ROLE_ADMIN (cấu hình ở SecurityConfig).
 */
@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    /**
     * Lấy toàn bộ sản phẩm (kể cả đã ẩn) với phân trang, sắp xếp và lọc.
     * Admin có thể xem cả sản phẩm isActive = false.
     * GET /api/admin/products?page=0&size=20&sort=createdAt,desc&categoryId=1&brand=Bandai&minPrice=0&maxPrice=1000000&onlyActive=false
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getAllProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean onlyActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {

        Pageable pageable = buildPageable(page, size, sort);
        PageResponse<ProductResponse> result = productService.getAllProducts(
                categoryId, brand, minPrice, maxPrice, onlyActive, pageable);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách sản phẩm thành công", result));
    }

    /**
     * Lấy chi tiết sản phẩm theo ID (bao gồm cả sản phẩm đã ẩn).
     * GET /api/admin/products/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success("Lấy chi tiết sản phẩm thành công", product));
    }

    /**
     * Tạo sản phẩm mới.
     * POST /api/admin/products
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductRequest request) {
        ProductResponse created = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo sản phẩm thành công", created));
    }

    /**
     * Cập nhật thông tin sản phẩm.
     * PUT /api/admin/products/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        ProductResponse updated = productService.updateProduct(id, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật sản phẩm thành công", updated));
    }

    /**
     * Xóa mềm sản phẩm (chuyển isActive = false, không xóa vật lý).
     * DELETE /api/admin/products/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa sản phẩm thành công (Soft Delete)"));
    }

    /**
     * Bật / tắt trạng thái hiển thị sản phẩm.
     * PATCH /api/admin/products/{id}/toggle-active
     */
    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<ApiResponse<ProductResponse>> toggleActive(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        // Đảo trạng thái isActive
        ProductRequest toggleRequest = buildToggleActiveRequest(product);
        ProductResponse updated = productService.updateProduct(id, toggleRequest);
        String msg = Boolean.TRUE.equals(updated.getIsActive())
                ? "Đã hiển thị sản phẩm"
                : "Đã ẩn sản phẩm";
        return ResponseEntity.ok(ApiResponse.success(msg, updated));
    }

    /**
     * Bật / tắt trạng thái nổi bật của sản phẩm.
     * PATCH /api/admin/products/{id}/toggle-featured
     */
    @PatchMapping("/{id}/toggle-featured")
    public ResponseEntity<ApiResponse<ProductResponse>> toggleFeatured(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        // Đảo trạng thái isFeatured
        ProductRequest toggleRequest = buildToggleFeaturedRequest(product);
        ProductResponse updated = productService.updateProduct(id, toggleRequest);
        String msg = Boolean.TRUE.equals(updated.getIsFeatured())
                ? "Đã đánh dấu sản phẩm nổi bật"
                : "Đã bỏ đánh dấu sản phẩm nổi bật";
        return ResponseEntity.ok(ApiResponse.success(msg, updated));
    }

    // ─────────────── Private helpers ───────────────

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

    /**
     * Tạo ProductRequest từ ProductResponse với isActive bị đảo.
     */
    private ProductRequest buildToggleActiveRequest(ProductResponse p) {
        return ProductRequest.builder()
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .salePrice(p.getSalePrice())
                .stock(p.getStock())
                .brand(p.getBrand())
                .imageUrl(p.getImageUrl())
                .images(p.getImages())
                .categoryId(p.getCategoryId())
                .isActive(!Boolean.TRUE.equals(p.getIsActive()))
                .isFeatured(p.getIsFeatured())
                .build();
    }

    /**
     * Tạo ProductRequest từ ProductResponse với isFeatured bị đảo.
     */
    private ProductRequest buildToggleFeaturedRequest(ProductResponse p) {
        return ProductRequest.builder()
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .salePrice(p.getSalePrice())
                .stock(p.getStock())
                .brand(p.getBrand())
                .imageUrl(p.getImageUrl())
                .images(p.getImages())
                .categoryId(p.getCategoryId())
                .isActive(p.getIsActive())
                .isFeatured(!Boolean.TRUE.equals(p.getIsFeatured()))
                .build();
    }
}
