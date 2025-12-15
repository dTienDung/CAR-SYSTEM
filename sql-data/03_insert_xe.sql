-- ============================================
-- FILE: 03_insert_xe.sql
-- MÔ TẢ: Insert dữ liệu cho bảng XE
-- LƯU Ý: Cần chạy file 02_insert_khach_hang.sql trước
-- ============================================

-- Xóa dữ liệu cũ (nếu có)
-- DELETE FROM xe;

-- Insert dữ liệu Xe
INSERT INTO xe (ma_xe, bien_so, so_khung, so_may, hang_xe, dong_xe, nam_san_xuat, nam_dang_ky, mau_sac, muc_dich_su_dung, gia_tri_xe, thong_tin_ky_thuat, khach_hang_id, created_at, updated_at) VALUES
-- Xe của khách hàng KH000001
('XE000001', '30A-12345', 'MHFM12345678901', 'EM12345678', 'Toyota', 'Vios', 2020, 2020, 'Trắng', 'Cá nhân', 550000000.00, 'Động cơ 1.5L, số tự động, full option', (SELECT id FROM khach_hang WHERE makh = 'KH000001'), NOW(), NOW()),

-- Xe của khách hàng KH000002
('XE000002', '30B-23456', 'MHFM23456789012', 'EM23456789', 'Honda', 'City', 2019, 2019, 'Đen', 'Cá nhân', 520000000.00, 'Động cơ 1.5L, số tự động, bản cao cấp', (SELECT id FROM khach_hang WHERE makh = 'KH000002'), NOW(), NOW()),

-- Xe của khách hàng KH000003
('XE000003', '30C-34567', 'MHFM34567890123', 'EM34567890', 'Mazda', 'CX-5', 2021, 2021, 'Đỏ', 'Cá nhân', 850000000.00, 'Động cơ 2.0L, số tự động, AWD', (SELECT id FROM khach_hang WHERE makh = 'KH000003'), NOW(), NOW()),

-- Xe của khách hàng KH000004
('XE000004', '30D-45678', 'MHFM45678901234', 'EM45678901', 'Hyundai', 'Accent', 2018, 2018, 'Bạc', 'Cá nhân', 480000000.00, 'Động cơ 1.4L, số tự động', (SELECT id FROM khach_hang WHERE makh = 'KH000004'), NOW(), NOW()),

-- Xe của khách hàng KH000005
('XE000005', '30E-56789', 'MHFM56789012345', 'EM56789012', 'Ford', 'Ranger', 2020, 2020, 'Xanh đen', 'Kinh doanh', 780000000.00, 'Động cơ 2.0L Bi-turbo, 4x4, bản Wildtrak', (SELECT id FROM khach_hang WHERE makh = 'KH000005'), NOW(), NOW()),

-- Xe của khách hàng KH000006
('XE000006', '30F-67890', 'MHFM67890123456', 'EM67890123', 'Kia', 'Morning', 2019, 2019, 'Vàng', 'Cá nhân', 320000000.00, 'Động cơ 1.25L, số sàn', (SELECT id FROM khach_hang WHERE makh = 'KH000006'), NOW(), NOW()),

-- Xe của khách hàng KH000007 (có 2 xe)
('XE000007', '30G-78901', 'MHFM78901234567', 'EM78901234', 'Mercedes-Benz', 'C200', 2022, 2022, 'Đen', 'Cá nhân', 1650000000.00, 'Động cơ 2.0L turbo, số tự động 9 cấp, full option', (SELECT id FROM khach_hang WHERE makh = 'KH000007'), NOW(), NOW()),
('XE000008', '30H-89012', 'MHFM89012345678', 'EM89012345', 'Toyota', 'Fortuner', 2021, 2021, 'Trắng', 'Cá nhân', 1150000000.00, 'Động cơ 2.4L diesel, số tự động, 4x2', (SELECT id FROM khach_hang WHERE makh = 'KH000007'), NOW(), NOW()),

-- Xe của khách hàng KH000008
('XE000009', '30I-90123', 'MHFM90123456789', 'EM90123456', 'Vinfast', 'Fadil', 2020, 2020, 'Xanh dương', 'Cá nhân', 420000000.00, 'Động cơ 1.4L, số tự động CVT', (SELECT id FROM khach_hang WHERE makh = 'KH000008'), NOW(), NOW()),

-- Xe của khách hàng KH000009
('XE000010', '30K-01234', 'MHFM01234567890', 'EM01234567', 'Honda', 'CR-V', 2021, 2021, 'Xám', 'Cá nhân', 1050000000.00, 'Động cơ 1.5L turbo, số tự động, bản L', (SELECT id FROM khach_hang WHERE makh = 'KH000009'), NOW(), NOW()),

-- Xe của khách hàng KH000010
('XE000011', '30L-11111', 'MHFM11111111111', 'EM11111111', 'Mazda', '3', 2020, 2020, 'Đỏ', 'Cá nhân', 720000000.00, 'Động cơ 2.0L, số tự động, bản 2.0 Premium', (SELECT id FROM khach_hang WHERE makh = 'KH000010'), NOW(), NOW()),

-- Thêm một số xe khác
('XE000012', '29A-22222', 'MHFM22222222222', 'EM22222222', 'Toyota', 'Camry', 2021, 2021, 'Đen', 'Cá nhân', 1250000000.00, 'Động cơ 2.5L, số tự động, bản 2.5Q', (SELECT id FROM khach_hang WHERE makh = 'KH000001'), NOW(), NOW()),
('XE000013', '29B-33333', 'MHFM33333333333', 'EM33333333', 'Mitsubishi', 'Xpander', 2020, 2020, 'Trắng', 'Cá nhân', 630000000.00, 'Động cơ 1.5L, số tự động, 7 chỗ', (SELECT id FROM khach_hang WHERE makh = 'KH000003'), NOW(), NOW()),
('XE000014', '29C-44444', 'MHFM44444444444', 'EM44444444', 'Suzuki', 'Swift', 2019, 2019, 'Vàng', 'Cá nhân', 450000000.00, 'Động cơ 1.2L, số tự động CVT', (SELECT id FROM khach_hang WHERE makh = 'KH000005'), NOW(), NOW()),
('XE000015', '29D-55555', 'MHFM55555555555', 'EM55555555', 'BMW', 'X5', 2022, 2022, 'Xanh đen', 'Cá nhân', 3500000000.00, 'Động cơ 3.0L, số tự động 8 cấp, xDrive40i', (SELECT id FROM khach_hang WHERE makh = 'KH000007'), NOW(), NOW());

SELECT 'Đã insert ' || COUNT(*) || ' xe thành công!' as result FROM xe;
