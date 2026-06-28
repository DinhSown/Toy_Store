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

## 🗺️ Roadmap Chi Tiết Từng Ngày (8 Tuần Backend)

Dưới đây là kế hoạch phát triển chi tiết chia nhỏ theo từng ngày (từ Thứ 2 đến Thứ 6) để dễ dàng triển khai và kiểm thử.

---

### 📦 Tuần 1: Khởi Tạo & Cơ Sở Hạ Tầng (Đã Hoàn Thành)
* **Ngày 1**: Khởi tạo project từ Spring Initializr, thiết lập Maven `pom.xml`, các file cấu hình `application.yml` và `application-dev.yml` (bao gồm hồ sơ kết nối DB, JWT và Cloudinary).
* **Ngày 2**: Viết migration script `V1__create_tables.sql` để tạo schema cơ sở dữ liệu. Xây dựng các lớp JPA Entities tương ứng (`User`, `Category`, `Product`, `Order`, `OrderItem`, `Review`) kế thừa từ `BaseEntity` (JPA Auditing).
* **Ngày 3**: Triển khai cơ sở hạ tầng phản hồi lỗi: Lớp bọc response chuẩn `ApiResponse<T>`, các ngoại lệ tùy chỉnh (`ResourceNotFoundException`, `BadRequestException`, `DuplicateResourceException`) và `GlobalExceptionHandler` dùng `@ControllerAdvice`.
* **Ngày 4**: Xây dựng toàn bộ các lớp DTO (Request/Response) cho tất cả entities kèm annotation ràng buộc validation dữ liệu đầu vào.
* **Ngày 5**: Viết tiện ích `SlugUtil` để tạo URL SEO-friendly. Triển khai các MapStruct mappers (bao gồm xử lý deserialize JSON của ảnh phụ bằng Jackson và tính toán động rating trung bình của sản phẩm). Khởi động ứng dụng, tự động chạy Flyway tạo bảng trên MySQL local và kết thúc Tuần 1.

---

### 📦 Tuần 2: Category & Product API (Không Auth)
> **Mục tiêu**: Có thể thực hiện CRUD danh mục và sản phẩm trên Postman thông qua các API public tạm thời.

* **Ngày 1 — CRUD Danh mục (Phần Service & DB)**:
  - [x] Tạo `CategoryRepository` kế thừa `JpaRepository`.
  - [x] Xây dựng interface `CategoryService` và lớp triển khai `CategoryServiceImpl` hỗ trợ các hàm: lấy tất cả danh mục (bao gồm phân cấp cha-con), lấy chi tiết danh mục theo ID/slug, thêm mới danh mục (sử dụng `SlugUtil` tự động sinh slug), cập nhật danh mục và thay đổi trạng thái hoạt động (soft delete).
* **Ngày 2 — CRUD Danh mục (Phần Controller)**:
  - [ ] Tạo `CategoryController` cung cấp các API public:
    - `GET /api/categories` — Lấy danh mục gốc và các danh mục con lồng nhau.
    - `GET /api/categories/{id}` — Lấy chi tiết danh mục.
    - `GET /api/categories/slug/{slug}` — Lấy danh mục theo slug.
  - [ ] Tạo `AdminCategoryController` (tạm public) chứa các phương thức POST, PUT, DELETE danh mục.
  - [ ] Kiểm thử toàn bộ API danh mục trên Postman.
* **Ngày 3 — Product Repository & Service**:
  - [ ] Tạo `ProductRepository` với các truy vấn tùy chỉnh để lọc sản phẩm theo categoryId, khoảng giá (minPrice, maxPrice) và brand.
  - [ ] Xây dựng interface `ProductService` và triển khai `ProductServiceImpl` với các phương thức nghiệp vụ: lấy sản phẩm phân trang, tìm kiếm sản phẩm theo từ khóa, lấy danh sách sản phẩm nổi bật (`isFeatured = true`), tạo mới sản phẩm (auto-generate slug và gán category), cập nhật sản phẩm và soft delete (`isActive = false`).
* **Ngày 4 — Product Controller (Public)**:
  - [ ] Tạo `ProductController` cung cấp các API public:
    - `GET /api/products` (hỗ trợ phân trang: `?page=0&size=12&sort=createdAt,desc` và lọc: `?categoryId=&brand=&minPrice=&maxPrice=`).
    - `GET /api/products/{id}`.
    - `GET /api/products/slug/{slug}`.
    - `GET /api/products/search?keyword=` (tìm kiếm full-text hoặc like query).
    - `GET /api/products/featured` (lấy sản phẩm nổi bật).
