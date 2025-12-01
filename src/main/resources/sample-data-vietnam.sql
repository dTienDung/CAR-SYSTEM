-- =====================================================
-- DỮ LIỆU MẪU VIỆT NAM - HỆ THỐNG BẢO HIỂM XE
-- MySQL Database - KHỚP VỚI SCHEMA
-- =====================================================

SET FOREIGN_KEY_CHECKS = 0;
SET SQL_SAFE_UPDATES = 0;

-- Xóa dữ liệu cũ (giữ user admin)
DELETE FROM thanh_toan;
DELETE FROM hop_dong;
DELETE FROM chi_tiet_tham_dinh;
DELETE FROM ho_so_tham_dinh;
DELETE FROM lich_su_tai_nan;
DELETE FROM xe;
DELETE FROM khach_hang;
DELETE FROM ma_tran_tinh_phi;
DELETE FROM tieu_chi_tham_dinh;
DELETE FROM goi_bao_hiem;
DELETE FROM users WHERE email NOT LIKE '%admin%';

SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================
-- 1. NGƯỜI DÙNG
-- =====================================================
INSERT INTO users (id, username, ho_ten, email, password, role, active, created_at, updated_at) VALUES
(1, 'admin', 'Quản trị viên', 'admin@baohiem.vn', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', 1, NOW(), NOW()),
(2, 'manager', 'Nguyễn Thị Hoa', 'manager@baohiem.vn', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'MANAGER', 1, NOW(), NOW()),
(3, 'staff1', 'Trần Văn Hùng', 'staff1@baohiem.vn', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'STAFF', 1, NOW(), NOW()),
(4, 'staff2', 'Lê Thị Lan', 'staff2@baohiem.vn', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'STAFF', 1, NOW(), NOW());

-- =====================================================
-- 2. KHÁCH HÀNG
-- =====================================================
INSERT INTO khach_hang (id, ma_kh, ho_ten, ngay_sinh, gioi_tinh, so_dien_thoai, email, dia_chi, cccd, nghe_nghiep, created_at, updated_at) VALUES
(1, 'KH-20241101-0001', 'Nguyễn Văn An', '1985-03-15', 'NAM', '0901234567', 'nva@gmail.com', '123 Lê Lợi, Q1, TP.HCM', '079085001234', 'Văn phòng', NOW(), NOW()),
(2, 'KH-20241102-0002', 'Trần Thị Bình', '1990-07-20', 'NU', '0902345678', 'ttbinh@gmail.com', '456 Nguyễn Huệ, Q1, TP.HCM', '079090005678', 'Giáo viên', NOW(), NOW()),
(3, 'KH-20241103-0003', 'Lê Văn Cường', '1982-11-10', 'NAM', '0903456789', 'lvcuong@gmail.com', '789 Trần Hưng Đạo, Q5, TP.HCM', '079082009012', 'Kinh doanh', NOW(), NOW()),
(4, 'KH-20241104-0004', 'Phạm Thị Dung', '1988-05-25', 'NU', '0904567890', 'ptdung@gmail.com', '321 Hai Bà Trưng, Q3, TP.HCM', '079088003456', 'Văn phòng', NOW(), NOW()),
(5, 'KH-20241105-0005', 'Hoàng Văn Em', '1995-09-05', 'NAM', '0905678901', 'hvem@gmail.com', '654 Võ Văn Tần, Q3, TP.HCM', '079095007890', 'Lái xe', NOW(), NOW());

-- =====================================================
-- 3. XE (ĐÚNG SCHEMA)
-- =====================================================
INSERT INTO xe (id, ma_xe, bien_so, so_khung, so_may, hang_xe, dong_xe, nam_san_xuat, nam_dang_ky, mau_sac, muc_dich_su_dung, gia_tri_xe, thong_tin_ky_thuat, khach_hang_id, created_at, updated_at) VALUES
(1, 'XE-20241101-0001', '51A-12345', 'VNTKB1234567890', 'TK123456', 'Toyota', 'Vios', 2020, 2020, 'Trắng', 'Cá nhân', 450000000, '1.5L, Số tự động', 1, NOW(), NOW()),
(2, 'XE-20241102-0002', '51B-23456', 'VNHC2345678901', 'HC234567', 'Honda', 'City', 2019, 2019, 'Đen', 'Cá nhân', 480000000, '1.5L, CVT', 2, NOW(), NOW()),
(3, 'XE-20241103-0003', '51C-34567', 'VNMZ3456789012', 'MZ345678', 'Mazda', 'CX-5', 2021, 2021, 'Đỏ', 'Cá nhân', 850000000, '2.0L, Số tự động', 3, NOW(), NOW()),
(4, 'XE-20241104-0004', '51D-45678', 'VNHY4567890123', 'HY456789', 'Hyundai', 'Accent', 2018, 2018, 'Bạc', 'Cá nhân', 380000000, '1.4L, Số sàn', 4, NOW(), NOW()),
(5, 'XE-20241105-0005', '51E-56789', 'VNKI5678901234', 'KI567890', 'Kia', 'Morning', 2017, 2017, 'Vàng', 'Cá nhân', 250000000, '1.0L, Số sàn', 5, NOW(), NOW());

-- =====================================================
-- 4. GÓI BẢO HIỂM
-- =====================================================
INSERT INTO goi_bao_hiem (id, ma_goi, ten_goi, phi_co_ban, mo_ta, active, created_at, updated_at) VALUES
(1, 'GOI-001', 'Gói Tiết kiệm', 5000000, 'Bảo hiểm vật chất cơ bản', 1, NOW(), NOW()),
(2, 'GOI-002', 'Gói Tiêu chuẩn', 8000000, 'Bảo hiểm toàn diện', 1, NOW(), NOW()),
(3, 'GOI-003', 'Gói Cao cấp', 12000000, 'Bảo hiểm toàn diện + Phụ trợ', 1, NOW(), NOW());

-- =====================================================
-- 5. TIÊU CHÍ THẨM ĐỊNH
-- =====================================================
INSERT INTO tieu_chi_tham_dinh (id, ma_tieu_chi, ten_tieu_chi, diem_toi_da, mo_ta, dieu_kien, thu_tu, active, created_at, updated_at) VALUES
(1, 'TC-001', 'Tuổi xe', 10, 'Đánh giá theo năm sản xuất', 'Xe > 10 năm: 8-10đ', 1, 1, NOW(), NOW()),
(2, 'TC-002', 'Giá trị xe', 8, 'Đánh giá theo trị giá', 'Xe > 1tỷ: 6-8đ', 2, 1, NOW(), NOW()),
(3, 'TC-003', 'Lịch sử tai nạn', 12, 'Số lần tai nạn', 'Mỗi lần: +4đ', 3, 1, NOW(), NOW());

-- =====================================================
-- 6. MA TRẬN TÍNH PHÍ
-- =====================================================
INSERT INTO ma_tran_tinh_phi (id, diem_rui_ro_tu, diem_rui_ro_den, he_so_phi, mo_ta, active, created_at, updated_at) VALUES
(1, 0, 10, 1.0, 'Rủi ro thấp', 1, NOW(), NOW()),
(2, 11, 14, 1.2, 'Rủi ro TB thấp', 1, NOW(), NOW()),
(3, 15, 20, 1.5, 'Rủi ro TB', 1, NOW(), NOW()),
(4, 21, 24, 1.8, 'Rủi ro TB cao', 1, NOW(), NOW()),
(5, 25, 30, 2.2, 'Rủi ro cao', 1, NOW(), NOW());

-- =====================================================
-- 7. HỒ SƠ THẨM ĐỊNH (ĐÚNG SCHEMA)
-- =====================================================
INSERT INTO ho_so_tham_dinh (id, ma_hs, khach_hang_id, xe_id, goi_bao_hiem_id, risk_score, risk_level, trang_thai, phi_bao_hiem, ghi_chu, created_at, updated_at) VALUES
(1, 'HS-20241101-0001', 1, 1, 2, 8, 'CHAP_NHAN', 'CHAP_NHAN', 8000000, 'Xe mới, KH uy tín', DATE_SUB(NOW(), INTERVAL 25 DAY), NOW()),
(2, 'HS-20241102-0002', 2, 2, 2, 10, 'CHAP_NHAN', 'CHAP_NHAN', 9600000, NULL, DATE_SUB(NOW(), INTERVAL 24 DAY), NOW()),
(3, 'HS-20241103-0003', 3, 3, 3, 12, 'CHAP_NHAN', 'CHAP_NHAN', 14400000, NULL, DATE_SUB(NOW(), INTERVAL 23 DAY), NOW()),
(4, 'HS-20241104-0004', 4, 4, 1, 6, 'CHAP_NHAN', 'CHAP_NHAN', 5000000, NULL, DATE_SUB(NOW(), INTERVAL 22 DAY), NOW()),
(5, 'HS-20241105-0005', 5, 5, 1, 9, 'CHAP_NHAN', 'CHAP_NHAN', 5000000, NULL, DATE_SUB(NOW(), INTERVAL 21 DAY), NOW());

-- =====================================================
-- 8. CHI TIẾT THẨM ĐỊNH
-- =====================================================
INSERT INTO chi_tiet_tham_dinh (ho_so_tham_dinh_id, tieu_chi_id, diem, ghi_chu, created_at, updated_at) VALUES
(1, 1, 2, 'Xe 4 năm tuổi', DATE_SUB(NOW(), INTERVAL 25 DAY), NOW()),
(1, 2, 2, 'Giá trị TB', DATE_SUB(NOW(), INTERVAL 25 DAY), NOW()),
(1, 3, 0, 'Không tai nạn', DATE_SUB(NOW(), INTERVAL 25 DAY), NOW()),
(2, 1, 3, 'Xe 5 năm tuổi', DATE_SUB(NOW(), INTERVAL 24 DAY), NOW()),
(2, 2, 2, NULL, DATE_SUB(NOW(), INTERVAL 24 DAY), NOW()),
(2, 3, 0, NULL, DATE_SUB(NOW(), INTERVAL 24 DAY), NOW());

-- =====================================================
-- 9. HỢP ĐỒNG (ĐÚNG SCHEMA)
-- =====================================================
INSERT INTO hop_dong (id, ma_hd, khach_hang_id, xe_id, ho_so_tham_dinh_id, goi_bao_hiem_id, ngay_ky, ngay_hieu_luc, ngay_het_han, tong_phi_bao_hiem, tong_da_thanh_toan, trang_thai, ghi_chu, created_at, updated_at) VALUES
(1, 'HD-20241101-0001', 1, 1, 1, 2, DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 24 DAY), DATE_ADD(NOW(), INTERVAL 340 DAY), 8000000, 8000000, 'ACTIVE', NULL, DATE_SUB(NOW(), INTERVAL 25 DAY), NOW()),
(2, 'HD-20241102-0002', 2, 2, 2, 2, DATE_SUB(NOW(), INTERVAL 24 DAY), DATE_SUB(NOW(), INTERVAL 23 DAY), DATE_ADD(NOW(), INTERVAL 341 DAY), 9600000, 9600000, 'ACTIVE', NULL, DATE_SUB(NOW(), INTERVAL 24 DAY), NOW()),
(3, 'HD-20241103-0003', 3, 3, 3, 3, DATE_SUB(NOW(), INTERVAL 23 DAY), DATE_SUB(NOW(), INTERVAL 22 DAY), DATE_ADD(NOW(), INTERVAL 342 DAY), 14400000, 14400000, 'ACTIVE', NULL, DATE_SUB(NOW(), INTERVAL 23 DAY), NOW()),
(4, 'HD-20241104-0004', 4, 4, 4, 1, DATE_SUB(NOW(), INTERVAL 22 DAY), DATE_SUB(NOW(), INTERVAL 21 DAY), DATE_ADD(NOW(), INTERVAL 343 DAY), 5000000, 5000000, 'ACTIVE', NULL, DATE_SUB(NOW(), INTERVAL 22 DAY), NOW()),
(5, 'HD-20241105-0005', 5, 5, 5, 1, DATE_SUB(NOW(), INTERVAL 21 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_ADD(NOW(), INTERVAL 344 DAY), 5000000, 0, 'PENDING_PAYMENT', 'Chờ thanh toán', DATE_SUB(NOW(), INTERVAL 21 DAY), NOW());

-- =====================================================
-- 10. THANH TOÁN
-- =====================================================
INSERT INTO thanh_toan (id, ma_tt, hop_dong_id, ngay_thanh_toan, so_tien, phuong_thuc_thanh_toan, trang_thai, ghi_chu, is_hoan_phi, created_at, updated_at) VALUES
(1, 'TT-20241101-0001', 1, DATE_SUB(NOW(), INTERVAL 20 DAY), 8000000, 'CHUYEN_KHOAN', 'THANH_CONG', 'Thanh toán đủ', 0, DATE_SUB(NOW(), INTERVAL 20 DAY), NOW()),
(2, 'TT-20241102-0002', 2, DATE_SUB(NOW(), INTERVAL 19 DAY), 9600000, 'TIEN_MAT', 'THANH_CONG', NULL, 0, DATE_SUB(NOW(), INTERVAL 19 DAY), NOW()),
(3, 'TT-20241103-0003', 3, DATE_SUB(NOW(), INTERVAL 18 DAY), 14400000, 'CHUYEN_KHOAN', 'THANH_CONG', NULL, 0, DATE_SUB(NOW(), INTERVAL 18 DAY), NOW()),
(4, 'TT-20241104-0004', 4, DATE_SUB(NOW(), INTERVAL 17 DAY), 5000000, 'TIEN_MAT', 'THANH_CONG', NULL, 0, DATE_SUB(NOW(), INTERVAL 17 DAY), NOW());

-- =====================================================
-- HOÀN TẤT!
-- =====================================================
SET FOREIGN_KEY_CHECKS = 1;
SET SQL_SAFE_UPDATES = 1;

SELECT 'Sample data loaded successfully!' AS status;
