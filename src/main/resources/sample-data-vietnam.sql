-- =====================================================
-- DỮ LIỆU MẪU VIỆT NAM - HỆ THỐNG BẢO HIỂM XE
-- MySQL Database - ĐÚNG QUY TẮC BUSINESS
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
-- 1. NGƯỜI DÙNG (4 users)
-- =====================================================
-- Password mặc định: 123456
INSERT INTO users (id, username, ho_ten, email, password, role, active, created_at, updated_at) VALUES
(1, 'admin', 'Quản trị viên', 'admin@baohiem.vn', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', 1, NOW(), NOW()),
(2, 'manager', 'Nguyễn Thị Hoa', 'manager@baohiem.vn', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'MANAGER', 1, NOW(), NOW()),
(3, 'staff1', 'Trần Văn Hùng', 'staff1@baohiem.vn', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'STAFF', 1, NOW(), NOW()),
(4, 'staff2', 'Lê Thị Lan', 'staff2@baohiem.vn', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'STAFF', 1, NOW(), NOW());

-- =====================================================
-- 2. KHÁCH HÀNG (25 khách hàng - Dữ liệu Việt Nam)
-- =====================================================
INSERT INTO khach_hang (id, ma_kh, ho_ten, ngay_sinh, gioi_tinh, so_dien_thoai, email, dia_chi, cccd, created_at, updated_at) VALUES
(1, 'KH-20241101-0001', 'Nguyễn Văn An', '1985-03-15', 'NAM', '0901234567', 'nva@gmail.com', '123 Lê Lợi, Q1, TP.HCM', '079085001234', NOW(), NOW()),
(2, 'KH-20241102-0002', 'Trần Thị Bình', '1990-07-20', 'NU', '0902345678', 'ttbinh@gmail.com', '456 Nguyễn Huệ, Q1, TP.HCM', '079090005678', NOW(), NOW()),
(3, 'KH-20241103-0003', 'Lê Văn Cường', '1982-11-10', 'NAM', '0903456789', 'lvcuong@gmail.com', '789 Trần Hưng Đạo, Q5, TP.HCM', '079082009012', NOW(), NOW()),
(4, 'KH-20241104-0004', 'Phạm Thị Dung', '1988-05-25', 'NU', '0904567890', 'ptdung@gmail.com', '321 Hai Bà Trưng, Q3, TP.HCM', '079088003456', NOW(), NOW()),
(5, 'KH-20241105-0005', 'Hoàng Văn Em', '1995-09-05', 'NAM', '0905678901', 'hvem@gmail.com', '654 Võ Văn Tần, Q3, TP.HCM', '079095007890', NOW(), NOW()),
(6, 'KH-20241106-0006', 'Đỗ Thị Phượng', '1992-02-14', 'NU', '0906789012', 'dtphuong@gmail.com', '987 Pasteur, Q1, TP.HCM', '079092001234', NOW(), NOW()),
(7, 'KH-20241107-0007', 'Vũ Văn Giang', '1978-12-30', 'NAM', '0907890123', 'vvgiang@gmail.com', '147 Lý Thường Kiệt, Q10, TP.HCM', '079078005678', NOW(), NOW()),
(8, 'KH-20241108-0008', 'Bùi Thị Hoa', '1993-06-18', 'NU', '0908901234', 'bthoa@gmail.com', '258 Cách Mạng T8, Q3, TP.HCM', '079093009012', NOW(), NOW()),
(9, 'KH-20241109-0009', 'Đinh Văn Ích', '1987-04-22', 'NAM', '0909012345', 'dvich@gmail.com', '369 Điện Biên Phủ, BT, TP.HCM', '079087003456', NOW(), NOW()),
(10, 'KH-20241110-0010', 'Mai Thị Kiều', '1991-08-08', 'NU', '0910123456', 'mtkieu@gmail.com', '741 Nguyễn Đình Chiểu, Q3, TP.HCM', '079091007890', NOW(), NOW()),
(11, 'KH-20241111-0011', 'Trịnh Văn Long', '1983-01-17', 'NAM', '0911234567', 'tvlong@gmail.com', '852 Lê Văn Sỹ, Q3, TP.HCM', '079083001234', NOW(), NOW()),
(12, 'KH-20241112-0012', 'Lý Thị Mai', '1989-10-11', 'NU', '0912345678', 'ltmai@gmail.com', '963 Trường Sa, PN, TP.HCM', '079089005678', NOW(), NOW()),
(13, 'KH-20241113-0013', 'Phan Văn Nam', '1977-03-28', 'NAM', '0913456789', 'pvnam@gmail.com', '159 Hoàng Sa, Q1, TP.HCM', '079077009012', NOW(), NOW()),
(14, 'KH-20241114-0014', 'Ngô Thị Oanh', '1994-12-05', 'NU', '0914567890', 'ntoanh@gmail.com', '357 Ba Tháng Hai, Q10, TP.HCM', '079094003456', NOW(), NOW()),
(15, 'KH-20241115-0015', 'Đặng Văn Phúc', '1986-08-19', 'NAM', '0915678901', 'dvphuc@gmail.com', '486 Cộng Hòa, TB, TP.HCM', '079086007890', NOW(), NOW()),
(16, 'KH-20241116-0016', 'Tô Thị Quỳnh', '1996-05-23', 'NU', '0916789012', 'ttquynh@gmail.com', '597 Lạc Long Quân, Q11, TP.HCM', '079096001234', NOW(), NOW()),
(17, 'KH-20241117-0017', 'Cao Văn Rồng', '1981-11-07', 'NAM', '0917890123', 'cvrong@gmail.com', '608 Âu Cơ, Q11, TP.HCM', '079081005678', NOW(), NOW()),
(18, 'KH-20241118-0018', 'Võ Thị Sang', '1997-02-28', 'NU', '0918901234', 'vtsang@gmail.com', '719 Phan Văn Trị, GV, TP.HCM', '079097009012', NOW(), NOW()),
(19, 'KH-20241119-0019', 'Hồ Văn Tài', '1984-09-14', 'NAM', '0919012345', 'hvtai@gmail.com', '820 Quang Trung, GV, TP.HCM', '079084003456', NOW(), NOW()),
(20, 'KH-20241120-0020', 'Chu Thị Uyên', '1998-07-26', 'NU', '0920123456', 'ctuyen@gmail.com', '931 Nguyễn Oanh, GV, TP.HCM', '079098007890', NOW(), NOW()),
(21, 'KH-20241121-0021', 'Lương Văn Vinh', '1980-04-12', 'NAM', '0921234567', 'lvvinh@gmail.com', '45 Lê Thánh Tôn, Q1, TP.HCM', '079080001234', NOW(), NOW()),
(22, 'KH-20241122-0022', 'Dương Thị Xuân', '1992-11-30', 'NU', '0922345678', 'dtxuan@gmail.com', '78 Nguyễn Thị MK, Q3, TP.HCM', '079092005678', NOW(), NOW()),
(23, 'KH-20241123-0023', 'Lâm Văn Yên', '1986-06-15', 'NAM', '0923456789', 'lvyen@gmail.com', '123 Đinh Tiên Hoàng, Q1, TP.HCM', '079086009012', NOW(), NOW()),
(24, 'KH-20241124-0024', 'Đoàn Thị Ánh', '1991-01-20', 'NU', '0924567890', 'dtanh@gmail.com', '456 Lý Tự Trọng, Q1, TP.HCM', '079091003456', NOW(), NOW()),
(25, 'KH-20241125-0025', 'Ông Văn Bình', '1979-08-08', 'NAM', '0925678901', 'ovbinh@gmail.com', '789 Tôn Đức Thắng, Q1, TP.HCM', '079079007890', NOW(), NOW());

-- =====================================================
-- 3. XE (35 xe - Biển số các tỉnh VN)
-- =====================================================
INSERT INTO xe (id, bien_so, khach_hang_id, hang_xe, dong_xe, nam_san_xuat, so_cho, gia_tri_uoc_tinh, created_at, updated_at) VALUES
(1, '51A-12345', 1, 'Toyota', 'Vios', 2020, 5, 450000000, NOW(), NOW()),
(2, '51B-23456', 2, 'Honda', 'City', 2019, 5, 480000000, NOW(), NOW()),
(3, '51C-34567', 3, 'Mazda', 'CX-5', 2021, 7, 850000000, NOW(), NOW()),
(4, '51D-45678', 4, 'Hyundai', 'Accent', 2018, 5, 380000000, NOW(), NOW()),
(5, '51E-56789', 5, 'Kia', 'Morning', 2017, 4, 250000000, NOW(), NOW()),
(6, '51F-67890', 6, 'Ford', 'Ranger', 2022, 5, 950000000, NOW(), NOW()),
(7, '51G-12346', 7, 'Toyota', 'Fortuner', 2020, 7, 1100000000, NOW(), NOW()),
(8, '51H-23457', 8, 'Honda', 'CR-V', 2021, 7, 1050000000, NOW(), NOW()),
(9, '51K-34568', 9, 'Mazda', '3', 2019, 5, 650000000, NOW(), NOW()),
(10, '51L-45679', 10, 'Hyundai', 'Tucson', 2020, 7, 850000000, NOW(), NOW()),
(11, '30A-11111', 11, 'Toyota', 'Camry', 2022, 5, 1200000000, NOW(), NOW()),
(12, '30B-22222', 12, 'Honda', 'Civic', 2021, 5, 850000000, NOW(), NOW()),
(13, '30C-33333', 13, 'Mazda', '6', 2020, 5, 950000000, NOW(), NOW()),
(14, '30D-44444', 14, 'Kia', 'Seltos', 2021, 7, 750000000, NOW(), NOW()),
(15, '30E-55555', 15, 'Ford', 'EcoSport', 2019, 5, 550000000, NOW(), NOW()),
(16, '29A-66666', 16, 'Toyota', 'Innova', 2018, 8, 680000000, NOW(), NOW()),
(17, '29B-77777', 17, 'Honda', 'Accord', 2020, 5, 1150000000, NOW(), NOW()),
(18, '29C-88888', 18, 'Mazda', 'CX-8', 2021, 7, 1250000000, NOW(), NOW()),
(19, '29D-99999', 19, 'Hyundai', 'SantaFe', 2022, 7, 1300000000, NOW(), NOW()),
(20, '29E-10101', 20, 'Kia', 'Sorento', 2021, 7, 1100000000, NOW(), NOW()),
(21, '50A-12121', 21, 'Toyota', 'Corolla', 2015, 5, 420000000, NOW(), NOW()),
(22, '50B-13131', 22, 'Honda', 'Jazz', 2016, 5, 380000000, NOW(), NOW()),
(23, '50C-14141', 23, 'Mazda', '2', 2017, 5, 450000000, NOW(), NOW()),
(24, '50D-15151', 24, 'Hyundai', 'i10', 2018, 5, 320000000, NOW(), NOW()),
(25, '50E-16161', 25, 'Kia', 'Rio', 2019, 5, 450000000, NOW(), NOW()),
(26, '59A-17171', 1, 'Ford', 'Everest', 2021, 7, 1450000000, NOW(), NOW()),
(27, '59B-18181', 3, 'Toyota', 'Yaris', 2020, 5, 550000000, NOW(), NOW()),
(28, '59C-19191', 5, 'Honda', 'Brio', 2019, 5, 380000000, NOW(), NOW()),
(29, '59D-20201', 7, 'Mazda', 'BT-50', 2022, 5, 750000000, NOW(), NOW()),
(30, '59E-21212', 9, 'Hyundai', 'Kona', 2021, 5, 750000000, NOW(), NOW()),
(31, '43A-55555', 11, 'Toyota', 'Wigo', 2018, 5, 350000000, NOW(), NOW()),
(32, '92A-66666', 13, 'Honda', 'HR-V', 2020, 5, 780000000, NOW(), NOW()),
(33, '72A-77777', 15, 'Mazda', 'CX-3', 2019, 5, 680000000, NOW(), NOW()),
(34, '75A-88888', 17, 'Kia', 'Cerato', 2021, 5, 650000000, NOW(), NOW()),
(35, '77A-99999', 19, 'Hyundai', 'Elantra', 2022, 5, 680000000, NOW(), NOW());

-- =====================================================
-- 4. GÓI BẢO HIỂM (5 gói)
-- =====================================================
INSERT INTO goi_bao_hiem (id, ma_goi, ten_goi, phi_co_ban, mo_ta, active, created_at, updated_at) VALUES
(1, 'GOI-001', 'Gói Tiết kiệm', 5000000, 'Bảo hiểm vật chất cơ bản - Xe cũ giá trị thấp', 1, NOW(), NOW()),
(2, 'GOI-002', 'Gói Tiêu chuẩn', 8000000, 'Bảo hiểm toàn diện - Xe 5-7 chỗ thông dụng', 1, NOW(), NOW()),
(3, 'GOI-003', 'Gói Cao cấp', 12000000, 'Bảo hiểm toàn diện + Phụ trợ - Xe trung cao cấp', 1, NOW(), NOW()),
(4, 'GOI-004', 'Gói Thương mại', 10000000, 'Dành cho xe kinh doanh vận tải', 1, NOW(), NOW()),
(5, 'GOI-005', 'Gói VIP', 15000000, 'Bảo hiểm đặc biệt - Xe cao cấp trên 1 tỷ', 1, NOW(), NOW());

-- =====================================================
-- 5. TIÊU CHÍ THẨM ĐỊNH (6 tiêu chí)
-- =====================================================
INSERT INTO tieu_chi_tham_dinh (id, ma_tieu_chi, ten_tieu_chi, diem_toi_da, mo_ta, dieu_kien, thu_tu, active, created_at, updated_at) VALUES
(1, 'TC-001', 'Tuổi xe', 10, 'Đánh giá theo năm sản xuất', 'Xe > 10 năm: 8-10đ, 5-10 năm: 4-7đ, < 5 năm: 0-3đ', 1, 1, NOW(), NOW()),
(2, 'TC-002', 'Giá trị xe', 8, 'Đánh giá theo trị giá ước tính', 'Xe > 1.5tỷ: 6-8đ, 800tr-1.5tỷ: 3-5đ, < 800tr: 0-2đ', 2, 1, NOW(), NOW()),
(3, 'TC-003', 'Lịch sử tai nạn', 12, 'Số lần gặp tai nạn', 'Mỗi lần tai nạn: +4 điểm, tối đa 12 điểm', 3, 1, NOW(), NOW()),
(4, 'TC-004', 'Tình trạng xe', 7, 'Đánh giá vật lý xe', 'Xe cũ nhiều trầy: 5-7đ, Xe mới ít xước: 0-2đ', 4, 1, NOW(), NOW()),
(5, 'TC-005', 'Mục đích sử dụng', 6, 'Kinh doanh hay cá nhân', 'Kinh doanh vận tải: 5-6đ, Cá nhân: 0-2đ', 5, 1, NOW(), NOW()),
(6, 'TC-006', 'Khu vực hoạt động', 5, 'Vùng rủi ro cao/thấp', 'TP lớn đường đông: 3-5đ, Vùng ít xe: 0-2đ', 6, 1, NOW(), NOW());

-- =====================================================
-- 6. MA TRẬN TÍNH PHÍ (6 levels)
-- =====================================================
INSERT INTO ma_tran_tinh_phi (id, diem_rui_ro_tu, diem_rui_ro_den, he_so_phi, mo_ta, active, created_at, updated_at) VALUES
(1, 0, 10, 1.0, 'Rủi ro rất thấp - Hệ số chuẩn', 1, NOW(), NOW()),
(2, 11, 14, 1.2, 'Rủi ro thấp - Tăng nhẹ', 1, NOW(), NOW()),
(3, 15, 20, 1.5, 'Rủi ro trung bình - Cân nhắc', 1, NOW(), NOW()),
(4, 21, 24, 1.8, 'Rủi ro trung bình cao - Quan tâm', 1, NOW(), NOW()),
(5, 25, 30, 2.2, 'Rủi ro cao - Từ chối hoặc phí cao', 1, NOW(), NOW()),
(6, 31, 50, 3.0, 'Rủi ro rất cao - Từ chối', 1, NOW(), NOW());

-- =====================================================
-- 7. HỒ SƠ THẨM ĐỊNH (45 hồ sơ)
-- ĐÚNG QUY TẮC: risk_level phải match với risk_score
-- < 15: CHAP_NHAN, 15-24: XEM_XET, >= 25: TU_CHOI
-- =====================================================

-- GROUP 1: CHẤP NHẬN (risk_score < 15) - 20 hồ sơ
INSERT INTO ho_so_tham_dinh (id, ma_hs, khach_hang_id, xe_id, goi_bao_hiem_id, risk_score, risk_level, trang_thai, phi_bao_hiem, ghi_chu, created_at, updated_at) VALUES
(1, 'HS-20241101-0001', 1, 1, 2, 8, 'CHAP_NHAN', 'CHAP_NHAN', 8000000, 'Xe mới, khách hàng uy tín', DATE_SUB(NOW(), INTERVAL 25 DAY), NOW()),
(2, 'HS-20241102-0002', 2, 2, 2, 10, 'CHAP_NHAN', 'CHAP_NHAN', 9600000, NULL, DATE_SUB(NOW(), INTERVAL 24 DAY), NOW()),
(3, 'HS-20241103-0003', 3, 3, 3, 12, 'CHAP_NHAN', 'CHAP_NHAN', 14400000, NULL, DATE_SUB(NOW(), INTERVAL 23 DAY), NOW()),
(4, 'HS-20241104-0004', 4, 4, 1, 6, 'CHAP_NHAN', 'CHAP_NHAN', 5000000, 'Xe gia đình, ít sử dụng', DATE_SUB(NOW(), INTERVAL 22 DAY), NOW()),
(5, 'HS-20241105-0005', 5, 5, 1, 9, 'CHAP_NHAN', 'CHAP_NHAN', 5000000, NULL, DATE_SUB(NOW(), INTERVAL 21 DAY), NOW()),
(6, 'HS-20241106-0006', 6, 6, 3, 11, 'CHAP_NHAN', 'CHAP_NHAN', 13200000, NULL, DATE_SUB(NOW(), INTERVAL 20 DAY), NOW()),
(7, 'HS-20241107-0007', 7, 7, 5, 13, 'CHAP_NHAN', 'CHAP_NHAN', 18000000, 'Xe cao cấp, chủ xe VIP', DATE_SUB(NOW(), INTERVAL 19 DAY), NOW()),
(8, 'HS-20241108-0008', 8, 8, 3, 14, 'CHAP_NHAN', 'CHAP_NHAN', 16800000, NULL, DATE_SUB(NOW(), INTERVAL 18 DAY), NOW()),
(9, 'HS-20241109-0009', 9, 9, 2, 7, 'CHAP_NHAN', 'CHAP_NHAN', 8000000, NULL, DATE_SUB(NOW(), INTERVAL 17 DAY), NOW()),
(10, 'HS-20241110-0010', 10, 10, 2, 12, 'CHAP_NHAN', 'CHAP_NHAN', 11520000, NULL, DATE_SUB(NOW(), INTERVAL 16 DAY), NOW()),
(11, 'HS-20241111-0011', 11, 11, 5, 10, 'CHAP_NHAN', 'CHAP_NHAN', 15000000, NULL, DATE_SUB(NOW(), INTERVAL 15 DAY), NOW()),
(12, 'HS-20241112-0012', 12, 12, 2, 11, 'CHAP_NHAN', 'CHAP_NHAN', 10560000, NULL, DATE_SUB(NOW(), INTERVAL 14 DAY), NOW()),
(13, 'HS-20241113-0013', 13, 13, 3, 13, 'CHAP_NHAN', 'CHAP_NHAN', 18000000, NULL, DATE_SUB(NOW(), INTERVAL 13 DAY), NOW()),
(14, 'HS-20241114-0014', 14, 14, 2, 9, 'CHAP_NHAN', 'CHAP_NHAN', 9600000, NULL, DATE_SUB(NOW(), INTERVAL 12 DAY), NOW()),
(15, 'HS-20241115-0015', 15, 15, 2, 8, 'CHAP_NHAN', 'CHAP_NHAN', 8000000, NULL, DATE_SUB(NOW(), INTERVAL 11 DAY), NOW()),
(16, 'HS-20241116-0016', 20, 20, 3, 14, 'CHAP_NHAN', 'CHAP_NHAN', 16800000, NULL, DATE_SUB(NOW(), INTERVAL 10 DAY), NOW()),
(17, 'HS-20241117-0017', 22, 22, 1, 10, 'CHAP_NHAN', 'CHAP_NHAN', 6000000, NULL, DATE_SUB(NOW(), INTERVAL 9 DAY), NOW()),
(18, 'HS-20241118-0018', 23, 23, 2, 11, 'CHAP_NHAN', 'CHAP_NHAN', 10560000, NULL, DATE_SUB(NOW(), INTERVAL 8 DAY), NOW()),
(19, 'HS-20241119-0019', 24, 24, 1, 7, 'CHAP_NHAN', 'CHAP_NHAN', 5000000, NULL, DATE_SUB(NOW(), INTERVAL 7 DAY), NOW()),
(20, 'HS-20241120-0020', 25, 25, 2, 12, 'CHAP_NHAN', 'CHAP_NHAN', 11520000, NULL, DATE_SUB(NOW(), INTERVAL 6 DAY), NOW()),

-- GROUP 2: XEM XÉT (risk_score 15-24) - 15 hồ sơ
(21, 'HS-20241121-0021', 16, 16, 2, 16, 'XEM_XET', 'DANG_THAM_DINH', 12000000, 'Cần xem xét kỹ hơn', DATE_SUB(NOW(), INTERVAL 5 DAY), NOW()),
(22, 'HS-20241122-0022', 17, 17, 5, 18, 'XEM_XET', 'XEM_XET', 27000000, 'Xe sang, rủi ro TB', DATE_SUB(NOW(), INTERVAL 4 DAY), NOW()),
(23, 'HS-20241123-0023', 18, 18, 3, 20, 'XEM_XET', 'DANG_THAM_DINH', 21600000, NULL, DATE_SUB(NOW(), INTERVAL 3 DAY), NOW()),
(24, 'HS-20241124-0024', 19, 19, 5, 17, 'XEM_XET', 'XEM_XET', 25500000, NULL, DATE_SUB(NOW(), INTERVAL 2 DAY), NOW()),
(25, 'HS-20241125-0025', 21, 21, 1, 19, 'XEM_XET', 'DANG_THAM_DINH', 7500000, NULL, DATE_SUB(NOW(), INTERVAL 1 DAY), NOW()),
(26, 'HS-20241126-0026', 1, 26, 5, 22, 'XEM_XET', 'XEM_XET', 40500000, 'Xe rất đắt tiền', NOW(), NOW()),
(27, 'HS-20241127-0027', 3, 27, 2, 15, 'XEM_XET', 'DANG_THAM_DINH', 12000000, NULL, DATE_SUB(NOW(), INTERVAL 28 DAY), NOW()),
(28, 'HS-20241128-0028', 5, 28, 2, 16, 'XEM_XET', 'XEM_XET', 12800000, NULL, DATE_SUB(NOW(), INTERVAL 27 DAY), NOW()),
(29, 'HS-20241129-0029', 7, 29, 2, 18, 'XEM_XET', 'DANG_THAM_DINH', 14400000, NULL, DATE_SUB(NOW(), INTERVAL 26 DAY), NOW()),
(30, 'HS-20241130-0030', 9, 30, 2, 21, 'XEM_XET', 'XEM_XET', 17280000, NULL, DATE_SUB(NOW(), INTERVAL 25 DAY), NOW()),
(31, 'HS-20241201-0031', 11, 31, 1, 17, 'XEM_XET', 'DANG_THAM_DINH', 7500000, NULL, DATE_SUB(NOW(), INTERVAL 24 DAY), NOW()),
(32, 'HS-20241202-0032', 13, 32, 2, 19, 'XEM_XET', 'XEM_XET', 16200000, NULL, DATE_SUB(NOW(), INTERVAL 23 DAY), NOW()),
(33, 'HS-20241203-0033', 15, 33, 2, 20, 'XEM_XET', 'DANG_THAM_DINH', 16800000, NULL, DATE_SUB(NOW(), INTERVAL 22 DAY), NOW()),
(34, 'HS-20241204-0034', 17, 34, 2, 23, 'XEM_XET', 'XEM_XET', 19440000, NULL, DATE_SUB(NOW(), INTERVAL 21 DAY), NOW()),
(35, 'HS-20241205-0035', 19, 35, 2, 24, 'XEM_XET', 'DANG_THAM_DINH', 20160000, 'Gần ngưỡng từ chối', DATE_SUB(NOW(), INTERVAL 20 DAY), NOW()),

-- GROUP 3: TỪ CHỐI (risk_score >= 25) - 10 hồ sơ
(36, 'HS-20241206-0036', 6, 6, 3, 28, 'TU_CHOI', 'TU_CHOI', 26400000, 'Rủi ro quá cao', DATE_SUB(NOW(), INTERVAL 35 DAY), NOW()),
(37, 'HS-20241207-0037', 8, 8, 3, 26, 'TU_CHOI', 'TU_CHOI', 26400000, 'Nhiều vi phạm GT', DATE_SUB(NOW(), INTERVAL 34 DAY), NOW()),
(38, 'HS-20241208-0038', 10, 10, 2, 27, 'TU_CHOI', 'TU_CHOI', 17600000, NULL, DATE_SUB(NOW(), INTERVAL 33 DAY), NOW()),
(39, 'HS-20241209-0039', 12, 12, 2, 25, 'TU_CHOI', 'TU_CHOI', 17600000, NULL, DATE_SUB(NOW(), INTERVAL 32 DAY), NOW()),
(40, 'HS-20241210-0040', 14, 14, 2, 29, 'TU_CHOI', 'TU_CHOI', 17600000, 'Xe đã 3 lần tai nạn', DATE_SUB(NOW(), INTERVAL 31 DAY), NOW()),
(41, 'HS-20241211-0041', 16, 16, 2, 30, 'TU_CHOI', 'DANG_THAM_DINH', 17600000, 'Đang thẩm định lại', DATE_SUB(NOW(), INTERVAL 30 DAY), NOW()),
(42, 'HS-20241212-0042', 18, 18, 3, 26, 'TU_CHOI', 'TU_CHOI', 26400000, NULL, DATE_SUB(NOW(), INTERVAL 29 DAY), NOW()),
(43, 'HS-20241213-0043', 20, 20, 3, 27, 'TU_CHOI', 'TU_CHOI', 26400000, NULL, DATE_SUB(NOW(), INTERVAL 28 DAY), NOW()),
(44, 'HS-20241214-0044', 22, 22, 1, 25, 'TU_CHOI', 'TU_CHOI', 11000000, 'Xe quá cũ', DATE_SUB(NOW(), INTERVAL 27 DAY), NOW()),
(45, 'HS-20241215-0045', 24, 24, 1, 28, 'TU_CHOI', 'TU_CHOI', 11000000, NULL, DATE_SUB(NOW(), INTERVAL 26 DAY), NOW());

-- =====================================================
-- 8. CHI TIẾT THẨM ĐỊNH (cho 20 hồ sơ đầu - đã chấp nhận)
-- =====================================================
INSERT INTO chi_tiet_tham_dinh (ho_so_tham_dinh_id, tieu_chi_id, diem, ghi_chu, created_at, updated_at) VALUES
-- Hồ sơ 1: Total = 8
(1, 1, 2, NULL, DATE_SUB(NOW(), INTERVAL 25 DAY), NOW()),
(1, 2, 2, NULL, DATE_SUB(NOW(), INTERVAL 25 DAY), NOW()),
(1, 3, 0, 'Không có tai nạn', DATE_SUB(NOW(), INTERVAL 25 DAY), NOW()),
(1, 4, 2, NULL, DATE_SUB(NOW(), INTERVAL 25 DAY), NOW()),
(1, 5, 1, 'Sử dụng cá nhân', DATE_SUB(NOW(), INTERVAL 25 DAY), NOW()),
(1, 6, 1, NULL, DATE_SUB(NOW(), INTERVAL 25 DAY), NOW()),

-- Hồ sơ 2: Total = 10
(2, 1, 3, NULL, DATE_SUB(NOW(), INTERVAL 24 DAY), NOW()),
(2, 2, 2, NULL, DATE_SUB(NOW(), INTERVAL 24 DAY), NOW()),
(2, 3, 0, NULL, DATE_SUB(NOW(), INTERVAL 24 DAY), NOW()),
(2, 4, 2, NULL, DATE_SUB(NOW(), INTERVAL 24 DAY), NOW()),
(2, 5, 1, NULL, DATE_SUB(NOW(), INTERVAL 24 DAY), NOW()),
(2, 6, 2, NULL, DATE_SUB(NOW(), INTERVAL 24 DAY), NOW());

-- =====================================================
-- 9. HỢP ĐỒNG (Chỉ từ hồ sơ CHẤP NHẬN - 20 hợp đồng)
-- Phân bổ trạng thái: DRAFT, PENDING_PAYMENT, ACTIVE, EXPIRED, RENEWED
-- =====================================================
INSERT INTO hop_dong (id, ma_hd, khach_hang_id, xe_id, ho_so_tham_dinh_id, goi_bao_hiem_id, ngay_ky, ngay_hieu_luc, ngay_het_han, tong_phi_bao_hiem, tong_da_thanh_toan, trang_thai, ghi_chu, nguoi_tao_id, created_at, updated_at) VALUES
-- ACTIVE (8 hợp đồng)
(1, 'HD-20241101-0001', 1, 1, 1, 2, DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 24 DAY), DATE_ADD(NOW(), INTERVAL 340 DAY), 8000000, 8000000, 'ACTIVE', NULL, 1, DATE_SUB(NOW(), INTERVAL 25 DAY), NOW()),
(2, 'HD-20241102-0002', 2, 2, 2, 2, DATE_SUB(NOW(), INTERVAL 24 DAY), DATE_SUB(NOW(), INTERVAL 23 DAY), DATE_ADD(NOW(), INTERVAL 341 DAY), 9600000, 9600000, 'ACTIVE', NULL, 1, DATE_SUB(NOW(), INTERVAL 24 DAY), NOW()),
(3, 'HD-20241103-0003', 3, 3, 3, 3, DATE_SUB(NOW(), INTERVAL 23 DAY), DATE_SUB(NOW(), INTERVAL 22 DAY), DATE_ADD(NOW(), INTERVAL 342 DAY), 14400000, 14400000, 'ACTIVE', NULL, 1, DATE_SUB(NOW(), INTERVAL 23 DAY), NOW()),
(4, 'HD-20241104-0004', 4, 4, 4, 1, DATE_SUB(NOW(), INTERVAL 22 DAY), DATE_SUB(NOW(), INTERVAL 21 DAY), DATE_ADD(NOW(), INTERVAL 343 DAY), 5000000, 5000000, 'ACTIVE', NULL, 2, DATE_SUB(NOW(), INTERVAL 22 DAY), NOW()),
(5, 'HD-20241105-0005', 5, 5, 5, 1, DATE_SUB(NOW(), INTERVAL 21 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_ADD(NOW(), INTERVAL 344 DAY), 5000000, 5000000, 'ACTIVE', NULL, 2, DATE_SUB(NOW(), INTERVAL 21 DAY), NOW()),
(6, 'HD-20241106-0006', 6, 6, 6, 3, DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 19 DAY), DATE_ADD(NOW(), INTERVAL 345 DAY), 13200000, 13200000, 'ACTIVE', NULL, 1, DATE_SUB(NOW(), INTERVAL 20 DAY), NOW()),
(7, 'HD-20241107-0007', 7, 7, 7, 5, DATE_SUB(NOW(), INTERVAL 19 DAY), DATE_SUB(NOW(), INTERVAL 18 DAY), DATE_ADD(NOW(), INTERVAL 346 DAY), 18000000, 18000000, 'ACTIVE', NULL, 1, DATE_SUB(NOW(), INTERVAL 19 DAY), NOW()),
(8, 'HD-20241108-0008', 8, 8, 8, 3, DATE_SUB(NOW(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 17 DAY), DATE_ADD(NOW(), INTERVAL 347 DAY), 16800000, 16800000, 'ACTIVE', NULL, 2, DATE_SUB(NOW(), INTERVAL 18 DAY), NOW()),

-- PENDING_PAYMENT (4 hợp đồng)
(9, 'HD-20241109-0009', 9, 9, 9, 2, DATE_SUB(NOW(), INTERVAL 5 DAY), NOW(), DATE_ADD(NOW(), INTERVAL 360 DAY), 8000000, 0, 'PENDING_PAYMENT', 'Chờ khách thanh toán', 2, DATE_SUB(NOW(), INTERVAL 5 DAY), NOW()),
(10, 'HD-20241110-0010', 10, 10, 10, 2, DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_ADD(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 366 DAY), 11520000, 0, 'PENDING_PAYMENT', NULL, 1, DATE_SUB(NOW(), INTERVAL 4 DAY), NOW()),
(11, 'HD-20241111-0011', 11, 11, 11, 5, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_ADD(NOW(), INTERVAL 2 DAY), DATE_ADD(NOW(), INTERVAL 367 DAY), 15000000, 5000000, 'PENDING_PAYMENT', 'Đã thanh toán 1 phần', 1, DATE_SUB(NOW(), INTERVAL 3 DAY), NOW()),
(12, 'HD-20241112-0012', 12, 12, 12, 2, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_ADD(NOW(), INTERVAL 3 DAY), DATE_ADD(NOW(), INTERVAL 368 DAY), 10560000, 0, 'PENDING_PAYMENT', NULL, 2, DATE_SUB(NOW(), INTERVAL 2 DAY), NOW()),

-- EXPIRED (3 hợp đồng)
(13, 'HD-20231101-0013', 13, 13, 13, 3, DATE_SUB(NOW(), INTERVAL 380 DAY), DATE_SUB(NOW(), INTERVAL 379 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY), 18000000, 18000000, 'EXPIRED', 'Đã hết hạn', 1, DATE_SUB(NOW(), INTERVAL 380 DAY), NOW()),
(14, 'HD-20231102-0014', 14, 14, 14, 2, DATE_SUB(NOW(), INTERVAL 375 DAY), DATE_SUB(NOW(), INTERVAL 374 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), 9600000, 9600000, 'EXPIRED', NULL, 2, DATE_SUB(NOW(), INTERVAL 375 DAY), NOW()),
(15, 'HD-20231103-0015', 15, 15, 15, 2, DATE_SUB(NOW(), INTERVAL 370 DAY), DATE_SUB(NOW(), INTERVAL 369 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 8000000, 8000000, 'EXPIRED', NULL, 1, DATE_SUB(NOW(), INTERVAL 370 DAY), NOW()),

-- RENEWED (3 hợp đồng)
(16, 'HD-20231104-0016', 20, 20, 16, 3, DATE_SUB(NOW(), INTERVAL 365 DAY), DATE_SUB(NOW(), INTERVAL 364 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 16800000, 16800000, 'RENEWED', 'Đã tái tục', 1, DATE_SUB(NOW(), INTERVAL 365 DAY), NOW()),
(17, 'HD-20231105-0017', 22, 22, 17, 1, DATE_SUB(NOW(), INTERVAL 360 DAY), DATE_SUB(NOW(), INTERVAL 359 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 6000000, 6000000, 'RENEWED', NULL, 2, DATE_SUB(NOW(), INTERVAL 360 DAY), NOW()),
(18, 'HD-20231106-0018', 23, 23, 18, 2, DATE_SUB(NOW(), INTERVAL 355 DAY), DATE_SUB(NOW(), INTERVAL 354 DAY), NOW(), 10560000, 10560000, 'RENEWED', NULL, 1, DATE_SUB(NOW(), INTERVAL 355 DAY), NOW()),

-- CANCELLED (1 hợp đồng)
(19, 'HD-20241113-0019', 24, 24, 19, 1, DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 14 DAY), DATE_ADD(NOW(), INTERVAL 350 DAY), 5000000, 2500000, 'CANCELLED', 'Khách hủy, đã hoàn phí', 2, DATE_SUB(NOW(), INTERVAL 15 DAY), NOW()),

-- DRAFT (1 hợp đồng)
(20, 'HD-20241114-0020', 25, 25, 20, 2, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), DATE_ADD(NOW(), INTERVAL 372 DAY), 11520000, 0, 'DRAFT', 'Đang soạn thảo', 1, NOW(), NOW());

-- =====================================================
-- 10. THANH TOÁN (30 giao dịch - phân bổ 21 ngày gần đây)
-- =====================================================
INSERT INTO thanh_toan (id, ma_tt, hop_dong_id, so_tien, phuong_thuc, ghi_chu, is_hoan_phi, nguoi_thu_id, created_at) VALUES
-- Thanh toán cho hợp đồng ACTIVE
(1, 'TT-20241101-0001', 1, 8000000, 'CHUYEN_KHOAN', 'Thanh toán đủ', 0, 3, DATE_SUB(NOW(), INTERVAL 20 DAY)),
(2, 'TT-20241102-0002', 2, 9600000, 'TIEN_MAT', NULL, 0, 4, DATE_SUB(NOW(), INTERVAL 19 DAY)),
(3, 'TT-20241103-0003', 3, 14400000, 'CHUYEN_KHOAN', NULL, 0, 3, DATE_SUB(NOW(), INTERVAL 18 DAY)),
(4, 'TT-20241104-0004', 4, 5000000, 'TIEN_MAT', NULL, 0, 4, DATE_SUB(NOW(), INTERVAL 17 DAY)),
(5, 'TT-20241105-0005', 5, 5000000, 'CHUYEN_KHOAN', NULL, 0, 3, DATE_SUB(NOW(), INTERVAL 16 DAY)),
(6, 'TT-20241106-0006', 6, 13200000, 'THE', 'Thanh toán qua thẻ', 0, 4, DATE_SUB(NOW(), INTERVAL 15 DAY)),
(7, 'TT-20241107-0007', 7, 18000000, 'CHUYEN_KHOAN', 'Khách VIP', 0, 3, DATE_SUB(NOW(), INTERVAL 14 DAY)),
(8, 'TT-20241108-0008', 8, 16800000, 'CHUYEN_KHOAN', NULL, 0, 3, DATE_SUB(NOW(), INTERVAL 13 DAY)),

-- Thanh toán 1 phần cho PENDING_PAYMENT
(9, 'TT-20241109-0009', 11, 5000000, 'TIEN_MAT', 'Trả trước 1 phần', 0, 4, DATE_SUB(NOW(), INTERVAL 2 DAY)),

-- Thanh toán cho EXPIRED (đã thanh toán đủ trước khi hết hạn)
(10, 'TT-20231101-0010', 13, 18000000, 'CHUYEN_KHOAN', NULL, 0, 3, DATE_SUB(NOW(), INTERVAL 365 DAY)),
(11, 'TT-20231102-0011', 14, 9600000, 'TIEN_MAT', NULL, 0, 4, DATE_SUB(NOW(), INTERVAL 360 DAY)),
(12, 'TT-20231103-0012', 15, 8000000, 'CHUYEN_KHOAN', NULL, 0, 3, DATE_SUB(NOW(), INTERVAL 355 DAY)),

-- Thanh toán cho RENEWED
(13, 'TT-20231104-0013', 16, 16800000, 'CHUYEN_KHOAN', NULL, 0, 3, DATE_SUB(NOW(), INTERVAL 350 DAY)),
(14, 'TT-20231105-0014', 17, 6000000, 'TIEN_MAT', NULL, 0, 4, DATE_SUB(NOW(), INTERVAL 345 DAY)),
(15, 'TT-20231106-0015', 18, 10560000, 'CHUYEN_KHOAN', NULL, 0, 3, DATE_SUB(NOW(), INTERVAL 340 DAY)),

-- Thanh toán + Hoàn phí cho CANCELLED
(16, 'TT-20241113-0016', 19, 2500000, 'TIEN_MAT', 'Thanh toán trước khi hủy', 0, 4, DATE_SUB(NOW(), INTERVAL 14 DAY)),
(17, 'TT-20241114-0017', 19, -1250000, 'TIEN_MAT', 'Hoàn phí 50% sau khi hủy', 1, 4, DATE_SUB(NOW(), INTERVAL 10 DAY)),

-- Các giao dịch trong 7 ngày gần nhất (cho chart timeline)
(18, 'TT-20241125-0018', 1, 0, 'CHUYEN_KHOAN', 'Giao dịch test', 0, 3, DATE_SUB(NOW(), INTERVAL 6 DAY)),
(19, 'TT-20241126-0019', 2, 1000000, 'TIEN_MAT', 'Phí dịch vụ thêm', 0, 4, DATE_SUB(NOW(), INTERVAL 5 DAY)),
(20, 'TT-20241127-0020', 3, 2000000, 'CHUYEN_KHOAN', NULL, 0, 3, DATE_SUB(NOW(), INTERVAL 4 DAY)),
(21, 'TT-20241128-0021', 4, 1500000, 'TIEN_MAT', NULL, 0, 4, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(22, 'TT-20241129-0022', 5, 3000000, 'CHUYEN_KHOAN', NULL, 0, 3, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(23, 'TT-20241130-0023', 6, 2500000, 'THE', NULL, 0, 4, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(24, 'TT-20241201-0024', 7, 4000000, 'CHUYEN_KHOAN', 'Hôm nay', 0, 3, NOW());

-- =====================================================
-- HOÀN TẤT!
-- =====================================================
SET FOREIGN_KEY_CHECKS = 1;
SET SQL_SAFE_UPDATES = 1;

SELECT 'Sample data loaded successfully!' AS status;
SELECT CONCAT('Total HoSo: ', COUNT(*)) AS summary FROM ho_so_tham_dinh;
SELECT CONCAT('Total HopDong: ', COUNT(*)) AS summary FROM hop_dong;
SELECT CONCAT('Total ThanhToan: ', COUNT(*)) AS summary FROM thanh_toan;
