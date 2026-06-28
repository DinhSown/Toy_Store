# 📊 Báo Cáo Tiến Độ & Review Code Backend — Toy Model Store

Tài liệu này ghi nhận chi tiết tiến độ code theo từng tuần của phần Backend Spring Boot 3 + MySQL 8, bao gồm giải thích chi tiết các thành phần đã phát triển và review thiết kế code.

---

## 📅 Bảng Tổng Quan Tiến Độ

| Tuần | Nội dung công việc chính | Trạng thái | Ghi chú |
| :--- | :--- | :---: | :--- |
| **Tuần 1** | Khởi tạo dự án, JPA Entities, Database Migration, Global Infrastructure, DTOs & Mappers. | **Hoàn thành** | Đã test khởi động app & Flyway migration thành công. |
| **Tuần 2** | CRUD Category & Product API (Không Auth). | **Đang thực hiện** | Hoàn thành Ngày 1 (Category DB + Service & Unit Tests). |
| **Tuần 3** | Authentication & Security (JWT, Authorization). | *Chưa bắt đầu* | |
| **Tuần 4** | Shopping Cart & Order API (User). | *Chưa bắt đầu* | |
| **Tuần 5** | Order Management API (Admin). | *Chưa bắt đầu* | |
| **Tuần 6** | Cloudinary Image Upload & Admin Stats API. | *Chưa bắt đầu* | |
| **Tuần 7** | Swagger OpenAPI Integration & Unit Tests. | *Chưa bắt đầu* | |
| **Tuần 8** | Integration Tests & Code Review tối ưu hóa. | *Chưa bắt đầu* | |

---

## 📦 Chi Tiết Tuần 1 (Setup, Entities, Infrastructure, DTOs & Mappers)

### 1. Cấu Trúc Thư Mục Hiện Tại
```text
backend/src/main/java/com/toystore/
├── ToyStoreApplication.java (Main class)
├── config/
│   └── CorsConfig.java (Cấu hình CORS đa nguồn)
├── dto/
│   ├── request/
│   │   ├── UserRegisterRequest.java, UserLoginRequest.java, UserUpdateRequest.java
│   │   ├── CategoryRequest.java, ProductRequest.java, ReviewRequest.java
│   │   └── OrderRequest.java, OrderItemRequest.java
│   └── response/
│       ├── ApiResponse.java (API Wrapper đồng nhất)
│       ├── PageResponse.java (Wrapper cho phân trang)
│       ├── UserResponse.java, CategoryResponse.java, ProductResponse.java
│       └── ReviewResponse.java, OrderResponse.java, OrderItemResponse.java
├── entity/
│   ├── BaseEntity.java (Chứa id, createdAt, updatedAt kế thừa chung)
│   ├── User.java, Category.java, Product.java, Review.java
│   └── Order.java, OrderItem.java
├── enums/
│   └── Role.java, OrderStatus.java, PaymentMethod.java
├── exception/
│   ├── ResourceNotFoundException.java (Lỗi 404)
│   ├── BadRequestException.java (Lỗi logic 400)
│   ├── DuplicateResourceException.java (Lỗi trùng lặp dữ liệu 409)
│   └── GlobalExceptionHandler.java (Bắt và format mọi Exception thành ApiResponse)
├── mapper/
│   ├── UserMapper.java, CategoryMapper.java, ProductMapper.java
│   └── ReviewMapper.java, OrderMapper.java, OrderItemMapper.java
└── util/
    └── SlugUtil.java (Tạo slug tiếng Việt không dấu)
```

---

### 2. Giải Thích Chi Tiết Những Gì Đã Code

