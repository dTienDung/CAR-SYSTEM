# 📋 TỔNG HỢP API - HỆ THỐNG QUẢN LÝ BẢO HIỂM XE Ô TÔ

**Base URL:** `http://localhost:8080/api`

**Response Format:** Tất cả API trả về format chuẩn `ApiResponse<T>`
```json
{
  "success": true/false,
  "message": "Thông báo",
  "data": {...},
  "timestamp": "2025-11-23T20:00:00"
}
```

---

## 🔐 1. MODULE AUTHENTICATION & AUTHORIZATION

### 1.1. Đăng ký tài khoản
- **Endpoint:** `POST /api/auth/register`
- **Request Body:**
```json
{
  "username": "string (required, unique)",
  "password": "string (required, min 6 chars)",
  "hoTen": "string (required)",
  "email": "string (required, valid email, unique)",
  "soDienThoai": "string (optional)",
  "role": "ADMIN | MANAGER | UNDERWRITER | SALES | ACCOUNTANT (required)"
}
```
- **Response:** `LoginResponse` (token + user info)
- **Status:** 201 Created / 400 Bad Request

### 1.2. Đăng nhập
- **Endpoint:** `POST /api/auth/login`
- **Request Body:**
```json
{
  "username": "string (required)",
  "password": "string (required)"
}
```
- **Response:** `LoginResponse` (token + user info)
- **Status:** 200 OK / 401 Unauthorized

### 1.3. Lấy thông tin user hiện tại
- **Endpoint:** `GET /api/auth/me`
- **Response:** User info
- **Status:** 200 OK
- **Note:** Cần JWT authentication (đang phát triển)

---

## 👥 2. MODULE NGƯỜI DÙNG (USER)

### 2.1. Lấy danh sách tất cả người dùng
- **Endpoint:** `GET /api/users`
- **Response:** `List<User>`
- **Status:** 200 OK

### 2.2. Lấy chi tiết người dùng
- **Endpoint:** `GET /api/users/{id}`
- **Response:** `User`
- **Status:** 200 OK / 404 Not Found

### 2.3. Tạo người dùng mới
- **Endpoint:** `POST /api/users`
- **Request Body:**
```json
{
  "username": "string (required, unique)",
  "password": "string (required)",
  "hoTen": "string (required)",
  "email": "string (required, valid email, unique)",
  "soDienThoai": "string (optional)",
  "role": "ADMIN | MANAGER | UNDERWRITER | SALES | ACCOUNTANT (required)",
  "active": "boolean (default: true)",
  "ghiChu": "string (optional)"
}
```
- **Response:** `User`
- **Status:** 201 Created / 400 Bad Request

### 2.4. Cập nhật người dùng
- **Endpoint:** `PUT /api/users/{id}`
- **Request Body:** `UserDTO` (password optional khi update)
- **Response:** `User`
- **Status:** 200 OK / 400 Bad Request

### 2.5. Xóa người dùng
- **Endpoint:** `DELETE /api/users/{id}`
- **Response:** Success message
- **Status:** 200 OK / 404 Not Found

### 2.6. Lấy danh sách roles
- **Endpoint:** `GET /api/users/roles`
- **Response:** `List<Role>` (ADMIN, MANAGER, UNDERWRITER, SALES, ACCOUNTANT)
- **Status:** 200 OK

---

## 👤 3. MODULE KHÁCH HÀNG

### 3.1. Lấy danh sách khách hàng
- **Endpoint:** `GET /api/khach-hang?keyword={keyword}`
- **Query Params:**
  - `keyword` (optional): Tìm kiếm theo tên/CCCD/SĐT
- **Response:** `List<KhachHang>`
- **Status:** 200 OK

### 3.2. Lấy chi tiết khách hàng
- **Endpoint:** `GET /api/khach-hang/{id}`
- **Response:** `KhachHang`
- **Status:** 200 OK / 404 Not Found

### 3.3. Tạo khách hàng mới
- **Endpoint:** `POST /api/khach-hang`
- **Request Body:**
```json
{
  "hoTen": "string (required)",
  "cccd": "string (required, unique, 12 chars)",
  "ngaySinh": "date (required, format: yyyy-MM-dd)",
  "gioiTinh": "string (required)",
  "soDienThoai": "string (required, unique)",
  "email": "string (required, valid email, unique)",
  "diaChi": "string (optional)",
  "ngheNghiep": "string (optional)"
}
```
- **Response:** `KhachHang` (maKH tự sinh: KH0001, KH0002...)
- **Status:** 201 Created / 400 Bad Request

