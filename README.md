# 🎮 Toy Model Store — Website Bán Đồ Chơi Mô Hình

> Nền tảng thương mại điện tử chuyên bán đồ chơi mô hình (figures, mô hình lắp ráp, collectibles) với trải nghiệm mua sắm hiện đại, giao diện đẹp mắt và quản lý sản phẩm tiện lợi.

---

## 📋 Tổng Quan

### Mô tả
Website bán đồ chơi mô hình trực tuyến, phục vụ cộng đồng collector và người yêu thích mô hình. Website cung cấp trải nghiệm mua sắm mượt mà với giao diện premium, hỗ trợ tìm kiếm, lọc sản phẩm thông minh và quy trình thanh toán đơn giản.

### Đối tượng khách hàng
- Người sưu tầm mô hình (Gundam, Figure Anime, Marvel, DC...)
- Người chơi mô hình lắp ráp (Gunpla, Lego Technic, Warhammer...)
- Phụ huynh mua đồ chơi cho trẻ em
- Người mua quà tặng

### Danh mục sản phẩm chính
| Danh mục | Ví dụ |
|---|---|
| 🤖 Mô hình lắp ráp | Gunpla, Lego, Bandai Model Kit |
| 🦸 Figure nhân vật | Anime Figure, Marvel Legends, Hot Toys |
| 🚗 Mô hình tĩnh | Xe mô hình, máy bay, tàu chiến |
| 🎲 Board Game & Collectibles | Thẻ bài, Funko Pop, Gashapon |
| 🛠️ Phụ kiện & Dụng cụ | Sơn, keo, dụng cụ lắp ráp, tủ trưng bày |

---

## 🏗️ Kiến Trúc Hệ Thống

```
┌─────────────────────────────────────────────────┐
│              FRONTEND (Stitch + tùy chỉnh)       │
│         HTML5 / CSS3 / JavaScript (ES6+)         │
└───────────────────────┬──────────────────────────┘
                        │ REST API (JSON)
                        │ http://localhost:8080/api/*
┌───────────────────────┼──────────────────────────┐
│          BACKEND — Spring Boot 3 (Java 17+)      │
│              ┌────────▼────────┐                 │
│              │  Controller    │  REST API        │
│              └────────┬────────┘                 │
│              ┌────────▼────────┐                 │
│              │    Service     │  Business Logic  │
│              └────────┬────────┘                 │
│              ┌────────▼────────┐                 │
│              │  Repository    │  Spring Data JPA │
│              └────────┬────────┘                 │
│        ┌──────────────┼──────────────┐           │
│  ┌─────▼─────┐  ┌─────▼─────┐ ┌─────▼─────┐   │
│  │  MySQL 8  │  │Cloudinary │ │ Spring    │   │
│  │  Database │  │  Images   │ │ Security  │   │
│  └───────────┘  └───────────┘ └───────────┘   │
└──────────────────────────────────────────────────┘
```

---

## 📁 Cấu Trúc Dự Án

```
Toy_Store/
├── frontend/           # Frontend (Stitch + chỉnh sửa)
│   └── README.md       # Roadmap & hướng dẫn frontend
│
├── backend/            # Spring Boot REST API
│   └── README.md       # Roadmap & hướng dẫn backend
│
└── README.md           # Tổng quan dự án (file này)
```

### 📖 Tài liệu chi tiết

| Phần | Tài liệu | Mô tả |
|---|---|---|
| **Backend** | [`backend/README.md`](./backend/README.md) | ⭐ **Code trước** — Spring Boot 3, MySQL 8, REST API, JWT Auth |
| **Tiến độ Backend** | [`backend/PROGRESS.md`](./backend/PROGRESS.md) | 📊 Báo cáo tiến độ code từng tuần & review code chi tiết |
| **Frontend** | [`frontend/README.md`](./frontend/README.md) | Làm bằng Stitch, chỉnh sửa sau |

---

## 🛠️ Tech Stack Tổng Quan

| Layer | Công nghệ |
|---|---|
| **Frontend** | HTML5, CSS3, JavaScript (ES6+) — Tạo bằng Stitch, chỉnh sửa thủ công |
| **Backend** | Java 17+, Spring Boot 3, Spring Data JPA, Spring Security |
| **Database** | MySQL 8 |
| **Auth** | JWT (JSON Web Token) |
| **Image Storage** | Cloudinary |
| **Build Tool** | Maven |
| **Testing** | JUnit 5, Mockito, MockMvc |