* **Ngày 5 — Admin Product Controller & Seed Data**:
  - [ ] Tạo `AdminProductController` (tạm public) chứa các phương thức POST, PUT, DELETE sản phẩm.
  - [ ] Viết file migration SQL `V2__seed_data.sql` chứa dữ liệu mẫu của 5 danh mục và ít nhất 20 sản phẩm đa dạng mẫu mã.
  - [ ] Khởi động ứng dụng để Flyway tự động nạp dữ liệu seed. Kiểm thử toàn bộ API sản phẩm trên Postman đảm bảo phân trang và bộ lọc chạy đúng kết quả.

---

### 📦 Tuần 3: Review API & Cơ sở hạ tầng JWT Security
> **Mục tiêu**: Hoàn thành API đánh giá sản phẩm và dựng sẵn hệ thống JWT để chuẩn bị tích hợp bảo mật Spring Security.

* **Ngày 1 — Review Service**:
  - [ ] Tạo `ReviewRepository` kèm ràng buộc để kiểm tra sự tồn tại của review theo `userId` và `productId`.
  - [ ] Thiết lập interface `ReviewService` và triển khai `ReviewServiceImpl` thực hiện: lấy đánh giá của một sản phẩm, thêm đánh giá (validate rating phải từ 1-5, kiểm tra user đã mua hàng chưa - tạm thời bỏ qua bước mua hàng, kiểm tra user đã đánh giá sản phẩm này chưa để ném `DuplicateResourceException`), cập nhật đánh giá, và xóa đánh giá.
* **Ngày 2 — Review Controller**:
  - [ ] Tạo `ReviewController` cung cấp các API:
    - `GET /api/reviews/product/{productId}` (public) — Lấy tất cả đánh giá của sản phẩm.
    - Các API POST, PUT, DELETE đánh giá (tạm thời public).
  - [ ] Kiểm thử luồng đánh giá sản phẩm trên Postman.
* **Ngày 3 — Thư viện JWT & JwtTokenProvider**:
  - [ ] Thêm thư viện `jjwt` vào `pom.xml`.
  - [ ] Tạo cấu hình JWT secrets và thời hạn hết hạn token trong `application-dev.yml`.
  - [ ] Tạo lớp `JwtTokenProvider` chứa logic tạo JWT (chứa email, role và claims), giải mã JWT, kiểm tra thời hạn token và lấy email người dùng từ token.
* **Ngày 4 — User Details & UserDetailsService**:
  - [ ] Cập nhật lớp `User` entity kế thừa `UserDetails` của Spring Security (hoặc tạo lớp `CustomUserDetails` bao ngoài User entity).
  - [ ] Tạo lớp `CustomUserDetailsService` kế thừa từ `UserDetailsService` để tìm kiếm user trong database theo email và trả về đối tượng `UserDetails`.
* **Ngày 5 — Bộ lọc bảo mật JwtAuthFilter**:
  - [ ] Tạo lớp `JwtAuthFilter` kế thừa từ `OncePerRequestFilter`. Lớp này sẽ chặn các request, trích xuất JWT từ Authorization header (`Bearer <token>`), kiểm tra tính hợp lệ và cấu hình thông tin xác thực vào `SecurityContextHolder`.
  - [ ] Khởi chạy thử ứng dụng đảm bảo bộ lọc JWT được load đúng và không phá vỡ các API hiện có.

---

### 📦 Tuần 4: Đăng Ký, Đăng Nhập & Phân Quyền (Spring Security)
> **Mục tiêu**: Người dùng có thể đăng ký, đăng nhập để nhận token. Các Admin API được bảo vệ nghiêm ngặt.

* **Ngày 1 — Authentication Service**:
  - [ ] Tạo lớp mã hóa mật khẩu `BCryptPasswordEncoder` bean.
  - [ ] Triển khai `AuthService` với các nghiệp vụ:
    - `register()`: Kiểm tra email trùng lặp, mã hóa mật khẩu bằng BCrypt, lưu User mới với role mặc định là `ROLE_USER`.
    - `login()`: Xác thực thông tin đăng nhập bằng Spring Security `AuthenticationManager`, tạo cặp Access Token và Refresh Token trả về client thông qua `AuthResponse`.