### 3.4. Cập nhật khách hàng
- **Endpoint:** `PUT /api/khach-hang/{id}`
- **Request Body:** `KhachHangDTO`
- **Response:** `KhachHang`
- **Status:** 200 OK / 400 Bad Request

### 3.5. Xóa khách hàng
- **Endpoint:** `DELETE /api/khach-hang/{id}`
- **Response:** Success message
- **Status:** 200 OK / 404 Not Found

---

## 🚗 4. MODULE XE (PHƯƠNG TIỆN)

### 4.1. Lấy danh sách xe
- **Endpoint:** `GET /api/xe?keyword={keyword}`
- **Query Params:**
  - `keyword` (optional): Tìm kiếm theo biển số/chủ xe
- **Response:** `List<Xe>`
- **Status:** 200 OK

### 4.2. Lấy chi tiết xe
- **Endpoint:** `GET /api/xe/{id}`
- **Response:** `Xe`
- **Status:** 200 OK / 404 Not Found

### 4.3. Thêm xe mới
- **Endpoint:** `POST /api/xe`
- **Request Body:**
```json
{
  "bienSo": "string (required)",
  "soKhung": "string (required, unique)",
  "soMay": "string (optional)",
  "hangXe": "string (required)",
  "dongXe": "string (required)",
  "namSanXuat": "integer (required)",
  "namDangKy": "integer (required)",
  "mauSac": "string (required)",
  "mucDichSuDung": "string (required)",
  "giaTriXe": "decimal (required, > 0)",
  "thongTinKyThuat": "string (optional)",
  "khachHangId": "long (required)"
}
```
- **Response:** `Xe` (maXe tự sinh: XE0001, XE0002...)
- **Status:** 201 Created / 400 Bad Request

### 4.4. Cập nhật thông tin xe
- **Endpoint:** `PUT /api/xe/{id}`
- **Request Body:** `XeDTO`
- **Response:** `Xe`
- **Status:** 200 OK / 400 Bad Request

### 4.5. Xóa xe
- **Endpoint:** `DELETE /api/xe/{id}`
- **Response:** Success message
- **Status:** 200 OK / 404 Not Found

---

## 📦 5. MODULE GÓI BẢO HIỂM

### 5.1. Lấy danh sách gói bảo hiểm
- **Endpoint:** `GET /api/goi-bao-hiem`
- **Response:** `List<GoiBaoHiem>`
- **Status:** 200 OK

### 5.2. Lấy chi tiết gói bảo hiểm
- **Endpoint:** `GET /api/goi-bao-hiem/{id}`
- **Response:** `GoiBaoHiem`
- **Status:** 200 OK / 404 Not Found

### 5.3. Tạo gói bảo hiểm
- **Endpoint:** `POST /api/goi-bao-hiem`
- **Request Body:**
```json
{
  "tenGoi": "string (required)",
  "moTa": "string (optional)",
  "phiCoBan": "decimal (required, > 0)",
  "quyenLoi": "string (optional)",
  "active": "boolean (default: true)"
}
```
- **Response:** `GoiBaoHiem` (maGoi tự sinh: GBH001, GBH002...)
- **Status:** 201 Created / 400 Bad Request

### 5.4. Cập nhật gói bảo hiểm
- **Endpoint:** `PUT /api/goi-bao-hiem/{id}`
- **Request Body:** `GoiBaoHiemDTO`
- **Response:** `GoiBaoHiem`
- **Status:** 200 OK / 400 Bad Request

### 5.5. Xóa gói bảo hiểm
- **Endpoint:** `DELETE /api/goi-bao-hiem/{id}`
- **Response:** Success message
- **Status:** 200 OK / 404 Not Found

---

## 📄 6. MODULE HỒ SƠ THẨM ĐỊNH

### 6.1. Lấy danh sách hồ sơ thẩm định
- **Endpoint:** `GET /api/ho-so-tham-dinh?trangThai={trangThai}&riskLevel={riskLevel}`
- **Query Params:**
  - `trangThai` (optional): MOI_TAO, DANG_THAM_DINH, CHAP_NHAN, TU_CHOI, XEM_XET
  - `riskLevel` (optional): CHAP_NHAN, XEM_XET, TU_CHOI