---

## 📌 Thứ Tự Phát Triển

```
  ┌─────────────────────────────────────────────────┐
  │  1. BACKEND (code trước, test API kỹ)           │
  │     → Spring Boot + MySQL + JWT + Cloudinary     │
  │     → Unit Test + Integration Test               │
  ├─────────────────────────────────────────────────┤
  │  2. FRONTEND (làm bằng Stitch, chỉnh sửa sau)  │
  │     → Tạo giao diện với Stitch                   │
  │     → Kết nối API backend                        │
  │     → Chỉnh sửa, responsive, animations          │
  └─────────────────────────────────────────────────┘
```

---

## 🗓️ Roadmap 2 Tháng

> Tổng thời gian: **8 tuần Backend** + **4 tuần Frontend** ≈ 3 tháng chia làm 2 giai đoạn chính.
> Mỗi tuần code khoảng **3–5 buổi**, mỗi buổi **2–4 giờ**.

---

### 🔵 GIAI ĐOẠN 1 — BACKEND (Tuần 1 → 8)

---

#### 📦 Tuần 1 — Setup & Nền Tảng

> Mục tiêu: Có project Spring Boot chạy được, kết nối được MySQL, database tạo xong.

- [ ] **Cài đặt môi trường**
  - [ ] Cài JDK 17+, Maven 3.8+, MySQL 8, IntelliJ IDEA
  - [ ] Tạo tài khoản Cloudinary (free tier)
  - [ ] Tạo Git repo, cấu trúc thư mục `backend/`

- [ ] **Khởi tạo Spring Boot project** (`start.spring.io`)
  - [ ] Dependencies: Web, Data JPA, Security, MySQL Driver, Validation, Lombok, Flyway, DevTools
  - [ ] Cấu hình `application.yml` + `application-dev.yml`
  - [ ] Verify chạy được: `mvn spring-boot:run` → không lỗi

- [ ] **Database & Entities**
  - [ ] Tạo MySQL database `toy_store` + user `toystore`
  - [ ] Viết `V1__create_tables.sql` (Flyway migration): tạo bảng users, categories, products, orders, order_items, reviews
  - [ ] Tạo JPA Entities: `User`, `Category`, `Product`, `Order`, `OrderItem`, `Review`
  - [ ] Tạo Enums: `Role`, `OrderStatus`, `PaymentMethod`
  - [ ] Verify Flyway tự tạo bảng khi app khởi động

- [ ] **Global Infrastructure**
  - [ ] `ApiResponse<T>` — wrapper cho tất cả response
  - [ ] `GlobalExceptionHandler` (`@ControllerAdvice`)
  - [ ] Custom Exceptions: `ResourceNotFoundException`, `BadRequestException`, `DuplicateResourceException`
  - [ ] `PageResponse<T>` — wrapper cho phân trang
  - [ ] `CorsConfig` — cho phép frontend gọi API

- [ ] **DTOs & Mapper**
  - [ ] Request DTOs cho từng entity
  - [ ] Response DTOs cho từng entity
  - [ ] Utility: `SlugUtil` (tạo slug từ tên)

**✅ Checkpoint:** App khởi động, Flyway tạo bảng thành công, không compile error.

---

#### 📦 Tuần 2 — Category & Product API (Không Auth)

> Mục tiêu: Có thể GET/POST/PUT/DELETE categories và products qua Postman.

- [ ] **Category CRUD**
  - [ ] `CategoryRepository` (JPA)
  - [ ] `CategoryService` interface + `CategoryServiceImpl`
  - [ ] `CategoryController` — `GET /api/categories`, `GET /api/categories/{id}`, `GET /api/categories/slug/{slug}`
  - [ ] `AdminCategoryController` — POST, PUT, DELETE (tạm thời public, chưa cần auth)
  - [ ] **Test Postman:** CRUD danh mục ✅