* **Ngày 2 — Authentication Controller & Endpoint /me**:
  - [ ] Tạo `AuthController` với các API:
    - `POST /api/auth/register` (public)
    - `POST /api/auth/login` (public)
    - `POST /api/auth/refresh` (public) — cấp lại Access Token từ Refresh Token.
    - `GET /api/auth/me` (yêu cầu đăng nhập) — trả về thông tin của User đang kết nối.
  - [ ] Viết API test thử luồng đăng ký/đăng nhập trên Postman.
* **Ngày 3 — Cấu hình Spring Security Config**:
  - [ ] Tạo lớp cấu hình `SecurityConfig` và kích hoạt chế độ `@EnableWebSecurity`.
  - [ ] Cấu hình `SecurityFilterChain` quản lý các quy định về xác thực: tắt cơ chế CSRF, cấu hình Session sang chế độ `STATELESS` (không lưu trạng thái session).
  - [ ] Cấu hình cors và tích hợp `JwtAuthFilter` đứng trước lớp `UsernamePasswordAuthenticationFilter`.
* **Ngày 4 — Thiết lập phân quyền chi tiết (RBAC)**:
  - [ ] Cấu hình chi tiết phân quyền trong `SecurityFilterChain`:
    - Cho phép truy cập không cần token (Permit All) đối với: các API GET của sản phẩm, danh mục, đánh giá và các API đăng nhập/đăng ký.
    - Yêu cầu quyền `ROLE_USER` hoặc `ROLE_ADMIN` đối với các API liên quan đến giỏ hàng, viết review, tạo đơn hàng.
    - Yêu cầu quyền `ROLE_ADMIN` đối với tất cả các API quản trị bắt đầu bằng `/api/admin/**`.
* **Ngày 5 — Kiểm thử luồng bảo mật**:
  - [ ] Test đăng nhập lấy token.
  - [ ] Gọi các API yêu cầu xác thực bằng token hợp lệ -> Trả về dữ liệu thành công (200).
  - [ ] Gọi API yêu cầu xác thực không có token hoặc token sai -> Trả về lỗi Unauthorized (401).
  - [ ] Sử dụng token của User thường để truy cập API admin -> Trả về lỗi Forbidden (403).

---

### 📦 Tuần 5: Order API & Luồng Đặt Hàng
> **Mục tiêu**: Khách hàng có thể tạo đơn hàng thành công, admin có thể quản lý trạng thái của tất cả đơn hàng.

* **Ngày 1 — Thiết kế Repositories & Xương cá Service**:
  - [ ] Tạo `OrderRepository` và `OrderItemRepository`.
  - [ ] Định nghĩa interface `OrderService` và các lớp Request/Response tương ứng.
* **Ngày 2 — Tạo Đơn hàng & Xử lý Kho hàng (Giao dịch chính)**:
  - [ ] Triển khai phương thức `createOrder()` trong `OrderServiceImpl` chạy trong một `@Transactional`:
    - Duyệt qua từng sản phẩm trong giỏ hàng gửi lên, kiểm tra xem số lượng trong kho có đủ không (nếu không đủ ném lỗi `BadRequestException`).
    - Trừ số lượng kho của sản phẩm tương ứng.
    - Tính tổng tiền đơn hàng, tạo các bản ghi `OrderItem` lưu snapshot của sản phẩm (tên, giá hiện tại, ảnh chính) tại thời điểm mua.
    - Tạo mã đơn hàng duy nhất định dạng: `TS-YYYYMMDD-XXXX` (trong đó YYYYMMDD là ngày hiện tại, XXXX là chuỗi ngẫu nhiên hoặc số tăng dần).
* **Ngày 3 — Lịch sử đơn hàng & Hủy đơn hàng (User)**:
  - [ ] Triển khai phương thức `getMyOrders()` lọc các đơn hàng của user đang đăng nhập hiện tại từ Security Context.
  - [ ] Triển khai phương thức `getOrderById()` lấy chi tiết đơn hàng (thêm validate chỉ cho phép chính chủ đơn hàng hoặc admin xem).
  - [ ] Triển khai phương thức `cancelOrder()` cho phép khách hàng hủy đơn hàng (chỉ cho phép hủy khi đơn hàng ở trạng thái `PENDING`, nếu khác PENDING ném lỗi `BadRequestException`).