- **Response:** `List<HoSoThamDinh>`
- **Status:** 200 OK

### 6.2. Lấy chi tiết hồ sơ thẩm định
- **Endpoint:** `GET /api/ho-so-tham-dinh/{id}`
- **Response:** `HoSoThamDinh` (bao gồm chi tiết điểm từng tiêu chí)
- **Status:** 200 OK / 404 Not Found

### 6.3. Tạo hồ sơ thẩm định mới
- **Endpoint:** `POST /api/ho-so-tham-dinh`
- **Request Body:**
```json
{
  "khachHangId": "long (required)",
  "xeId": "long (required)",
  "goiBaoHiemId": "long (required)",
  "ghiChu": "string (optional)",
  "nguoiThamDinhId": "long (optional)"
}
```
- **Response:** `HoSoThamDinh` (maHS tự sinh: HS-YYYYMMDD-XXXX, RiskScore tự động tính)
- **Status:** 201 Created / 400 Bad Request
- **Note:** Tự động tính RiskScore và RiskLevel sau khi tạo

### 6.4. Cập nhật hồ sơ thẩm định
- **Endpoint:** `PUT /api/ho-so-tham-dinh/{id}`
- **Request Body:** `HoSoThamDinhDTO`
- **Response:** `HoSoThamDinh`
- **Status:** 200 OK / 400 Bad Request

### 6.5. Tính lại điểm rủi ro
- **Endpoint:** `POST /api/ho-so-tham-dinh/{id}/risk-score`
- **Response:** `RiskScoreDTO`
```json
{
  "riskScore": "integer",
  "riskLevel": "CHAP_NHAN | XEM_XET | TU_CHOI",
  "moTa": "string"
}
```
- **Status:** 200 OK / 400 Bad Request

### 6.6. Xóa hồ sơ thẩm định
- **Endpoint:** `DELETE /api/ho-so-tham-dinh/{id}`
- **Response:** Success message
- **Status:** 200 OK / 404 Not Found

### 6.7. Export Excel hồ sơ thẩm định
- **Endpoint:** `GET /api/ho-so-tham-dinh/export?trangThai={trangThai}&riskLevel={riskLevel}`
- **Query Params:**
  - `trangThai` (optional): MOI_TAO, DANG_THAM_DINH, CHAP_NHAN, TU_CHOI, XEM_XET
  - `riskLevel` (optional): CHAP_NHAN, XEM_XET, TU_CHOI
- **Response:** Excel file (.xlsx)
- **Filename:** `HoSoThamDinh_YYYYMMDD_HHmmss.xlsx`
- **Status:** 200 OK / 500 Internal Server Error
- **Content-Type:** `application/octet-stream`

---

## 📝 7. MODULE HỢP ĐỒNG

### 7.1. Lấy danh sách hợp đồng
- **Endpoint:** `GET /api/hop-dong?trangThai={trangThai}&khachHangId={id}&fromDate={date}&toDate={date}`
- **Query Params:**
  - `trangThai` (optional): DRAFT, PENDING_PAYMENT, ACTIVE, EXPIRED, CANCELLED, TERMINATED, RENEWED
  - `khachHangId` (optional): Filter theo khách hàng
  - `fromDate` (optional): Từ ngày (format: yyyy-MM-dd)
  - `toDate` (optional): Đến ngày (format: yyyy-MM-dd)
- **Response:** `List<HopDong>`
- **Status:** 200 OK

### 7.2. Lấy chi tiết hợp đồng
- **Endpoint:** `GET /api/hop-dong/{id}`
- **Response:** `HopDong`
- **Status:** 200 OK / 404 Not Found

### 7.3. Tạo hợp đồng mới
- **Endpoint:** `POST /api/hop-dong`
- **Request Body:**
```json
{
  "hoSoThamDinhId": "long (required)",
  "ngayKy": "date (required, format: yyyy-MM-dd)",
  "ngayHieuLuc": "date (required, format: yyyy-MM-dd)",
  "ngayHetHan": "date (required, format: yyyy-MM-dd)",
  "ghiChu": "string (optional)"
}
```
- **Response:** `HopDong` (maHD tự sinh: HD-YYYYMMDD-XXXX)
- **Status:** 201 Created / 400 Bad Request
- **Note:** Chỉ tạo được từ hồ sơ đã CHẤP NHẬN, tự động tính phí bảo hiểm