- [ ] **Product CRUD**
  - [ ] `ProductRepository` + custom queries (tìm kiếm, lọc theo danh mục / brand / giá)
  - [ ] `ProductService` interface + `ProductServiceImpl`
  - [ ] `ProductController`:
    - `GET /api/products` (pageable: `?page=0&size=12&sort=createdAt,desc`, filter: `?categoryId=&brand=&minPrice=&maxPrice=`)
    - `GET /api/products/{id}`
    - `GET /api/products/slug/{slug}`
    - `GET /api/products/search?keyword=`
    - `GET /api/products/featured`
    - `GET /api/products/category/{categoryId}`
  - [ ] `AdminProductController` — POST, PUT, DELETE (tạm public)
  - [ ] **Test Postman:** CRUD sản phẩm, phân trang, tìm kiếm ✅

- [ ] **Seed dữ liệu mẫu**
  - [ ] `V2__seed_data.sql` — 5 danh mục + 20 sản phẩm mẫu

**✅ Checkpoint:** GET 20 sản phẩm, phân trang hoạt động, filter đúng kết quả.

---

#### 📦 Tuần 3 — Review API + JWT Auth Infrastructure

> Mục tiêu: Có Review API hoạt động. JWT infrastructure sẵn sàng.

- [ ] **Review API**
  - [ ] `ReviewRepository`
  - [ ] `ReviewService` + `ReviewServiceImpl`
  - [ ] `ReviewController`:
    - `GET /api/reviews/product/{productId}` (public)
    - `POST /api/reviews` (tạm public)
    - `PUT /api/reviews/{id}` (tạm public)
    - `DELETE /api/reviews/{id}` (tạm public)
  - [ ] Validate: 1 user chỉ đánh giá 1 sản phẩm 1 lần
  - [ ] **Test Postman:** CRUD review ✅

- [ ] **JWT Infrastructure**
  - [ ] Thêm dependency `jjwt` vào `pom.xml`
  - [ ] `JwtTokenProvider` — generateToken, validateToken, extractUsername, extractClaims
  - [ ] `CustomUserDetailsService` implements `UserDetailsService`
  - [ ] `JwtAuthFilter` extends `OncePerRequestFilter`

**✅ Checkpoint:** JWT filter load đúng, không break các API hiện có.

---

#### 📦 Tuần 4 — Authentication & Authorization

> Mục tiêu: Đăng ký, đăng nhập JWT hoạt động. Admin endpoints được bảo vệ.

- [ ] **Auth API**
  - [ ] `AuthService` + `AuthServiceImpl`:
    - `register()` — hash password BCrypt, lưu user, trả `AuthResponse`
    - `login()` — xác thực, generate access token + refresh token
    - `refreshToken()` — validate refresh token, cấp token mới
  - [ ] `AuthController`:
    - `POST /api/auth/register`
    - `POST /api/auth/login`
    - `POST /api/auth/refresh`
    - `GET /api/auth/me` (cần auth)

- [ ] **Spring Security Config**
  - [ ] `SecurityConfig` — `SecurityFilterChain`
  - [ ] Permit: `GET /api/products/**`, `GET /api/categories/**`, `GET /api/reviews/**`, `POST /api/auth/**`
  - [ ] Require `ROLE_USER`: `POST /api/reviews`, `POST /api/orders`, `GET /api/orders/my-orders`
  - [ ] Require `ROLE_ADMIN`: `/api/admin/**`
  - [ ] Thêm `JwtAuthFilter` vào filter chain

- [ ] **Test auth flow Postman:**
  - [ ] Đăng ký → Đăng nhập → Lấy token
  - [ ] Gọi API có auth với token → 200
  - [ ] Gọi API có auth không có token → 401
  - [ ] Gọi admin API với token USER → 403 ✅

**✅ Checkpoint:** Auth flow hoàn chỉnh, security đúng role.

---

#### 📦 Tuần 5 — Order API

> Mục tiêu: User tạo được đơn hàng, admin quản lý được trạng thái đơn.

- [ ] **Order Service & Repository**
  - [ ] `OrderRepository` + `OrderItemRepository`
  - [ ] `OrderService` + `OrderServiceImpl`:
    - `createOrder()` — validate stock, tính tổng tiền, tạo OrderItems, auto-generate order code (`TS-YYYYMMDD-XXXX`)
    - `getMyOrders()` — lấy đơn hàng của user đang đăng nhập
    - `getOrderById()` — chi tiết đơn (chỉ owner hoặc admin)
    - `cancelOrder()` — chỉ cancel nếu status = PENDING
    - `updateOrderStatus()` (admin) — validate transition hợp lệ

