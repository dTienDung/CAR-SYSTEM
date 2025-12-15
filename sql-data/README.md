# HƯỚNG DẪN CHẠY CÁC FILE SQL INSERT DATA

## Mô tả
Thư mục này chứa các file SQL để insert dữ liệu mẫu cho hệ thống quản lý bảo hiểm xe ô tô (CAR-SYSTEM).

## ⚠️ LƯU Ý QUAN TRỌNG

### Dữ liệu HARDCODE (Tự động insert bởi DatabaseInitializer)
- **Tiêu chí thẩm định** (4 tiêu chí): Tự động insert từ `TieuChiThamDinhConstants.java`
- **Ma trận tính phí** (3 mức): Tự động insert từ `MaTranTinhPhiConstants.java`

➡️ **Không cần chạy file SQL cho 2 bảng này!**

## Thông tin dữ liệu

### Dữ liệu hardcode (auto-insert)
- **4 tiêu chí thẩm định** (CT01-CT04): Tuổi xe, Mục đích sử dụng, Tuổi tài xế, Giá trị xe
  - Mỗi tiêu chí: 0-2 điểm
  - Tổng điểm tối đa: 8 điểm
- **3 ma trận tính phí**: 
  - 0-2 điểm: hệ số 1.0 (LOW)
  - 3-5 điểm: hệ số 1.2 (MEDIUM)
  - 6-8 điểm: hệ số 1.5 (HIGH)

### Dữ liệu từ file SQL
- **4 users** với các vai trò: ADMIN, UNDERWRITER, SALES, ACCOUNTANT
- **30 khách hàng** với thông tin đầy đủ
- **15 xe** đã đăng ký
- **6 gói bảo hiểm** đa dạng
- **14 lịch sử tai nạn**
- **15 hồ sơ thẩm định** (11 hoàn thành, 4 đang xử lý)
- **44 chi tiết thẩm định** (11 hồ sơ × 4 tiêu chí)
- **11 hợp đồng** bảo hiểm
- **12 giao dịch thanh toán**

## Thứ tự chạy các file (QUAN TRỌNG!)

**Lưu ý:** Phải chạy theo đúng thứ tự dưới đây vì có mối quan hệ phụ thuộc giữa các bảng.

### File SQL cần chạy (9 files)

```sql
-- Bước 1: Insert Users (4 tài khoản)
source 01_insert_users.sql;

-- Bước 2: Insert Khách hàng (30 người)
source 02_insert_khach_hang.sql;

-- Bước 3: Insert Xe (15 xe, phụ thuộc: khach_hang)
source 03_insert_xe.sql;

-- Bước 4: Insert Gói bảo hiểm (6 gói)
source 04_insert_goi_bao_hiem.sql;

-- Bước 5: Insert Lịch sử tai nạn (14 tai nạn, phụ thuộc: xe)
source 05_insert_lich_su_tai_nan.sql;

-- Bước 6: Insert Hồ sơ thẩm định (15 hồ sơ, phụ thuộc: khach_hang, xe, goi_bao_hiem, users)
source 06_insert_ho_so_tham_dinh.sql;

-- Bước 7: Insert Chi tiết thẩm định (44 records, phụ thuộc: ho_so_tham_dinh, tieu_chi_tham_dinh)
source 07_insert_chi_tiet_tham_dinh.sql;

-- Bước 8: Insert Hợp đồng (11 hợp đồng, phụ thuộc: khach_hang, xe, ho_so_tham_dinh, goi_bao_hiem, users)
source 08_insert_hop_dong.sql;

-- Bước 9: Insert Thanh toán (12 giao dịch, phụ thuộc: hop_dong, users)
source 09_insert_thanh_toan.sql;
```

## Phân quyền Users

### 1. **ADMIN** (admin)
- ✅ Quyền truy cập TẤT CẢ các tính năng
- Quản lý người dùng
- Quản lý cấu hình hệ thống
- Xem tất cả báo cáo

### 2. **UNDERWRITER** (underwriter) - Nhân viên thẩm định
- ✅ Quản lý hồ sơ thẩm định
- ✅ Quản lý tiêu chí thẩm định
- ❌ Chỉ xem và xử lý hồ sơ thẩm định

### 3. **SALES** (sales) - Nhân viên kinh doanh
- ✅ Quản lý khách hàng, xe
- ✅ Quản lý gói bảo hiểm
- ✅ Xem và tạo hồ sơ thẩm định
- ✅ Quản lý hợp đồng (tạo hợp đồng)
- ❌ Không quản lý thanh toán, tiêu chí, ma trận

### 4. **ACCOUNTANT** (accountant) - Kế toán
- ✅ Quản lý hợp đồng
- ✅ Quản lý thanh toán (thu tiền)
- ❌ Chỉ xử lý thanh toán và hợp đồng

**Password mặc định:** `password123` (cho tất cả users)

## Cách chạy

### Cách 1: Chạy từng file một (Khuyến nghị)

Trong MySQL/PostgreSQL client:

```sql
source 01_insert_users.sql;
source 02_insert_khach_hang.sql;
source 03_insert_xe.sql;
source 04_insert_goi_bao_hiem.sql;
source 05_insert_lich_su_tai_nan.sql;
source 06_insert_ho_so_tham_dinh.sql;
source 07_insert_chi_tiet_tham_dinh.sql;
source 08_insert_hop_dong.sql;
source 09_insert_thanh_toan.sql;
```

### Cách 2: Sử dụng Command Line