### 7.4. Cập nhật hợp đồng
- **Endpoint:** `PUT /api/hop-dong/{id}`
- **Request Body:** `HopDongDTO`
- **Response:** `HopDong`
- **Status:** 200 OK / 400 Bad Request

### 7.5. Tái tục hợp đồng
- **Endpoint:** `POST /api/hop-dong/{id}/renew`
- **Request Body:**
```json
{
  "ngayKy": "date (required)",
  "ngayHieuLuc": "date (required)",
  "ngayHetHan": "date (required)",
  "ghiChu": "string (optional)"
}
```
- **Response:** `HopDong` (hợp đồng mới, liên kết với hợp đồng cũ)
- **Status:** 200 OK / 400 Bad Request
- **Note:** Chỉ tái tục được hợp đồng ACTIVE hoặc EXPIRED

### 7.6. Hủy hợp đồng
- **Endpoint:** `POST /api/hop-dong/{id}/cancel`
- **Request Body:**
```json
{
  "lyDo": "string (optional)",
  "hoanPhi": "boolean (default: true)"
}
```
- **Response:** `HopDong`
- **Status:** 200 OK / 400 Bad Request

### 7.7. Xóa hợp đồng
- **Endpoint:** `DELETE /api/hop-dong/{id}`
- **Response:** Success message
- **Status:** 200 OK / 404 Not Found

### 7.8. Export Excel hợp đồng
- **Endpoint:** `GET /api/hop-dong/export?trangThai={trangThai}&khachHangId={id}&fromDate={date}&toDate={date}`
- **Query Params:**
  - `trangThai` (optional): DRAFT, PENDING_PAYMENT, ACTIVE, EXPIRED, CANCELLED, TERMINATED, RENEWED
  - `khachHangId` (optional): Filter theo khách hàng
  - `fromDate` (optional): Từ ngày (format: yyyy-MM-dd)
  - `toDate` (optional): Đến ngày (format: yyyy-MM-dd)
- **Response:** Excel file (.xlsx)
- **Filename:** `HopDong_YYYYMMDD_HHmmss.xlsx`
- **Status:** 200 OK / 500 Internal Server Error
- **Content-Type:** `application/octet-stream`

---

## 💰 8. MODULE THANH TOÁN

### 8.1. Lấy danh sách thanh toán
- **Endpoint:** `GET /api/thanh-toan`
- **Response:** `List<ThanhToan>`
- **Status:** 200 OK

### 8.2. Lấy danh sách thanh toán theo hợp đồng
- **Endpoint:** `GET /api/thanh-toan/hop-dong/{hopDongId}`
- **Response:** `List<ThanhToan>`
- **Status:** 200 OK

### 8.3. Lấy chi tiết thanh toán
- **Endpoint:** `GET /api/thanh-toan/{id}`
- **Response:** `ThanhToan`
- **Status:** 200 OK / 404 Not Found

### 8.4. Thực hiện thanh toán
- **Endpoint:** `POST /api/thanh-toan`
- **Request Body:**
```json
{
  "hopDongId": "long (required)",
  "soTien": "decimal (required, > 0)",
  "phuongThuc": "TIEN_MAT | CHUYEN_KHOAN | POS_THE (required)",
  "soTaiKhoan": "string (optional, nếu chuyển khoản)",
  "soThe": "string (optional, nếu POS/thẻ)",
  "ghiChu": "string (optional)"
}
```
- **Response:** `ThanhToan` (maTT tự sinh: TT-YYYYMMDD-XXXX)
- **Status:** 201 Created / 400 Bad Request
- **Note:** Tự động cập nhật trạng thái hợp đồng (ACTIVE khi thanh toán đủ)