- [ ] **Order Controllers**
  - [ ] `OrderController` (user):
    - `POST /api/orders`
    - `GET /api/orders/my-orders`
    - `GET /api/orders/{id}`
    - `PUT /api/orders/{id}/cancel`
  - [ ] `AdminOrderController` (admin):
    - `GET /api/admin/orders`
    - `PUT /api/admin/orders/{id}/status`

- [ ] **Test Postman — full flow:**
  - [ ] Login → Tạo đơn → Xem đơn của mình
  - [ ] Admin login → Xem tất cả đơn → Cập nhật status PENDING → CONFIRMED → SHIPPING → DELIVERED
  - [ ] User thường cancel đơn PENDING → 200; cancel đơn CONFIRMED → 400 ✅

**✅ Checkpoint:** Order flow hoàn chỉnh từ tạo đến giao hàng.

---

#### 📦 Tuần 6 — User Profile & File Upload

> Mục tiêu: Quản lý profile user, upload ảnh sản phẩm lên Cloudinary.

- [ ] **User Profile API**
  - [ ] `PUT /api/auth/profile` — cập nhật fullName, phone, avatar
  - [ ] `PUT /api/auth/change-password` — validate old password, hash new password
  - [ ] **Test Postman ✅**

- [ ] **File Upload (Cloudinary)**
  - [ ] Thêm Cloudinary dependency vào `pom.xml`
  - [ ] `CloudinaryConfig` — cấu hình credentials từ `application-dev.yml`
  - [ ] `FileUploadService` + `FileUploadServiceImpl`:
    - `uploadImage(MultipartFile)` → trả về URL
    - `uploadImages(List<MultipartFile>)` → trả về list URL
    - `deleteImage(String publicId)` → xóa ảnh
  - [ ] `UploadController`:
    - `POST /api/upload/image` (admin)
    - `POST /api/upload/images` (admin)
    - `DELETE /api/upload/image` (admin)
  - [ ] Validate: chỉ cho phép jpg/png/webp, max 5MB
  - [ ] **Test Postman:** Upload ảnh → lấy URL → dùng URL tạo sản phẩm ✅

**✅ Checkpoint:** Upload ảnh lên Cloudinary thành công, URL trả về dùng được.

---

#### 📦 Tuần 7 — Admin Dashboard & Swagger

> Mục tiêu: Admin có thống kê tổng quan. Swagger docs hoàn chỉnh.

- [ ] **Admin Dashboard API**
  - [ ] `AdminUserController` — list users, activate/deactivate
  - [ ] `AdminDashboardController`:
    - `GET /api/admin/stats/dashboard` — trả về: tổng users, tổng sản phẩm, tổng đơn hàng, doanh thu tháng hiện tại
  - [ ] **Test Postman ✅**

- [ ] **Swagger / OpenAPI**
  - [ ] Thêm `springdoc-openapi-starter-webmvc-ui` vào `pom.xml`
  - [ ] `OpenApiConfig` — mô tả project, thêm JWT auth vào Swagger
  - [ ] Annotate các controllers với `@Tag`, `@Operation`, `@ApiResponse`
  - [ ] Verify Swagger UI tại `http://localhost:8080/swagger-ui.html`
  - [ ] Test được tất cả API từ Swagger UI ✅

**✅ Checkpoint:** Swagger UI đầy đủ, admin thấy được dashboard stats.

---

#### 📦 Tuần 8 — Testing & Hoàn Thiện Backend

> Mục tiêu: Backend ổn định, tất cả test pass, API docs đầy đủ.

- [ ] **Unit Tests (JUnit 5 + Mockito)**
  - [ ] `ProductServiceTest` — test createProduct, updateProduct, getProductById, search, filter
  - [ ] `CategoryServiceTest` — test CRUD
  - [ ] `OrderServiceTest` — test createOrder (validate stock), updateStatus (valid/invalid transitions)
  - [ ] `AuthServiceTest` — test register (duplicate email), login (wrong password)
  - [ ] `ReviewServiceTest` — test duplicate review

- [ ] **Integration Tests (MockMvc)**
  - [ ] `ProductControllerTest` — test GET endpoints, filter, pagination
  - [ ] `AuthControllerTest` — test register/login flow, JWT response
  - [ ] `OrderControllerTest` — test auth required, create order, status transitions