#### A. Database Migration (Flyway) & Entities
- **[V1__create_tables.sql](file:///d:/Code/Toy_Store/backend/src/main/resources/db/migration/V1__create_tables.sql)**: Khởi tạo cấu trúc các bảng vật lý trong MySQL 8 với đầy đủ Index, Foreign Key và ràng buộc UNIQUE (ví dụ: `uq_review_user_product` để đảm bảo mỗi khách hàng chỉ được đánh giá sản phẩm 1 lần).
- **[BaseEntity](file:///d:/Code/Toy_Store/backend/src/main/java/com/toystore/entity/BaseEntity.java)**: Sử dụng `@MappedSuperclass` kết hợp với `@EntityListeners(AuditingEntityListener.class)` của Spring Data JPA. Các trường `createdAt` và `updatedAt` tự động sinh giá trị thời gian thực tế mỗi khi bản ghi được thêm/sửa, giải phóng việc gán giá trị thủ công trong code nghiệp vụ.
- **Quan hệ cha-con tự liên kết (Self-referencing Category)**: 
  Trong [Category.java](file:///d:/Code/Toy_Store/backend/src/main/java/com/toystore/entity/Category.java), trường `parent` kiểu `Category` và danh sách `children` kiểu `List<Category>` cho phép thiết lập hệ thống danh mục đa cấp (như *Mô hình Lắp ráp* -> *Mô hình Gundam* -> *Gundam HG*).

#### B. Global Infrastructure
- **[ApiResponse<T>](file:///d:/Code/Toy_Store/backend/src/main/java/com/toystore/dto/response/ApiResponse.java)**: Chuẩn hóa toàn bộ dữ liệu trả về client dưới dạng:
  ```json
  {
    "success": true,
    "message": "Thành công",
    "data": { ... },
    "timestamp": "2026-06-25T13:40:00"
  }
  ```
- **[GlobalExceptionHandler](file:///d:/Code/Toy_Store/backend/src/main/java/com/toystore/exception/GlobalExceptionHandler.java)**: Bắt và định dạng tất cả các lỗi của hệ thống (như lỗi Validation `@Valid`, lỗi không tìm thấy resource, lỗi trùng lặp dữ liệu, lỗi quá tải dung lượng file upload) thành dạng `ApiResponse` chuẩn mực trước khi trả về client.

#### C. DTOs & MapStruct Mappers
- **[ProductMapper](file:///d:/Code/Toy_Store/backend/src/main/java/com/toystore/mapper/ProductMapper.java)**:
  - Sử dụng Jackson `ObjectMapper` để tự động hóa việc chuyển đổi chuỗi JSON của ảnh phụ (`images` trong DB) thành `List<String>` trong `ProductResponse`, giúp tối ưu hóa việc lưu trữ danh sách ảnh mà không cần tạo thêm bảng quan hệ cồng kềnh.
  - Tự động tính toán điểm rating trung bình (`averageRating`) và tổng lượt đánh giá (`reviewCount`) trực tiếp từ danh sách liên kết `reviews` của sản phẩm khi map sang `ProductResponse`.

#### D. Utility: `SlugUtil`
- **[SlugUtil](file:///d:/Code/Toy_Store/backend/src/main/java/com/toystore/util/SlugUtil.java)**: Sử dụng Java Regex kết hợp `java.text.Normalizer` để bóc tách dấu tiếng Việt chuẩn xác (kể cả chữ cái đặc biệt `đ`/`Đ`), hỗ trợ tự động tạo URL thân thiện SEO cho sản phẩm và danh mục.

---

## 📦 Chi Tiết Tuần 2 (Category & Product API — Không Auth)

### 1. Các File Đã Code trong Ngày 1
- **[CategoryRepository.java](file:///d:/Code/Toy_Store/backend/src/main/java/com/toystore/repository/CategoryRepository.java)**: Hỗ trợ tìm kiếm theo slug, kiểm tra trùng lặp tên/slug và lấy danh mục gốc (`parent IS NULL`).
- **[CategoryService.java](file:///d:/Code/Toy_Store/backend/src/main/java/com/toystore/service/CategoryService.java)**: Định nghĩa các nghiệp vụ CRUD cho danh mục.
- **[CategoryServiceImpl.java](file:///d:/Code/Toy_Store/backend/src/main/java/com/toystore/service/impl/CategoryServiceImpl.java)**: Triển khai logic nghiệp vụ:
  - Ghép cấu trúc cây danh mục đệ quy và lọc các con cháu đang hoạt động (`mapToResponse`).
  - Kiểm tra tránh chu kỳ lặp vòng khi chỉ định danh mục cha (`isDescendantOf`).
  - Đệ quy hủy kích hoạt (soft delete) toàn bộ cây con cháu khi danh mục cha bị vô hiệu hóa (`deactivateCategoryAndChildren`).
- **[CategoryServiceTest.java](file:///d:/Code/Toy_Store/backend/src/test/java/com/toystore/service/CategoryServiceTest.java)**: Viết 8 unit tests phủ sóng đầy đủ các tình huống nghiệp vụ trên.

---

## 🛠️ Review Code & Phân Tích Thiết Kế

### 1. Tại sao dùng MapStruct thay vì ModelMapper hoặc map thủ công?
- **Hiệu năng cao hơn**: ModelMapper sử dụng cơ chế Reflection (dò tìm kiểu dữ liệu lúc runtime) nên tốc độ rất chậm khi tải cao. MapStruct tạo code ánh xạ trực tiếp bằng các câu lệnh gán thuần túy Java (`set`/`get`) ngay tại thời điểm biên dịch (Compile-time), tối ưu bộ nhớ và CPU.
- **Dễ debug**: Vì MapStruct tạo mã nguồn thực tế tại thư mục `target/generated-sources/annotations`, chúng ta có thể mở xem trực tiếp cách thức dữ liệu được sao chép từ request sang entity, dễ dàng phát hiện lỗi sai logic.

### 2. Thiết kế Cơ chế Lưu trữ Ảnh Phụ (Images as JSON)
- Thay vì tạo thêm một bảng `product_images` với quan hệ một-nhiều (1-N) gây phát sinh truy vấn JOIN chậm, việc sử dụng kiểu dữ liệu `JSON` trong MySQL 8 cho cột `images` giúp lưu trữ danh sách URL ảnh trực tiếp trên bản ghi sản phẩm.
- Việc đọc và chuyển đổi sang JSON được tích hợp ngay trong `ProductMapper` một cách an toàn và không gây tốn tài nguyên.

### 3. Sửa lỗi Xung Đột Kiểu Dữ Liệu `rating` (TINYINT vs Integer)
- **Vấn đề**: Cột `rating` trong database được định nghĩa là `TINYINT` (chiếm 1 byte để tiết kiệm dung lượng do giá trị chỉ chạy từ 1-5). Tuy nhiên, thực thể `Review.java` khai báo kiểu dữ liệu là `Integer`. Khi cấu hình `ddl-auto: validate` hoạt động, Hibernate quét và báo lỗi không tương thích giữa `TINYINT` và `INTEGER`.
- **Review giải pháp**: Có hai cách xử lý:
  1. Thay Java field sang `Byte` hoặc khai báo `@Column(columnDefinition = "TINYINT")`. Tuy nhiên cách này đôi khi phát sinh cảnh báo phụ thuộc tùy thuộc vào JDBC Driver.
  2. Nâng cấp kiểu dữ liệu cột trong database lên `INT` nhưng vẫn giữ nguyên CHECK constraint giới hạn giá trị từ 1 đến 5.
  -> Chúng ta chọn cách **2** vì nó chuẩn hóa kiểu dữ liệu số nguyên trong Java và Hibernate, giúp loại bỏ hoàn toàn các lỗi validation khi khởi động app mà không làm ảnh hưởng đến tính toàn vẹn của dữ liệu.

### 4. Xử lý Tương Thích Lombok trên JDK 25
- Trong môi trường local của máy chủ chạy JDK 25.0.2 mới nhất, các phiên bản Lombok cũ sẽ gặp lỗi không thể khởi tạo compiler do cấu trúc nội bộ của class `TypeTag` trong JDK bị thay đổi.
- Việc nâng cấp Lombok lên phiên bản `1.18.36` trong `pom.xml` giúp đảm bảo khả năng tương thích biên dịch lâu dài khi chạy trên các phiên bản Java LTS mới nhất (Java 21 và Java 25).

### 5. Thiết Kế Tránh Vòng Lặp Tuần Hoàn (Cycle Prevention) của Danh Mục Cha-Con
- **Vấn đề**: Khi cập nhật danh mục cha của danh mục $A$, nếu người dùng chọn danh mục $B$ (vốn là con hoặc cháu của $A$) làm cha mới của $A$, sẽ gây ra lỗi vòng lặp tuần hoàn trong database (đứt gãy cây danh mục và gây tràn bộ nhớ StackOverflow khi duyệt cây đệ quy).
- **Review giải pháp**: 
  - Trong `CategoryServiceImpl.updateCategory`, chúng tôi sử dụng thuật toán đệ quy ngược dòng `isDescendantOf(newParent, category)` để tìm kiếm từ danh mục cha mới ngược lên gốc. 
  - Nếu phát hiện danh mục hiện tại là tổ tiên của danh mục cha mới, hệ thống ngay lập tức chặn lại và ném lỗi `BadRequestException`. Điều này giúp đảm bảo tính toàn vẹn của dữ liệu cây danh mục tuyệt đối ở mức nghiệp vụ.

### 6. Cascade Soft Delete trên Cây Danh Mục
- **Thiết kế**: Khi một danh mục bị ẩn (`isActive = false`), tất cả các danh mục con và cháu của nó cũng không được phép hiển thị trên giao diện của người dùng.
- **Giải pháp**: Phương thức đệ quy `deactivateCategoryAndChildren` được kích hoạt để duyệt toàn bộ cây con cháu và tự động lưu trạng thái `isActive = false` xuống DB cho tất cả chúng trong cùng một transaction. Điều này giúp đồng bộ dữ liệu sạch sẽ mà không đòi hỏi frontend phải xử lý phức tạp.