### 8.5. Tạo giao dịch hoàn phí
- **Endpoint:** `POST /api/thanh-toan/giao-dich-hoan-phi`
- **Request Body:**
```json
{
  "hopDongId": "long (required)",
  "soTienHoan": "decimal (required)",
  "phuongThuc": "TIEN_MAT | CHUYEN_KHOAN | POS_THE (required)",
  "soTaiKhoan": "string (optional)",
  "ghiChu": "string (optional)"
}
```
- **Response:** `ThanhToan` (số tiền âm)
- **Status:** 201 Created / 400 Bad Request
- **Note:** Chỉ hoàn phí được cho hợp đồng đã CANCELLED

### 8.6. Xóa thanh toán
- **Endpoint:** `DELETE /api/thanh-toan/{id}`
- **Response:** Success message
- **Status:** 200 OK / 404 Not Found

---

## 🚨 9. MODULE LỊCH SỬ TAI NẠN

### 9.1. Lấy danh sách lịch sử tai nạn
- **Endpoint:** `GET /api/lich-su-tai-nan?xeId={xeId}`
- **Query Params:**
  - `xeId` (optional): Lọc theo xe
- **Response:** `List<LichSuTaiNanResponseDTO>`
```json
{
  "id": "long",
  "xeId": "long",
  "bienSo": "string",
  "khachHang": "string",
  "ngayXayRa": "date",
  "moTa": "string",
  "thietHai": "decimal",
  "diaDiem": "string"
}
```
- **Status:** 200 OK

### 9.2. Lấy chi tiết lịch sử tai nạn
- **Endpoint:** `GET /api/lich-su-tai-nan/{id}`
- **Response:** `LichSuTaiNanResponseDTO`
- **Status:** 200 OK / 404 Not Found

### 9.3. Tạo lịch sử tai nạn
- **Endpoint:** `POST /api/lich-su-tai-nan`
- **Request Body:**
```json
{
  "xe": {
    "id": "long (required)"
  },
  "ngayXayRa": "date (required, format: yyyy-MM-dd)",
  "moTa": "string (required)",
  "thietHai": "decimal (optional)",
  "diaDiem": "string (optional)"
}
```
- **Response:** `LichSuTaiNanResponseDTO`
- **Status:** 201 Created / 400 Bad Request

### 9.4. Xóa lịch sử tai nạn
- **Endpoint:** `DELETE /api/lich-su-tai-nan/{id}`
- **Response:** Success message
- **Status:** 200 OK / 404 Not Found

---

## 📊 10. MODULE BÁO CÁO & DASHBOARD

### 10.1. Báo cáo doanh thu chi tiết
- **Endpoint:** `GET /api/bao-cao/doanh-thu?fromDate={date}&toDate={date}&groupBy={groupBy}`
- **Query Params:**
  - `fromDate` (optional): Từ ngày (format: yyyy-MM-dd, default: 30 ngày trước)
  - `toDate` (optional): Đến ngày (format: yyyy-MM-dd, default: hôm nay)
  - `groupBy` (optional): day | week | month (default: day)
- **Response:**
```json
{
  "tongDoanhThu": "decimal",
  "soGiaoDich": "long",
  "doanhThuTrungBinh": "decimal",
  "doanhThuHomNay": "decimal",
  "doanhThuTuanNay": "decimal",
  "doanhThuThangNay": "decimal",
  "timeline": {"date": "amount"},
  "theoPhuongThucThanhToan": {"method": "amount"},
  "theoLoai": {"THU_PHI/HOAN_PHI": "amount"},
  "chiTiet": [{"maTT", "soTien", "phuongThuc", "ngayThanhToan"...}],
  "fromDate": "date",
  "toDate": "date",
  "groupBy": "string"
}
```
- **Status:** 200 OK

### 10.2. Báo cáo hợp đồng
- **Endpoint:** `GET /api/bao-cao/hop-dong?fromDate={date}&toDate={date}`
- **Query Params:**
  - `fromDate` (optional): Từ ngày
  - `toDate` (optional): Đến ngày
- **Response:**
```json
{
  "tongHopDong": "long",
  "theoTrangThai": {"status": "count"},
  "theoLoaiQuanHe": {"MOI/TAI_TUC": "count"},
  "tongPhiBaoHiem": "decimal",
  "tongDaThanhToan": "decimal",
  "tongConNo": "decimal",
  "hopDongSapHetHan": "long (30 ngày tới)",
  "topGoiBaoHiem": {"goiName": "count"},
  "chiTiet": [{"maHD", "khachHang", "xe", "tongPhi"...}],
  "fromDate": "date",
  "toDate": "date"
}
```
- **Status:** 200 OK