- [ ] **Kiểm tra toàn bộ**
  - [ ] `mvn test` → tất cả pass ✅
  - [ ] Test lại tất cả Postman endpoints một lượt cuối
  - [ ] Kiểm tra error handling: 400, 401, 403, 404, 500
  - [ ] Review code, xóa code thừa, thêm Javadoc cho các service

**✅ Checkpoint: Backend hoàn thành. API sẵn sàng cho Frontend.**

---

### 🟢 GIAI ĐOẠN 2 — FRONTEND (Tuần 9 → 12)

> Frontend tạo bằng **Stitch**, chỉnh sửa thủ công để kết nối API và tối ưu UX.
> Xem roadmap chi tiết tại [`frontend/README.md`](./frontend/README.md)

---

#### 🎨 Tuần 9 — Tạo Giao Diện Với Stitch

- [ ] Thiết kế & generate các trang chính với Stitch:
  - [ ] Trang chủ (Home) — Hero, sản phẩm nổi bật, danh mục
  - [ ] Trang danh sách sản phẩm — grid, sidebar filter
  - [ ] Trang chi tiết sản phẩm — gallery, tab mô tả/đánh giá
  - [ ] Trang giỏ hàng, thanh toán
  - [ ] Trang đăng nhập / đăng ký
  - [ ] Trang tài khoản (profile, lịch sử đơn)
- [ ] Dọn dẹp HTML/CSS output từ Stitch, tổ chức file structure

**✅ Checkpoint:** Tất cả trang render đúng, không lỗi layout.

---

#### 🎨 Tuần 10 — Kết Nối API Backend

- [ ] Tạo `js/api.js` — module gọi API (fetch wrapper, handle token)
- [ ] Kết nối trang chủ: load sản phẩm nổi bật, danh mục từ API
- [ ] Kết nối danh sách sản phẩm: paginate, filter, search real-time
- [ ] Kết nối chi tiết sản phẩm: load đánh giá, submit đánh giá
- [ ] Auth flow: đăng nhập → lưu JWT → redirect
- [ ] Giỏ hàng: localStorage + đồng bộ khi thanh toán
- [ ] Trang tài khoản: load profile, lịch sử đơn hàng

**✅ Checkpoint:** Tất cả trang kéo data thật từ backend, auth hoạt động.

---

#### 🎨 Tuần 11 — Responsive & UX Polish

- [ ] Responsive design — mobile/tablet/desktop
- [ ] Hamburger menu trên mobile
- [ ] Dark Mode toggle
- [ ] Loading skeleton
- [ ] Toast notifications
- [ ] Micro-animations & hover effects
- [ ] Image lazy loading

**✅ Checkpoint:** Website chạy tốt trên mobile, UX mượt mà.

---

#### 🎨 Tuần 12 — Kiểm Tra & Hoàn Thiện

- [ ] Test cross-browser (Chrome, Firefox, Safari)
- [ ] Test các edge case (hết hàng, lỗi mạng, token hết hạn)
- [ ] SEO: meta tags, Open Graph, heading structure
- [ ] Performance: Lighthouse audit > 85 điểm
- [ ] Sửa bug cuối
- [ ] Chuẩn bị deploy (Vercel cho FE, Railway/Render cho BE)

**✅ Checkpoint: Dự án hoàn thành. Sẵn sàng deploy.**

---

## 📊 Timeline Tổng Quan

| Tuần | Giai đoạn | Nội dung |
|---:|---|---|
| 1 | 🔵 Backend | Setup project, Database, Entities, Global Config |
| 2 | 🔵 Backend | Category API + Product API |
| 3 | 🔵 Backend | Review API + JWT Infrastructure |
| 4 | 🔵 Backend | Auth (Register/Login) + Spring Security |
| 5 | 🔵 Backend | Order API (tạo đơn, quản lý trạng thái) |
| 6 | 🔵 Backend | User Profile + File Upload (Cloudinary) |
| 7 | 🔵 Backend | Admin Dashboard + Swagger Docs |
| 8 | 🔵 Backend | Unit Tests + Integration Tests + Review toàn bộ |
| 9 | 🟢 Frontend | Tạo giao diện với Stitch |
| 10 | 🟢 Frontend | Kết nối API backend |
| 11 | 🟢 Frontend | Responsive + UX Polish |
| 12 | 🟢 Frontend | Testing + Bug fix + Chuẩn bị deploy |

---

> **Bắt đầu từ Backend** → Xem [`backend/README.md`](./backend/README.md) 🚀

