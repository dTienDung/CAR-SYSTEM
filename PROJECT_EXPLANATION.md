# 🚗 HỆ THỐNG QUẢN LÝ BẢO HIỂM XE - CAR SYSTEM

## 📋 TỔNG QUAN DỰ ÁN

Hệ thống quản lý bảo hiểm xe là một ứng dụng web quản lý toàn bộ quy trình bảo hiểm xe từ khách hàng, thẩm định, hợp đồng đến thanh toán.

## 🏗️ KIẾN TRÚC HỆ THỐNG

### Backend (Spring Boot)
- **Framework**: Spring Boot 3.x
- **Database**: MySQL
- **Security**: JWT Authentication + Role-based Authorization
- **ORM**: JPA/Hibernate

### Frontend
- **HTML/CSS/JavaScript** (Vanilla JS)
- **RESTful API** communication

## 📦 CÁC MODULE CHÍNH

### 1. **Quản lý Người dùng (User Management)**
- Đăng ký, đăng nhập
- Quản lý tài khoản
- Phân quyền theo role

### 2. **Quản lý Khách hàng (Customer Management)**
- CRUD khách hàng
- Tìm kiếm khách hàng

### 3. **Quản lý Xe (Vehicle Management)**
- CRUD thông tin xe
- Liên kết với khách hàng
- Lịch sử tai nạn

### 4. **Quản lý Gói Bảo hiểm (Insurance Package)**
- Tạo và quản lý các gói bảo hiểm
- Phí cơ bản, quyền lợi

### 5. **Quản lý Hồ sơ Thẩm định (Appraisal File)**
- Tạo hồ sơ thẩm định
- Đánh giá theo tiêu chí
- Tính điểm rủi ro
- Xác định mức rủi ro: CHẤP NHẬN, XEM XÉT, TỪ CHỐI

### 6. **Quản lý Tiêu chí Thẩm định (Appraisal Criteria)**
- Quản lý các tiêu chí đánh giá
- Điểm tối đa, thứ tự hiển thị

### 7. **Ma trận Tính phí (Fee Calculation Matrix)**
- Xác định hệ số phí dựa trên điểm rủi ro
- Khoảng điểm rủi ro và hệ số tương ứng

### 8. **Quản lý Hợp đồng (Contract Management)**
- Tạo hợp đồng từ hồ sơ đã chấp nhận
- Tái tục hợp đồng
- Hủy hợp đồng
- Quản lý trạng thái hợp đồng

### 9. **Quản lý Thanh toán (Payment Management)**
- Thanh toán phí bảo hiểm
- Hoàn phí khi hủy hợp đồng
- Lịch sử giao dịch

### 10. **Báo cáo (Reports)**
- Báo cáo doanh thu
- Báo cáo tái tục
- Báo cáo thẩm định

## 🔐 HỆ THỐNG PHÂN QUYỀN

### Các Role trong hệ thống:

1. **ADMIN** (Quản trị viên)
   - ✅ Quyền truy cập TẤT CẢ các tính năng
   - Quản lý người dùng
   - Quản lý cấu hình hệ thống
   - Xem tất cả báo cáo

2. **MANAGER** (Quản lý)
   - ✅ Quản lý khách hàng, xe, gói bảo hiểm
   - ✅ Xem hồ sơ thẩm định, hợp đồng, thanh toán
   - ✅ Xem báo cáo
   - ❌ Không quản lý người dùng, tiêu chí, ma trận tính phí

3. **UNDERWRITER** (Nhân viên thẩm định)
   - ✅ Quản lý hồ sơ thẩm định
   - ✅ Quản lý tiêu chí thẩm định
   - ❌ Chỉ xem và xử lý hồ sơ thẩm định

4. **SALES** (Nhân viên kinh doanh)
   - ✅ Quản lý khách hàng, xe
   - ✅ Quản lý gói bảo hiểm
   - ✅ Xem và tạo hồ sơ thẩm định
   - ✅ Quản lý hợp đồng
   - ❌ Không quản lý thanh toán, tiêu chí, ma trận

5. **ACCOUNTANT** (Kế toán)
   - ✅ Quản lý hợp đồng
   - ✅ Quản lý thanh toán
   - ❌ Chỉ xử lý thanh toán và hợp đồng

### Bảng Phân quyền chi tiết:

| Tính năng | ADMIN | MANAGER | UNDERWRITER | SALES | ACCOUNTANT |
|-----------|-------|---------|-------------|-------|------------|
| Quản lý Người dùng | ✅ | ❌ | ❌ | ❌ | ❌ |
| Quản lý Khách hàng | ✅ | ✅ | ❌ | ✅ | ❌ |
| Quản lý Xe | ✅ | ✅ | ❌ | ✅ | ❌ |
| Quản lý Gói BH | ✅ | ✅ | ❌ | ✅ | ❌ |
| Hồ sơ Thẩm định | ✅ | ✅ | ✅ | ✅ | ❌ |
| Quản lý Hợp đồng | ✅ | ✅ | ❌ | ✅ | ✅ |
| Quản lý Thanh toán | ✅ | ✅ | ❌ | ❌ | ✅ |
| Tiêu chí Thẩm định | ✅ | ❌ | ✅ | ❌ | ❌ |
| Ma trận Tính phí | ✅ | ❌ | ❌ | ❌ | ❌ |
| Báo cáo | ✅ | ✅ | ❌ | ❌ | ❌ |

## 🔧 CÔNG NGHỆ SỬ DỤNG

### Backend:
- Spring Boot 3.x
- Spring Security (JWT)
- Spring Data JPA
- MySQL
- Lombok
- Jackson (JSON)

### Frontend:
- HTML5, CSS3
- Vanilla JavaScript
- RESTful API calls

## 📁 CẤU TRÚC THƯ MỤC

```
src/main/java/com/example/CAR_/SYSTEM/
├── annotation/          # Custom annotations (@RequireRole)
├── aspect/              # AOP aspects (RoleAuthorizationAspect)
├── config/              # Configuration (Security, JWT, CORS)
├── controller/          # REST Controllers
├── dto/                 # Data Transfer Objects
│   ├── request/         # Request DTOs
│   └── response/        # Response DTOs
├── filter/              # JWT Authentication Filter
├── model/              # Entity models
│   └── enums/          # Enumerations
├── repository/          # JPA Repositories
├── service/            # Service interfaces
│   └── impl/           # Service implementations
└── util/               # Utility classes

src/main/resources/
├── static/
│   ├── css/            # Stylesheets
│   ├── js/             # JavaScript files
│   │   ├── api.js      # API helper functions
│   │   ├── auth.js     # Authentication
│   │   └── role.js     # Role-based access control
│   └── *.html          # HTML pages
└── application.properties
```

## 🔄 QUY TRÌNH NGHIỆP VỤ

### 1. Tạo Hồ sơ Thẩm định:
1. Chọn khách hàng → Chọn xe → Chọn gói bảo hiểm
2. Đánh giá theo các tiêu chí thẩm định
3. Hệ thống tự động tính điểm rủi ro
4. Xác định mức rủi ro (CHẤP NHẬN/XEM XÉT/TỪ CHỐI)
5. Tính phí bảo hiểm dựa trên ma trận tính phí

### 2. Tạo Hợp đồng:
1. Chọn hồ sơ thẩm định đã được CHẤP NHẬN
2. Nhập ngày ký, ngày hiệu lực, ngày hết hạn
3. Hệ thống tự động tính phí bảo hiểm
4. Tạo hợp đồng với trạng thái DRAFT

### 3. Thanh toán:
1. Chọn hợp đồng cần thanh toán
2. Nhập số tiền thanh toán
3. Cập nhật trạng thái hợp đồng

## 🚀 CÁCH SỬ DỤNG

### 1. Khởi động Backend:
```bash
mvn spring-boot:run
```

### 2. Truy cập Frontend:
- Mở browser: `http://localhost:8080`
- Đăng nhập với tài khoản đã tạo

### 3. Tạo tài khoản Admin đầu tiên:
- Truy cập `/index.html`
- Đăng ký với role ADMIN
- Hoặc tạo trực tiếp trong database

## 🔒 BẢO MẬT

- **JWT Authentication**: Tất cả API (trừ `/api/auth/**`) yêu cầu JWT token
- **Role-based Authorization**: Mỗi endpoint được bảo vệ bởi `@RequireRole`
- **Password Encryption**: BCrypt
- **CORS**: Cấu hình cho phép cross-origin requests

## 📝 GHI CHÚ

- Tất cả API trả về format: `{success: boolean, data: any, message: string}`
- JWT token được lưu trong localStorage
- User info được lưu trong localStorage
- Frontend tự động ẩn menu items dựa trên role

## 🛠️ PHÁT TRIỂN TIẾP

- [ ] Thêm JWT refresh token
- [ ] Thêm audit log
- [ ] Thêm file upload cho tài liệu
- [ ] Thêm email notification
- [ ] Thêm dashboard charts
- [ ] Thêm export Excel/PDF