### 10.3. Báo cáo khách hàng
- **Endpoint:** `GET /api/bao-cao/khach-hang?fromDate={date}&toDate={date}`
- **Query Params:**
  - `fromDate` (optional): Từ ngày
  - `toDate` (optional): Đến ngày
- **Response:**
```json
{
  "tongKhachHang": "long",
  "theoGioiTinh": {"gender": "count"},
  "theoNgheNghiep": {"job": "count"},
  "theoDoTuoi": {"Dưới 25/25-34/35-44/45-54/55+": "count"},
  "topKhachHangNhieuXe": [{"maKH", "hoTen", "soXe"...}],
  "topKhachHangGiaTriCao": [{"maKH", "hoTen", "tongGiaTri", "soHopDong"...}],
  "fromDate": "date",
  "toDate": "date"
}
```
- **Status:** 200 OK

### 10.4. Báo cáo thẩm định
- **Endpoint:** `GET /api/bao-cao/tham-dinh?fromDate={date}&toDate={date}`
- **Query Params:**
  - `fromDate` (optional): Từ ngày
  - `toDate` (optional): Đến ngày
- **Response:**
```json
{
  "countByStatus": {"status": "count"},
  "avgRiskScore": "double",
  "totalPhi": "decimal",
  "details": [{"maHS", "khachHang", "bienSo", "goiBaoHiem", "riskScore", "riskLevel", "trangThai", "phiBaoHiem"}]
}
```
- **Status:** 200 OK

### 10.5. Dashboard - Vòng đời hợp đồng
- **Endpoint:** `GET /api/bao-cao/hop-dong-lifecycle`
- **Response:** `Map<String, Long>` - Số lượng hợp đồng theo trạng thái
```json
{
  "DRAFT": 5,
  "PENDING_PAYMENT": 10,
  "ACTIVE": 150,
  "EXPIRED": 30,
  "CANCELLED": 8,
  "RENEWED": 45
}
```
- **Status:** 200 OK

### 10.6. Dashboard - Kết quả thẩm định
- **Endpoint:** `GET /api/bao-cao/tham-dinh-result`
- **Response:** `Map<String, Long>` - Số lượng hồ sơ theo risk level
```json
{
  "CHAP_NHAN": 120,
  "XEM_XET": 45,
  "TU_CHOI": 15
}
```
- **Status:** 200 OK

### 10.7. Dashboard - Timeline doanh thu
- **Endpoint:** `GET /api/bao-cao/doanh-thu-timeline?days={days}&startDate={date}`
- **Query Params:**
  - `days` (optional): Số ngày (default: 21)
  - `startDate` (optional): Ngày bắt đầu (format: yyyy-MM-dd)
- **Response:**
```json
{
  "labels": ["01/12", "02/12", "03/12"...],
  "data": [1000000, 1500000, 2000000...],
  "days": 21,
  "startDate": "2025-11-01" (nếu có)
}
```
- **Status:** 200 OK

### 10.8. Dashboard - Tỷ lệ tái tục
- **Endpoint:** `GET /api/bao-cao/tai-tuc-rate?months={months}`
- **Query Params:**
  - `months` (optional): Số tháng (default: 6)
- **Response:**
```json
{
  "labels": ["Tháng 7", "Tháng 8"...],
  "renewed": [10, 15, 20...],
  "expired": [5, 8, 12...]
}
```
- **Status:** 200 OK

### 10.9. Dashboard - Top xe rủi ro cao
- **Endpoint:** `GET /api/bao-cao/top-risk-vehicles?limit={limit}`
- **Query Params:**
  - `limit` (optional): Số lượng (default: 10)
- **Response:**
```json
[
  {
    "bienSo": "30A-12345",
    "model": "Toyota Vios",
    "chuXe": "Nguyễn Văn A",
    "riskScore": 28,
    "riskLevel": "TU_CHOI",
    "xeId": 123
  }
]
```
- **Status:** 200 OK

---

## 🔑 BUSINESS RULES & VALIDATION

### Validation Rules:
1. **CCCD** khách hàng phải unique trong toàn hệ thống
2. **Email** khách hàng phải unique và hợp lệ
3. **Số điện thoại** khách hàng phải unique
4. **Số khung xe** (SoKhung) phải unique
5. **Username** và **Email** user phải unique