* **Ngày 4 — Cập nhật trạng thái đơn hàng (Admin)**:
  - [ ] Triển khai phương thức `updateOrderStatus()` dành cho Admin để thay đổi trạng thái đơn hàng.
  - [ ] Thiết lập bộ kiểm tra logic chuyển đổi trạng thái hợp lý (ví dụ: không được chuyển từ `PENDING` thẳng sang `DELIVERED`, không được cập nhật đơn hàng đã hủy `CANCELLED`).
* **Ngày 5 — Controllers & Kiểm thử Luồng đặt hàng**:
  - [ ] Tạo `OrderController` cho khách hàng (`POST /api/orders`, `GET /api/orders/my-orders`, `PUT /api/orders/{id}/cancel`).
  - [ ] Tạo `AdminOrderController` cho quản trị viên (`GET /api/admin/orders`, `PUT /api/admin/orders/{id}/status`).
  - [ ] Kiểm thử toàn bộ chu trình đặt hàng trên Postman: Tạo đơn hàng -> Admin xác nhận -> Giao hàng thành công. Kiểm tra xem số lượng kho của sản phẩm có tự động trừ đi chính xác hay không.

---

### 📦 Tuần 6: Hồ Sơ Người Dùng & File Upload (Cloudinary)
> **Mục tiêu**: Người dùng tự quản lý thông tin tài khoản và Admin có thể đăng ảnh sản phẩm trực tiếp từ máy lên kho lưu trữ đám mây.

* **Ngày 1 — User Profile API**:
  - [ ] Triển khai API `PUT /api/auth/profile` cho phép cập nhật thông tin cá nhân (Họ tên, số điện thoại, ảnh đại diện).
  - [ ] Triển khai API `PUT /api/auth/change-password` xác thực mật khẩu cũ và mã hóa mật khẩu mới cập nhật vào DB.
* **Ngày 2 — Tích hợp SDK Cloudinary**:
  - [ ] Cấu hình các thông số API Key, API Secret và Cloud Name của Cloudinary vào `application-dev.yml`.
  - [ ] Tạo lớp cấu hình `CloudinaryConfig` khởi tạo đối tượng `Cloudinary` bean làm việc với API của hãng.
* **Ngày 3 — File Upload Service**:
  - [ ] Thiết lập interface `FileUploadService` và triển khai `FileUploadServiceImpl` chứa các chức năng:
    - Upload một file ảnh lên thư mục định sẵn trên Cloudinary và trả về URL ảnh tuyệt đối.
    - Upload danh sách nhiều file ảnh (dành cho bộ sưu tập ảnh phụ của sản phẩm).
    - Xóa ảnh cũ trên Cloudinary thông qua Public ID khi cập nhật hoặc xóa sản phẩm.
* **Ngày 4 — Upload Controller**:
  - [ ] Tạo `UploadController` (yêu cầu quyền Admin) cung cấp các endpoint:
    - `POST /api/upload/image` (tải lên 1 ảnh).
    - `POST /api/upload/images` (tải lên nhiều ảnh đồng thời).
    - `DELETE /api/upload/image` (xóa ảnh).
  - [ ] Thêm validation kiểm tra định dạng file (chỉ nhận ảnh: `.jpg`, `.png`, `.webp`) và giới hạn kích thước file tải lên tối đa là 5MB để tránh quá tải server.
* **Ngày 5 — Kiểm thử luồng upload ảnh**:
  - [ ] Test upload ảnh bằng Postman (sử dụng Form-data).
  - [ ] Sử dụng đường dẫn ảnh nhận được từ Cloudinary để làm trường `imageUrl` tạo sản phẩm mới thông qua API -> Xác nhận ảnh hiển thị đúng trên trình duyệt.

---

### 📦 Tuần 7: Thống Kê Admin Dashboard & Tài Liệu API Swagger
> **Mục tiêu**: Cung cấp API thống kê nhanh cho Admin và tích hợp giao diện tương tác API Swagger trực quan.

* **Ngày 1 — Quản lý Tài khoản Khách hàng (Admin)**:
  - [ ] Tạo `AdminUserController` cung cấp API lấy danh sách toàn bộ người dùng, tìm kiếm theo tên/email và API bật/tắt trạng thái hoạt động của tài khoản (`isActive = true/false`).