```bash
# Đối với PostgreSQL
psql -U username -d database_name -f 01_insert_users.sql
psql -U username -d database_name -f 02_insert_khach_hang.sql
# ... tiếp tục với các file khác

# Đối với MySQL
mysql -u username -p database_name < 01_insert_users.sql
mysql -u username -p database_name < 02_insert_khach_hang.sql
# ... tiếp tục với các file khác
```

## Xóa dữ liệu (nếu cần)

Nếu cần xóa dữ liệu và chạy lại, chạy theo thứ tự NGƯỢC LẠI:

```sql
DELETE FROM thanh_toan;
DELETE FROM hop_dong;
DELETE FROM chi_tiet_tham_dinh;
DELETE FROM ho_so_tham_dinh;
DELETE FROM lich_su_tai_nan;
DELETE FROM goi_bao_hiem;
DELETE FROM xe;
DELETE FROM khach_hang;
DELETE FROM users WHERE username != 'admin'; -- Giữ lại admin nếu cần

-- LƯU Ý: KHÔNG xóa 2 bảng này (hardcode tự động insert):
-- DELETE FROM tieu_chi_tham_dinh;
-- DELETE FROM ma_tran_tinh_phi;
```

## Mô tả chi tiết từng file

### 01_insert_users.sql
- 4 users theo Role enum
- Vai trò: ADMIN, UNDERWRITER, SALES, ACCOUNTANT
- Password: `password123` (đã mã hóa bcrypt)

### 02_insert_khach_hang.sql
- 30 khách hàng với thông tin đầy đủ
- CCCD, ngày sinh, địa chỉ, nghề nghiệp

### 03_insert_xe.sql
- 15 xe thuộc các khách hàng
- Đa dạng: Toyota, Honda, Mazda, Mercedes-Benz, BMW, Ford, Kia, Vinfast, Hyundai
- Giá trị: 320 triệu - 3.5 tỷ đồng

### 04_insert_goi_bao_hiem.sql
- 6 gói: Cơ Bản, Tiêu Chuẩn, Toàn Diện, VIP, Kinh Doanh, Linh Hoạt
- Phí: 3-15 triệu đồng

### 05_insert_lich_su_tai_nan.sql
- 14 tai nạn cho các xe
- Mức độ: nhẹ đến nghiêm trọng

### 06_insert_ho_so_tham_dinh.sql
- 15 hồ sơ (11 hoàn thành, 4 đang xử lý)
- Risk Score: 0-8 điểm
- Risk Level: LOW (0-2), MEDIUM (3-5), HIGH (6-8)

### 07_insert_chi_tiet_tham_dinh.sql
- 44 bản ghi (11 hồ sơ hoàn thành × 4 tiêu chí)
- 4 tiêu chí hardcode: CT01, CT02, CT03, CT04
- Điểm: 0-2 cho mỗi tiêu chí

### 08_insert_hop_dong.sql
- 11 hợp đồng (tạo bởi SALES)
- Tất cả đang hiệu lực
- Thời hạn: 1 năm

### 09_insert_thanh_toan.sql
- 12 giao dịch (xử lý bởi ACCOUNTANT)
- Phương thức: Tiền mặt, Chuyển khoản, Thẻ

## Kiểm tra sau khi chạy

```sql
-- Kiểm tra số lượng bản ghi
SELECT 'users' as table_name, COUNT(*) as count FROM users
UNION ALL SELECT 'khach_hang', COUNT(*) FROM khach_hang
UNION ALL SELECT 'xe', COUNT(*) FROM xe
UNION ALL SELECT 'goi_bao_hiem', COUNT(*) FROM goi_bao_hiem
UNION ALL SELECT 'tieu_chi_tham_dinh', COUNT(*) FROM tieu_chi_tham_dinh
UNION ALL SELECT 'ma_tran_tinh_phi', COUNT(*) FROM ma_tran_tinh_phi
UNION ALL SELECT 'lich_su_tai_nan', COUNT(*) FROM lich_su_tai_nan
UNION ALL SELECT 'ho_so_tham_dinh', COUNT(*) FROM ho_so_tham_dinh
UNION ALL SELECT 'chi_tiet_tham_dinh', COUNT(*) FROM chi_tiet_tham_dinh
UNION ALL SELECT 'hop_dong', COUNT(*) FROM hop_dong
UNION ALL SELECT 'thanh_toan', COUNT(*) FROM thanh_toan;
```

### Kết quả mong đợi:
- users: 4 ✅
- khach_hang: 30 ✅
- xe: 15 ✅
- goi_bao_hiem: 6 ✅
- tieu_chi_tham_dinh: 4 ✅ (auto-insert)
- ma_tran_tinh_phi: 3 ✅ (auto-insert)
- lich_su_tai_nan: 14 ✅
- ho_so_tham_dinh: 15 ✅
- chi_tiet_tham_dinh: 44 ✅
- hop_dong: 11 ✅
- thanh_toan: 12 ✅

## Lưu ý

1. **DatabaseInitializer sẽ tự động insert:**
   - Tiêu chí thẩm định (4 records)
   - Ma trận tính phí (3 records)

2. **Đảm bảo database đã được tạo** trước khi chạy các file SQL

3. **Chạy đúng thứ tự** để tránh lỗi foreign key

4. Nếu gặp lỗi, xóa dữ liệu và chạy lại từ đầu

5. Password mặc định: `password123`

6. Dữ liệu phù hợp cho môi trường development/testing

## Hỗ trợ

Nếu có vấn đề khi chạy các file SQL:
- Kiểm tra phiên bản database
- Kiểm tra các bảng đã được tạo đầy đủ
- Kiểm tra Foreign key constraints
- Kiểm tra Charset/Collation (utf8mb4 cho tiếng Việt)
- Đảm bảo DatabaseInitializer đã chạy để insert dữ liệu hardcode