### Auto-generated Codes:
- **MaKH**: KH0001, KH0002, KH0003...
- **MaXe**: XE0001, XE0002, XE0003...
- **MaHD**: HD-YYYYMMDD-XXXX (ví dụ: HD-20251123-0001)
- **MaHS**: HS-YYYYMMDD-XXXX
- **MaTT**: TT-YYYYMMDD-XXXX
- **MaGoi**: GBH001, GBH002, GBH003...

### Risk Scoring:
- **RiskScore < 15**: CHẤP NHẬN (CHAP_NHAN)
- **RiskScore 15-24**: XEM XÉT (XEM_XET)
- **RiskScore ≥ 25**: TỪ CHỐI (TU_CHOI)

### Contract Status Flow:
- **DRAFT** → **PENDING_PAYMENT** → **ACTIVE** (khi thanh toán đủ)
- **ACTIVE** → **EXPIRED** (khi hết hạn)
- **ACTIVE/EXPIRED** → **RENEWED** (khi tái tục)
- **ACTIVE** → **CANCELLED** (khi hủy)

### Payment Status:
- **CHƯA THANH TOÁN**: Tổng thanh toán = 0
- **THANH TOÁN MỘT PHẦN**: Tổng thanh toán < Tổng phí
- **ĐÃ THANH TOÁN ĐỦ**: Tổng thanh toán ≥ Tổng phí

---

## 📝 NOTES

1. **Authentication**: Hiện tại tất cả API đều permit all để test. Khi triển khai JWT filter, cần cấu hình lại SecurityConfig.

2. **CORS**: Đã cấu hình cho phép tất cả origins. Production nên chỉ định cụ thể.

3. **Error Handling**: Tất cả lỗi trả về format `ApiResponse` với message mô tả.

4. **Date Format**: Sử dụng format `yyyy-MM-dd` cho tất cả date fields.

5. **Decimal Precision**: Sử dụng BigDecimal với precision 15, scale 2 cho các trường tiền tệ.

6. **⚠️ TIÊU CHÍ THẨM ĐỊNH & MA TRẬN TÍNH PHÍ**: 
   - Dữ liệu đã được **HARDCODE** trong constants (không thể thay đổi qua API/UI)
   - Xem: `TieuChiThamDinhConstants.java` và `MaTranTinhPhiConstants.java`
   - Tự động seed vào database khi app khởi động qua `DatabaseInitializer.java`
   - **Tiêu chí**: CT01 (Tuổi xe), CT02 (Mục đích), CT03 (Tuổi tài xế), CT04 (Giá trị xe) - Tổng 8 điểm
   - **Ma trận phí**: 0-2đ (hệ số 1.0), 3-5đ (hệ số 1.2), ≥6đ (hệ số 1.5)
   - **RiskLevel**: 0-2đ (CHẤP NHẬN), 3-5đ (XEM XÉT), ≥6đ (TỪ CHỐI)

---

## 📊 TỔNG KẾT

**Tổng số API Endpoints: 58 endpoints**

### Phân loại theo module:
- 🔐 Authentication & Authorization: **3 endpoints**
- 👥 User Management: **6 endpoints**
- 👤 Khách hàng: **5 endpoints**
- 🚗 Xe (Phương tiện): **5 endpoints**
- 📦 Gói bảo hiểm: **5 endpoints**
- 📄 Hồ sơ thẩm định: **7 endpoints** (bao gồm export Excel)
- 📝 Hợp đồng: **8 endpoints** (bao gồm export Excel)
- 💰 Thanh toán: **6 endpoints**
- 🚨 Lịch sử tai nạn: **4 endpoints**
- 📊 Báo cáo & Dashboard: **9 endpoints**

### Tính năng đặc biệt:
- ✅ Export Excel cho Hồ sơ thẩm định và Hợp đồng
- ✅ Dashboard analytics với 5 biểu đồ thống kê
- ✅ Báo cáo chi tiết (doanh thu, hợp đồng, khách hàng, thẩm định)
- ✅ Risk scoring và assessment tự động
- ✅ Auto-generated code cho tất cả entities

**Version:** 2.0.0  
**Last Updated:** 2025-12-13
