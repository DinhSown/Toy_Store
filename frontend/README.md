# 🎨 Toy Model Store — Frontend

> Giao diện frontend cho website bán đồ chơi mô hình. Tạo bằng **Stitch**, chỉnh sửa thủ công sau.

---

## 📌 Ghi Chú

Frontend sẽ được làm bằng **Stitch** và chỉnh sửa thủ công. Phần này sẽ được thực hiện **sau khi backend hoàn thành** và API đã được test kỹ.

---

## 🛠️ Tech Stack

| Công nghệ | Mục đích |
|---|---|
| **Stitch** | Tạo giao diện ban đầu |
| **HTML5** | Cấu trúc trang |
| **CSS3** (Vanilla) | Styling, responsive design, animations |
| **JavaScript** (ES6+) | Logic ứng dụng, DOM manipulation, kết nối API |
| **Google Fonts** | Typography (Inter / Outfit) |

---

## 🗺️ Roadmap

### Phase 1 — Tạo Giao Diện Với Stitch

- [ ] Trang Chủ (Home) — Hero banner, sản phẩm nổi bật, danh mục
- [ ] Trang Danh Sách Sản Phẩm (Product Listing) — Grid, filter, sort
- [ ] Trang Chi Tiết Sản Phẩm (Product Detail) — Gallery, thông tin, đánh giá
- [ ] Trang Giỏ Hàng (Cart)
- [ ] Trang Thanh Toán (Checkout)
- [ ] Trang Đăng Nhập / Đăng Ký
- [ ] Trang Tài Khoản (Profile)
- [ ] Các trang phụ (About, Contact, 404)

### Phase 2 — Chỉnh Sửa & Kết Nối API

- [ ] Chỉnh sửa HTML/CSS từ Stitch cho phù hợp
- [ ] Responsive design (mobile-first)
- [ ] Kết nối với Backend API (`http://localhost:8080/api/*`)
- [ ] Xử lý Authentication (JWT token trong localStorage/cookie)
- [ ] Giỏ hàng (localStorage + API sync)
- [ ] Tìm kiếm & lọc sản phẩm
- [ ] Form validation

### Phase 3 — Polish & Nâng Cao

- [ ] Dark Mode
- [ ] Micro-animations & transitions
- [ ] Loading skeleton
- [ ] Toast notifications
- [ ] Image lazy loading
- [ ] SEO (meta tags, Open Graph)
- [ ] Performance optimization

---

## 🎨 Design Guidelines

### Bảng Màu

| Token | Giá trị | Mục đích |
|---|---|---|
| `--color-primary` | `#6C5CE7` | Màu chủ đạo (tím modern) |
| `--color-secondary` | `#00CEC9` | Màu phụ (xanh ngọc) |
| `--color-accent` | `#FD79A8` | Điểm nhấn (hồng san hô) |
| `--color-bg` | `#0F0F1A` | Nền dark mode |
| `--color-surface` | `#1A1A2E` | Surface / card background |
| `--color-text` | `#E8E8F0` | Text chính |
| `--color-text-muted` | `#8888AA` | Text phụ |
| `--color-success` | `#00B894` | Trạng thái thành công |
| `--color-warning` | `#FDCB6E` | Cảnh báo |
| `--color-danger` | `#FF6B6B` | Lỗi / nguy hiểm |

### Typography
- **Heading**: `Outfit` — Bold, modern, eye-catching
- **Body**: `Inter` — Clean, readable
- **Scale**: 12 / 14 / 16 / 18 / 20 / 24 / 32 / 40 / 48px

### Hiệu Ứng
- Glassmorphism cho cards & modals
- Gradient overlays cho banners
- Smooth transitions (300ms ease)
- Hover scale & glow effects
- Skeleton loading screens

---

## 📡 API Base URL

```
Development: http://localhost:8080/api
Production:  https://api.toystore.com/api
```

Xem chi tiết API endpoints tại [`backend/README.md`](../backend/README.md).
