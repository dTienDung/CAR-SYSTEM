-- =====================================================
-- DỮ LIỆU 8 TIÊU CHÍ CƠ BẢN - HỆ THỐNG LINH HOẠT
-- Hệ thống Bảo hiểm Xe - MySQL Database
-- Tổng điểm: 50 điểm (phù hợp với ngưỡng 25%-50%)
-- =====================================================

SET FOREIGN_KEY_CHECKS = 0;
SET SQL_SAFE_UPDATES = 0;

-- =====================================================
-- 1. GÓI BẢO HIỂM (8 gói)
-- =====================================================

DELETE FROM goi_bao_hiem;

INSERT INTO goi_bao_hiem (id, ma_goi, ten_goi, phi_co_ban, mo_ta, active, created_at, updated_at) VALUES
(1, 'GOI-BASIC-001', 'Gói Tiết kiệm', 5000000, 'Bảo hiểm vật chất cơ bản - Phù hợp xe cũ', 1, NOW(), NOW()),
(2, 'GOI-BASIC-002', 'Gói Cơ bản Plus', 6500000, 'Bảo hiểm vật chất + TNDS bắt buộc', 1, NOW(), NOW()),
(3, 'GOI-STANDARD-001', 'Gói Tiêu chuẩn', 8000000, 'Bảo hiểm toàn diện cơ bản', 1, NOW(), NOW()),
(4, 'GOI-STANDARD-002', 'Gói Tiêu chuẩn Plus', 10000000, 'Bảo hiểm toàn diện + Mất cắp', 1, NOW(), NOW()),
(5, 'GOI-PREMIUM-001', 'Gói Cao cấp', 12000000, 'Bảo hiểm toàn diện cao cấp', 1, NOW(), NOW()),
(6, 'GOI-PREMIUM-002', 'Gói VIP', 15000000, 'Bảo hiểm VIP - Xe sang', 1, NOW(), NOW()),
(7, 'GOI-SPECIAL-001', 'Gói Taxi/Grab', 9000000, 'Chuyên dụng xe kinh doanh vận tải', 1, NOW(), NOW()),
(8, 'GOI-SPECIAL-002', 'Gói Doanh nghiệp', 11000000, 'Cho xe công ty, doanh nghiệp', 1, NOW(), NOW());

-- =====================================================
-- 2. TIÊU CHÍ THẨM ĐỊNH (8 tiêu chí - Tổng 50 điểm)
-- =====================================================

DELETE FROM tieu_chi_tham_dinh;