* **Ngày 2 — Thống kê số liệu Dashboard (Admin)**:
  - [ ] Viết các câu lệnh SQL Custom Queries trong Repository để lấy số liệu:
    - Tổng doanh thu tháng hiện tại so với tháng trước.
    - Tổng số lượng đơn hàng, tổng số sản phẩm đã bán ra.
    - Số lượng người dùng mới đăng ký trong tháng.
  - [ ] Tạo `AdminDashboardController` cung cấp API `GET /api/admin/stats/dashboard` để trả về các số liệu thống kê tổng hợp trên.
* **Ngày 3 — Cấu hình Swagger OpenAPI**:
  - [ ] Tích hợp thư viện `springdoc-openapi-starter-webmvc-ui` vào dự án.
  - [ ] Tạo lớp `OpenApiConfig` để mô tả dự án và cấu hình cơ chế bảo mật JWT Security Scheme trên giao diện Swagger UI để có thể dán JWT Token vào test trực tiếp.
* **Ngày 4 — Viết tài liệu mô tả API**:
  - [ ] Thêm các annotation `@Tag` mô tả phân nhóm chức năng cho từng Controller.
  - [ ] Sử dụng `@Operation` và `@ApiResponse` để chú thích cụ thể chức năng của từng endpoint, tham số đầu vào và kiểu dữ liệu trả về của DTO.
* **Ngày 5 — Xác minh giao diện Swagger**:
  - [ ] Truy cập đường dẫn `http://localhost:8080/swagger-ui.html` trên trình duyệt.
  - [ ] Thực hiện đăng nhập lấy token từ API Auth, dán vào nút "Authorize" trên Swagger và thử gọi các API của Admin để đảm bảo tài liệu API tương tác tốt 100%.

---

### 📦 Tuần 8: Viết Kiểm Thử (Testing) & Nghiệm Thu Backend
> **Mục tiêu**: Đảm bảo toàn bộ nghiệp vụ hoạt động ổn định thông qua kiểm thử tự động đạt độ phủ cao, hoàn thiện source code.

* **Ngày 1 — Unit Test cho các Services (Product & Category)**:
  - [ ] Tạo các lớp kiểm thử `ProductServiceTest` và `CategoryServiceTest` sử dụng JUnit 5 và Mockito.
  - [ ] Viết mock kiểm thử luồng CRUD sản phẩm, kiểm thử tính đúng đắn của bộ lọc và tìm kiếm sản phẩm.
* **Ngày 2 — Unit Test cho các Services (Order, Auth & Review)**:
  - [ ] Viết kiểm thử logic đặt hàng `OrderServiceTest` (giả lập các trường hợp hết hàng, chuyển đổi trạng thái đơn hợp lệ/không hợp lệ).
  - [ ] Viết kiểm thử `AuthServiceTest` (giả lập đăng ký trùng email, đăng nhập sai mật khẩu) và `ReviewServiceTest`.
* **Ngày 3 — Integration Test cho Controllers (Sản phẩm & Auth)**:
  - [ ] Viết các kiểm thử tích hợp sử dụng `MockMvc` và `@SpringBootTest` kết hợp cơ sở dữ liệu in-memory H2.
  - [ ] Kiểm thử luồng đăng ký -> đăng nhập -> lấy token -> dùng token lấy danh sách sản phẩm.
* **Ngày 4 — Integration Test cho Orders & Error Cases**:
  - [ ] Viết các kiểm thử tích hợp kiểm tra phân quyền đầu vào (gọi API không có token, token của User thường truy cập Admin Endpoint).
  - [ ] Kiểm thử xử lý lỗi ngoại lệ của Global Exception Handler khi truyền sai định dạng dữ liệu đầu vào.
* **Ngày 5 — Nghiệm thu và Làm sạch Source Code**:
  - [ ] Chạy câu lệnh `mvn clean test` trên terminal của dự án để kiểm tra toàn bộ các test case đảm bảo vượt qua 100%.
  - [ ] Rà soát lại code, xóa bỏ các import dư thừa, dòng log test hoặc code thừa không sử dụng.
  - [ ] Viết thêm Javadoc giải thích các phương thức phức tạp trong service và sẵn sàng bàn giao API hoàn chỉnh cho giai đoạn làm Frontend.

---


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
