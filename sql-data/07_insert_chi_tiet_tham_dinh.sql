-- ============================================
-- FILE: 07_insert_chi_tiet_tham_dinh.sql
-- MÔ TẢ: Insert dữ liệu cho bảng CHI_TIET_THAM_DINH
-- LƯU Ý: Cần chạy file 06_insert_ho_so_tham_dinh.sql trước
--        4 tiêu chí HARDCODE: CT01-CT04 (mỗi tiêu chí max 2 điểm)
--        Tổng điểm: 0-8
-- ============================================

-- Xóa dữ liệu cũ (nếu có)
-- DELETE FROM chi_tiet_tham_dinh;

-- Insert dữ liệu Chi tiết thẩm định cho các hồ sơ đã hoàn thành
-- Lưu ý: tieu_chi_id sẽ là 1,2,3,4 (hardcode trong DatabaseInitializer)

-- Hồ sơ HS-20231201-0001 (Risk Score = 2)
INSERT INTO chi_tiet_tham_dinh (ho_so_tham_dinh_id, tieu_chi_id, diem, ghi_chu, created_at) VALUES
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231201-0001'), 1, 0, 'Xe mới 3 năm (<5 năm)', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231201-0001'), 2, 0, 'Sử dụng cá nhân', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231201-0001'), 3, 0, 'Tài xế 38 tuổi (26-55)', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231201-0001'), 4, 2, 'Xe giá 550 triệu (>500tr nhưng gần ranh)', NOW());

-- Hồ sơ HS-20231202-0002 (Risk Score = 3)
INSERT INTO chi_tiet_tham_dinh (ho_so_tham_dinh_id, tieu_chi_id, diem, ghi_chu, created_at) VALUES
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231202-0002'), 1, 0, 'Xe 4 năm (<5 năm)', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231202-0002'), 2, 0, 'Cá nhân', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231202-0002'), 3, 2, 'Tài xế 33 tuổi nhưng thiếu kinh nghiệm', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231202-0002'), 4, 1, 'Xe 520 triệu (>500tr)', NOW());

-- Hồ sơ HS-20231205-0003 (Risk Score = 1)
INSERT INTO chi_tiet_tham_dinh (ho_so_tham_dinh_id, tieu_chi_id, diem, ghi_chu, created_at) VALUES
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231205-0003'), 1, 0, 'Xe 2 năm (<5 năm)', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231205-0003'), 2, 0, 'Cá nhân', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231205-0003'), 3, 0, 'Tài xế 35 tuổi', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231205-0003'), 4, 1, 'Xe Mazda CX-5 850 triệu', NOW());

-- Hồ sơ HS-20231206-0004 (Risk Score = 4)
INSERT INTO chi_tiet_tham_dinh (ho_so_tham_dinh_id, tieu_chi_id, diem, ghi_chu, created_at) VALUES
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231206-0004'), 1, 1, 'Xe 5 năm (5-10 năm)', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231206-0004'), 2, 0, 'Cá nhân', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231206-0004'), 3, 2, 'Tài xế trẻ 24 tuổi', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231206-0004'), 4, 1, 'Xe 480 triệu (200-500tr)', NOW());

-- Hồ sơ HS-20231208-0005 (Risk Score = 6)
INSERT INTO chi_tiet_tham_dinh (ho_so_tham_dinh_id, tieu_chi_id, diem, ghi_chu, created_at) VALUES
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231208-0005'), 1, 0, 'Xe 3 năm', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231208-0005'), 2, 2, 'Kinh doanh vận tải', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231208-0005'), 3, 2, 'Tài xế 23 tuổi', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231208-0005'), 4, 2, 'Xe pickup giá trị TB', NOW());

-- Hồ sơ HS-20231210-0006 (Risk Score = 3)
INSERT INTO chi_tiet_tham_dinh (ho_so_tham_dinh_id, tieu_chi_id, diem, ghi_chu, created_at) VALUES
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231210-0006'), 1, 0, 'Xe 4 năm', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231210-0006'), 2, 0, 'Cá nhân', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231210-0006'), 3, 1, 'Tài xế cao tuổi 58', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231210-0006'), 4, 2, 'Xe nhỏ 320 triệu', NOW());

-- Hồ sơ HS-20231212-0007 (Risk Score = 0)
INSERT INTO chi_tiet_tham_dinh (ho_so_tham_dinh_id, tieu_chi_id, diem, ghi_chu, created_at) VALUES
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231212-0007'), 1, 0, 'Xe sang mới 1 năm', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231212-0007'), 2, 0, 'Cá nhân', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231212-0007'), 3, 0, 'Tài xế giàu kinh nghiệm 40 tuổi', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231212-0007'), 4, 0, 'Mercedes C200 - xe cao cấp >500tr', NOW());

-- Hồ sơ HS-20231213-0008 (Risk Score = 1)
INSERT INTO chi_tiet_tham_dinh (ho_so_tham_dinh_id, tieu_chi_id, diem, ghi_chu, created_at) VALUES
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231213-0008'), 1, 0, 'Xe 2 năm', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231213-0008'), 2, 0, 'Cá nhân', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231213-0008'), 3, 0, 'Tài xế 40 tuổi', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231213-0008'), 4, 1, 'Fortuner 1.15 tỷ', NOW());

-- Hồ sơ HS-20231215-0009 (Risk Score = 5)
INSERT INTO chi_tiet_tham_dinh (ho_so_tham_dinh_id, tieu_chi_id, diem, ghi_chu, created_at) VALUES
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231215-0009'), 1, 0, 'Xe 3 năm', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231215-0009'), 2, 0, 'Cá nhân', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231215-0009'), 3, 2, 'Tài xế 22 tuổi', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231215-0009'), 4, 1, 'Xe 420 triệu', NOW());

-- Hồ sơ HS-20231218-0010 (Risk Score = 2)
INSERT INTO chi_tiet_tham_dinh (ho_so_tham_dinh_id, tieu_chi_id, diem, ghi_chu, created_at) VALUES
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231218-0010'), 1, 0, 'Xe 2 năm', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231218-0010'), 2, 0, 'Cá nhân', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231218-0010'), 3, 0, 'Tài xế 32 tuổi', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20231218-0010'), 4, 2, 'Honda CR-V 1.05 tỷ', NOW());

-- Hồ sơ HS-20240105-0015 (Risk Score = 3)
INSERT INTO chi_tiet_tham_dinh (ho_so_tham_dinh_id, tieu_chi_id, diem, ghi_chu, created_at) VALUES
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20240105-0015'), 1, 0, 'Xe 3 năm', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20240105-0015'), 2, 0, 'Cá nhân', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20240105-0015'), 3, 2, 'Tài xế ít kinh nghiệm', NOW()),
((SELECT id FROM ho_so_tham_dinh WHERE ma_hs = 'HS-20240105-0015'), 4, 1, 'Mazda 3 - 720 triệu', NOW());

SELECT 'Đã insert ' || COUNT(*) || ' bản ghi chi tiết thẩm định thành công!' as result FROM chi_tiet_tham_dinh;
