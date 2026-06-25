# ☕ Toy Model Store — Backend API

> REST API server cho website bán đồ chơi mô hình, xây dựng với **Java Spring Boot 3** và **MySQL 8**.
>
> 📊 **Báo cáo tiến độ & Review code:** Xem chi tiết tại [PROGRESS.md](./PROGRESS.md)

---

## 📋 Mục Lục

- [Tech Stack](#-tech-stack)
- [Yêu Cầu Hệ Thống](#-yêu-cầu-hệ-thống)
- [Kiến Trúc](#-kiến-trúc)
- [Database Schema](#-database-schema)
- [API Endpoints](#-api-endpoints)
- [Roadmap](#-roadmap)
- [Cấu Trúc Thư Mục](#-cấu-trúc-thư-mục)
- [Hướng Dẫn Chạy](#-hướng-dẫn-chạy)
- [Testing](#-testing)

---

## 🛠️ Tech Stack

| Công nghệ | Version | Mục đích |
|---|---|---|
| **Java** | 17+ | Ngôn ngữ chính |
| **Spring Boot** | 3.x | Framework REST API |
| **Spring Data JPA** | — | ORM, tương tác database |
| **Spring Security** | — | Xác thực & phân quyền |
| **MySQL** | 8.x | Relational database |
| **JWT (jjwt)** | 0.12.x | Token-based authentication |
| **Flyway** | — | Database migration |
| **Lombok** | — | Giảm boilerplate code |
| **MapStruct** | 1.5.x | Object mapping (Entity ↔ DTO) |
| **Maven** | 3.8+ | Build tool & dependency management |
| **Cloudinary** | — | Lưu trữ hình ảnh sản phẩm |
| **JUnit 5** | — | Unit testing |
| **Mockito** | — | Mocking framework |
| **SpringDoc OpenAPI** | 2.x | Swagger API documentation |

---

## 💻 Yêu Cầu Hệ Thống

- **JDK**: 17 trở lên
- **Maven**: 3.8+
- **MySQL**: 8.0+
- **IDE**: IntelliJ IDEA (khuyến nghị) hoặc VS Code + Extension Pack for Java
- **Postman**: Test API

---

## 🏗️ Kiến Trúc

### Layered Architecture

```
┌──────────────────────────────────────────────────┐
│                   Controller Layer               │
│         (REST endpoints, request validation)     │
├──────────────────────────────────────────────────┤
│                    Service Layer                 │
│           (Business logic, transactions)         │
├──────────────────────────────────────────────────┤
│                  Repository Layer                │
│          (Spring Data JPA, database queries)     │
├──────────────────────────────────────────────────┤
│                   Entity / Model                 │
│            (JPA entities, MySQL tables)          │
└──────────────────────────────────────────────────┘
```

### Request Flow

```
Client Request
    │
    ▼
┌─────────────┐     ┌─────────────┐
│ JWT Filter  │────▶│ Security    │  (xác thực token, phân quyền)
└──────┬──────┘     │ Config      │
       │            └─────────────┘
       ▼
┌─────────────┐
│ Controller  │  → Validate @RequestBody, path variables
└──────┬──────┘
       │
       ▼
┌─────────────┐
│  Service    │  → Business logic, gọi Repository
└──────┬──────┘
       │
       ▼
┌─────────────┐
│ Repository  │  → Spring Data JPA → MySQL 8
└─────────────┘
       │
       ▼
   JSON Response (DTO)
```

---

## 🗄️ Database Schema (ERD)

```
┌─────────────┐       ┌──────────────┐
│   users     │       │  categories  │
├─────────────┤       ├──────────────┤
│ id (PK)     │       │ id (PK)      │
│ email       │       │ name         │
│ password    │       │ slug         │
│ full_name   │       │ description  │
│ phone       │       │ image_url    │
│ avatar_url  │       │ parent_id(FK)│
│ role        │       │ created_at   │
│ is_active   │       │ updated_at   │
│ created_at  │       └──────┬───────┘
│ updated_at  │              │ 1:N
└──────┬──────┘              │
       │                     │
       │ 1:N          ┌──────▼───────┐
       │              │   products   │
       │              ├──────────────┤
       │              │ id (PK)      │
       │              │ name         │
       │              │ slug         │
       │              │ description  │
       │              │ price        │
       │              │ sale_price   │
       │              │ stock        │
       │              │ brand        │
       │              │ image_url    │
       │              │ images (JSON)│
       │              │ category_id  │
       │              │ is_active    │
       │              │ is_featured  │
       │              │ created_at   │
       │              │ updated_at   │
       │              └──────┬───────┘
       │                     │
       │ 1:N                 │ 1:N
       │                     │
┌──────▼──────┐       ┌──────▼───────┐
│   orders    │       │   reviews    │
├─────────────┤       ├──────────────┤
│ id (PK)     │       │ id (PK)      │
│ user_id(FK) │       │ user_id (FK) │
│ order_code  │       │ product_id   │
│ status      │       │ rating (1-5) │
│ total_amount│       │ comment      │
│ shipping_   │       │ created_at   │
│   address   │       │ updated_at   │
│ phone       │       └──────────────┘
│ note        │
│ payment_    │
│   method    │
│ created_at  │
│ updated_at  │
└──────┬──────┘
       │ 1:N
       │
┌──────▼──────┐
│ order_items │
├─────────────┤
│ id (PK)     │
│ order_id(FK)│
│ product_id  │
│ product_name│
│ quantity    │
│ price       │
│ subtotal    │
└─────────────┘
```

---

## 📡 API Endpoints

### 🔐 Authentication (`/api/auth`)

| Method | Endpoint | Mô tả | Auth |
|---|---|---|---|
| `POST` | `/api/auth/register` | Đăng ký tài khoản | ❌ |
| `POST` | `/api/auth/login` | Đăng nhập → JWT token | ❌ |
| `POST` | `/api/auth/refresh` | Refresh JWT token | ✅ |
| `GET` | `/api/auth/me` | Lấy thông tin user hiện tại | ✅ |

### 📦 Products (`/api/products`)

| Method | Endpoint | Mô tả | Auth |
|---|---|---|---|
| `GET` | `/api/products` | Danh sách sản phẩm (pageable, filter, sort) | ❌ |
| `GET` | `/api/products/{id}` | Chi tiết sản phẩm | ❌ |
| `GET` | `/api/products/slug/{slug}` | Sản phẩm theo slug | ❌ |
| `GET` | `/api/products/search?keyword=` | Tìm kiếm sản phẩm | ❌ |
| `GET` | `/api/products/featured` | Sản phẩm nổi bật | ❌ |
| `GET` | `/api/products/category/{categoryId}` | Sản phẩm theo danh mục | ❌ |

### 📂 Categories (`/api/categories`)

| Method | Endpoint | Mô tả | Auth |
|---|---|---|---|
| `GET` | `/api/categories` | Danh sách danh mục | ❌ |
| `GET` | `/api/categories/{id}` | Chi tiết danh mục | ❌ |
| `GET` | `/api/categories/slug/{slug}` | Danh mục theo slug | ❌ |

### 🛒 Orders (`/api/orders`)

| Method | Endpoint | Mô tả | Auth |
|---|---|---|---|
| `POST` | `/api/orders` | Tạo đơn hàng mới | ✅ USER |
| `GET` | `/api/orders/my-orders` | Đơn hàng của user hiện tại | ✅ USER |
| `GET` | `/api/orders/{id}` | Chi tiết đơn hàng | ✅ USER |
| `PUT` | `/api/orders/{id}/cancel` | Hủy đơn hàng (nếu PENDING) | ✅ USER |

### ⭐ Reviews (`/api/reviews`)

| Method | Endpoint | Mô tả | Auth |
|---|---|---|---|
| `GET` | `/api/reviews/product/{productId}` | Đánh giá của sản phẩm | ❌ |
| `POST` | `/api/reviews` | Tạo đánh giá mới | ✅ USER |
| `PUT` | `/api/reviews/{id}` | Sửa đánh giá | ✅ USER |
| `DELETE` | `/api/reviews/{id}` | Xóa đánh giá | ✅ USER |

### 👑 Admin (`/api/admin`)

| Method | Endpoint | Mô tả | Auth |
|---|---|---|---|
| `GET` | `/api/admin/stats/dashboard` | Thống kê tổng quan | ✅ ADMIN |
| `POST` | `/api/admin/products` | Thêm sản phẩm | ✅ ADMIN |
| `PUT` | `/api/admin/products/{id}` | Cập nhật sản phẩm | ✅ ADMIN |
| `DELETE` | `/api/admin/products/{id}` | Xóa sản phẩm | ✅ ADMIN |
| `POST` | `/api/admin/categories` | Thêm danh mục | ✅ ADMIN |
| `PUT` | `/api/admin/categories/{id}` | Cập nhật danh mục | ✅ ADMIN |
| `DELETE` | `/api/admin/categories/{id}` | Xóa danh mục | ✅ ADMIN |
| `GET` | `/api/admin/orders` | Tất cả đơn hàng | ✅ ADMIN |
| `PUT` | `/api/admin/orders/{id}/status` | Cập nhật trạng thái đơn | ✅ ADMIN |
| `GET` | `/api/admin/users` | Danh sách users | ✅ ADMIN |
| `PUT` | `/api/admin/users/{id}/status` | Kích hoạt/vô hiệu hóa user | ✅ ADMIN |

### 📤 Upload (`/api/upload`)

| Method | Endpoint | Mô tả | Auth |
|---|---|---|---|
| `POST` | `/api/upload/image` | Upload 1 ảnh | ✅ ADMIN |
| `POST` | `/api/upload/images` | Upload nhiều ảnh | ✅ ADMIN |
| `DELETE` | `/api/upload/image` | Xóa ảnh (by publicId) | ✅ ADMIN |

### Response Format

```json
// Success
{
  "success": true,
  "message": "Lấy danh sách sản phẩm thành công",
  "data": { ... },
  "timestamp": "2026-06-25T12:00:00"
}

// Error
{
  "success": false,
  "message": "Sản phẩm không tồn tại",
  "errors": ["Product with id 99 not found"],
  "timestamp": "2026-06-25T12:00:00"
}

// Paginated
{
  "success": true,
  "message": "OK",
  "data": {
    "content": [...],
    "page": 0,
    "size": 12,
    "totalElements": 50,
    "totalPages": 5,
    "last": false
  },
  "timestamp": "2026-06-25T12:00:00"
}
```

---

## 🗺️ Roadmap

### Phase 1 — Khởi Tạo & Database `[Tuần 1]`

- [ ] **1.1 — Setup Spring Boot Project**
  - [ ] Tạo project từ Spring Initializr (Maven, Java 17+)
  - [ ] Thêm dependencies: Spring Web, Data JPA, Security, MySQL, Lombok, Validation, Flyway
  - [ ] Cấu hình `application.yml` + `application-dev.yml`
  - [ ] Cấu hình CORS cho frontend
  - [ ] Setup `.gitignore`

- [ ] **1.2 — Database & Entity**
  - [ ] Tạo MySQL database `toy_store`
  - [ ] Viết Flyway migration scripts (V1__create_tables.sql, V2__seed_data.sql)
  - [ ] Tạo JPA Entities: `User`, `Category`, `Product`, `Order`, `OrderItem`, `Review`
  - [ ] Tạo Enums: `Role`, `OrderStatus`, `PaymentMethod`
  - [ ] Tạo base entity (`BaseEntity` với id, createdAt, updatedAt)
  - [ ] Tạo DTOs (request/response) cho mỗi entity
  - [ ] Tạo Mapper (MapStruct hoặc thủ công)

- [ ] **1.3 — Global Config**
  - [ ] `ApiResponse<T>` wrapper class
  - [ ] `GlobalExceptionHandler` (@ControllerAdvice)
  - [ ] Custom exceptions (ResourceNotFoundException, BadRequestException, DuplicateResourceException)
  - [ ] Pagination response DTO

---

### Phase 2 — CRUD API (Không Auth) `[Tuần 2]`

- [ ] **2.1 — Category API**
  - [ ] `CategoryRepository` (Spring Data JPA)
  - [ ] `CategoryService` + `CategoryServiceImpl`
  - [ ] `CategoryController` — GET endpoints (public)
  - [ ] `AdminCategoryController` — POST/PUT/DELETE (tạm thời không auth)
  - [ ] ✅ Test với Postman

- [ ] **2.2 — Product API**
  - [ ] `ProductRepository` (custom queries: filter, search, featured)
  - [ ] `ProductService` + `ProductServiceImpl`
  - [ ] `ProductController` — GET endpoints (public, pageable)
  - [ ] `AdminProductController` — CRUD (tạm thời không auth)
  - [ ] Specification hoặc custom queries cho filter (category, brand, price range)
  - [ ] ✅ Test với Postman

- [ ] **2.3 — Review API**
  - [ ] `ReviewRepository`
  - [ ] `ReviewService` + `ReviewServiceImpl`
  - [ ] `ReviewController` — GET (public), POST/PUT/DELETE (tạm không auth)
  - [ ] ✅ Test với Postman

---

### Phase 3 — Authentication & Authorization `[Tuần 3]`

- [ ] **3.1 — JWT Infrastructure**
  - [ ] Thêm dependency `jjwt` (io.jsonwebtoken)
  - [ ] `JwtTokenProvider` — generate, validate, extract claims
  - [ ] `JwtAuthFilter` extends `OncePerRequestFilter`
  - [ ] `CustomUserDetailsService` implements `UserDetailsService`

- [ ] **3.2 — Auth API**
  - [ ] `AuthController` — register, login, refresh, me
  - [ ] `AuthService` — register (BCrypt), login (authenticate + generate JWT)
  - [ ] Response: `{ token, refreshToken, tokenType, expiresIn }`

- [ ] **3.3 — Security Config**
  - [ ] `SecurityConfig` — SecurityFilterChain
  - [ ] Public endpoints: GET products, categories, reviews, auth/*
  - [ ] USER endpoints: orders/*, reviews POST/PUT/DELETE
  - [ ] ADMIN endpoints: /api/admin/**
  - [ ] CORS config cho frontend origin
  - [ ] ✅ Test auth flow đầy đủ với Postman

---

### Phase 4 — Order API & Business Logic `[Tuần 4]`

- [ ] **4.1 — Order API**
  - [ ] `OrderController` — CRUD cho user
  - [ ] `AdminOrderController` — quản lý đơn hàng
  - [ ] `OrderService` — tạo đơn hàng (validate stock, tính tổng, tạo order items)
  - [ ] `OrderStatus` transitions validation (PENDING → CONFIRMED → SHIPPING → ...)
  - [ ] Auto-generate order code (e.g., TS-20260625-0001)
  - [ ] ✅ Test đầy đủ flow: tạo đơn → cập nhật status → hủy đơn

- [ ] **4.2 — User Profile API**
  - [ ] `GET /api/auth/me` — thông tin user
  - [ ] `PUT /api/auth/profile` — cập nhật profile
  - [ ] `PUT /api/auth/change-password` — đổi mật khẩu
  - [ ] ✅ Test với Postman

---

### Phase 5 — Upload, Admin & Testing `[Tuần 5]`

- [ ] **5.1 — File Upload (Cloudinary)**
  - [ ] Tích hợp Cloudinary SDK
  - [ ] `FileUploadService` — upload, delete image
  - [ ] `UploadController` — endpoints upload 1 ảnh / nhiều ảnh
  - [ ] Validate file type (jpg, png, webp) & size (max 5MB)
  - [ ] ✅ Test upload/delete

- [ ] **5.2 — Admin Dashboard API**
  - [ ] `GET /api/admin/stats/dashboard` — tổng sản phẩm, đơn hàng, users, doanh thu
  - [ ] Admin CRUD cho users (list, activate/deactivate)
  - [ ] ✅ Test với Postman

- [ ] **5.3 — Unit & Integration Tests**
  - [ ] Unit test cho Services (JUnit 5 + Mockito)
  - [ ] Integration test cho Controllers (MockMvc + @SpringBootTest)
  - [ ] Test JWT auth flow
  - [ ] Test error cases & validation
  - [ ] `mvn test` — tất cả test phải pass ✅

- [ ] **5.4 — API Documentation**
  - [ ] Thêm SpringDoc OpenAPI (Swagger UI)
  - [ ] Annotate controllers với `@Operation`, `@ApiResponse`
  - [ ] Swagger UI tại `http://localhost:8080/swagger-ui.html`

---

## 📁 Cấu Trúc Thư Mục

```
backend/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/toystore/
│   │   │   ├── ToyStoreApplication.java
│   │   │   │
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── CorsConfig.java
│   │   │   │   ├── CloudinaryConfig.java
│   │   │   │   └── OpenApiConfig.java
│   │   │   │
│   │   │   ├── entity/
│   │   │   │   ├── BaseEntity.java
│   │   │   │   ├── User.java
│   │   │   │   ├── Category.java
│   │   │   │   ├── Product.java
│   │   │   │   ├── Order.java
│   │   │   │   ├── OrderItem.java
│   │   │   │   └── Review.java
│   │   │   │
│   │   │   ├── enums/
│   │   │   │   ├── Role.java
│   │   │   │   ├── OrderStatus.java
│   │   │   │   └── PaymentMethod.java
│   │   │   │
│   │   │   ├── dto/
│   │   │   │   ├── request/
│   │   │   │   │   ├── LoginRequest.java
│   │   │   │   │   ├── RegisterRequest.java
│   │   │   │   │   ├── ProductRequest.java
│   │   │   │   │   ├── CategoryRequest.java
│   │   │   │   │   ├── OrderRequest.java
│   │   │   │   │   └── ReviewRequest.java
│   │   │   │   └── response/
│   │   │   │       ├── ApiResponse.java
│   │   │   │       ├── AuthResponse.java
│   │   │   │       ├── ProductResponse.java
│   │   │   │       ├── CategoryResponse.java
│   │   │   │       ├── OrderResponse.java
│   │   │   │       ├── ReviewResponse.java
│   │   │   │       ├── UserResponse.java
│   │   │   │       └── PageResponse.java
│   │   │   │
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── ProductRepository.java
│   │   │   │   ├── CategoryRepository.java
│   │   │   │   ├── OrderRepository.java
│   │   │   │   ├── OrderItemRepository.java
│   │   │   │   └── ReviewRepository.java
│   │   │   │
│   │   │   ├── service/
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── ProductService.java
│   │   │   │   ├── CategoryService.java
│   │   │   │   ├── OrderService.java
│   │   │   │   ├── ReviewService.java
│   │   │   │   ├── FileUploadService.java
│   │   │   │   └── impl/
│   │   │   │       ├── AuthServiceImpl.java
│   │   │   │       ├── ProductServiceImpl.java
│   │   │   │       ├── CategoryServiceImpl.java
│   │   │   │       ├── OrderServiceImpl.java
│   │   │   │       ├── ReviewServiceImpl.java
│   │   │   │       └── FileUploadServiceImpl.java
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── ProductController.java
│   │   │   │   ├── CategoryController.java
│   │   │   │   ├── OrderController.java
│   │   │   │   ├── ReviewController.java
│   │   │   │   ├── UploadController.java
│   │   │   │   └── admin/
│   │   │   │       ├── AdminProductController.java
│   │   │   │       ├── AdminCategoryController.java
│   │   │   │       ├── AdminOrderController.java
│   │   │   │       ├── AdminUserController.java
│   │   │   │       └── AdminDashboardController.java
│   │   │   │
│   │   │   ├── security/
│   │   │   │   ├── JwtTokenProvider.java
│   │   │   │   ├── JwtAuthFilter.java
│   │   │   │   └── CustomUserDetailsService.java
│   │   │   │
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   ├── BadRequestException.java
│   │   │   │   └── DuplicateResourceException.java
│   │   │   │
│   │   │   └── util/
│   │   │       └── SlugUtil.java
│   │   │
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── application-prod.yml
│   │       └── db/migration/
│   │           ├── V1__create_tables.sql
│   │           └── V2__seed_data.sql
│   │
│   └── test/java/com/toystore/
│       ├── service/
│       │   ├── ProductServiceTest.java
│       │   ├── CategoryServiceTest.java
│       │   ├── OrderServiceTest.java
│       │   └── AuthServiceTest.java
│       └── controller/
│           ├── ProductControllerTest.java
│           ├── AuthControllerTest.java
│           └── OrderControllerTest.java
│
└── README.md   # File này
```

---

## 🚀 Hướng Dẫn Chạy

### 1. Tạo Database MySQL

```sql
CREATE DATABASE toy_store CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'toystore'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON toy_store.* TO 'toystore'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Cấu hình `application-dev.yml`

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/toy_store?useSSL=false&serverTimezone=Asia/Ho_Chi_Minh&allowPublicKeyRetrieval=true
    username: toystore
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect
        format_sql: true
  flyway:
    enabled: true
    locations: classpath:db/migration

server:
  port: 8080

app:
  jwt:
    secret: mySecretKeyForJWTTokenGenerationAtLeast256BitsLong2026
    expiration-ms: 86400000        # 24 giờ
    refresh-expiration-ms: 604800000  # 7 ngày
  cloudinary:
    cloud-name: your-cloud-name
    api-key: your-api-key
    api-secret: your-api-secret
  cors:
    allowed-origins: http://localhost:3000,http://localhost:5500
```

### 3. Chạy Application

```bash
# Development
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Hoặc build JAR
mvn clean package -DskipTests
java -jar target/toy-store-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

### 4. Kiểm Tra

- API: `http://localhost:8080/api/products`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- Health: `http://localhost:8080/actuator/health`

---

## 🧪 Testing

### Chạy Test

```bash
# Chạy tất cả test
mvn test

# Chạy test cụ thể
mvn test -Dtest=ProductServiceTest

# Chạy với report
mvn test jacoco:report
```

### Test Strategy

| Layer | Tool | Mô tả |
|---|---|---|
| **Service** | JUnit 5 + Mockito | Mock repository, test business logic |
| **Controller** | MockMvc | Test HTTP request/response, status codes |
| **Repository** | @DataJpaTest | Test custom queries với H2 in-memory DB |
| **Integration** | @SpringBootTest | Test full flow end-to-end |
| **API Manual** | Postman Collection | Test thủ công tất cả endpoints |

### Postman Collection

Sau khi hoàn thành, export Postman collection tại: `docs/ToyStore_API.postman_collection.json`

---

## 📌 Ghi Chú

- Sử dụng `@Transactional` cho các service methods có write operations
- Tất cả response đều wrap trong `ApiResponse<T>` để đồng nhất format
- Password hash bằng BCrypt (strength 10)
- JWT token gửi qua header: `Authorization: Bearer <token>`
- Slug tự động generate từ tên sản phẩm/danh mục
- Soft delete cho products (đặt `is_active = false` thay vì xóa)
- Pagination mặc định: `page=0, size=12, sort=createdAt,desc`
