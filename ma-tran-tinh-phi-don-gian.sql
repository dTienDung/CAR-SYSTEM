-- =====================================================
-- MA TRẬN TÍNH PHÍ ĐƠN GIẢN (Dựa trên % điểm rủi ro)
-- Hệ thống Bảo hiểm Xe - MySQL Database
-- =====================================================

SET FOREIGN_KEY_CHECKS = 0;
SET SQL_SAFE_UPDATES = 0;

-- Xóa dữ liệu cũ
DELETE FROM ma_tran_tinh_phi;

-- =====================================================
-- MA TRẬN TÍNH PHÍ - 7 CẤP ĐỘ
-- =====================================================
-- Hệ thống tự động tính: % = (Điểm rủi ro / Tổng điểm) × 100
-- Sau đó tìm ma trận phù hợp và áp dụng hệ số

INSERT INTO ma_tran_tinh_phi (id, diem_rui_ro_tu, diem_rui_ro_den, he_so_phi, mo_ta, active, created_at, updated_at) VALUES

(1, 0, 10, 0.8, 'Rủi ro rất thấp (0-10%) → Giảm 20%', 1, NOW(), NOW()),
(2, 11, 25, 1.0, 'Rủi ro thấp (11-25%) → Phí chuẩn', 1, NOW(), NOW()),
(3, 26, 35, 1.2, 'Rủi ro TB thấp (26-35%) → Tăng 20%', 1, NOW(), NOW()),
(4, 36, 50, 1.5, 'Rủi ro TB (36-50%) → Tăng 50%', 1, NOW(), NOW()),
(5, 51, 65, 1.8, 'Rủi ro TB cao (51-65%) → Tăng 80%', 1, NOW(), NOW()),
(6, 66, 80, 2.2, 'Rủi ro cao (66-80%) → Tăng 120%', 1, NOW(), NOW()),
(7, 81, 100, 2.5, 'Rủi ro rất cao (81-100%) → Tăng 150%', 1, NOW(), NOW());

-- =====================================================
-- VÍ DỤ TÍNH PHÍ
-- =====================================================

SELECT '💡 VÍ DỤ TÍNH PHÍ (Gói 8,000,000 VNĐ)' AS vi_du;

SELECT 
    CONCAT(diem_rui_ro_tu, '-', diem_rui_ro_den, '%') AS khoang_phan_tram,
    CONCAT(he_so_phi, 'x') AS he_so,
    CASE 
        WHEN he_so_phi < 1.0 THEN CONCAT('Giảm ', ROUND((1.0 - he_so_phi) * 100), '%')
        WHEN he_so_phi = 1.0 THEN 'Phí chuẩn'
        ELSE CONCAT('Tăng ', ROUND((he_so_phi - 1.0) * 100), '%')
    END AS thay_doi,
    CONCAT(FORMAT(8000000 * he_so_phi, 0), ' VNĐ') AS phi_thuc_te
FROM ma_tran_tinh_phi
ORDER BY diem_rui_ro_tu;

-- =====================================================
-- HOÀN TẤT!
-- =====================================================

SET FOREIGN_KEY_CHECKS = 1;
SET SQL_SAFE_UPDATES = 1;

SELECT 
    '✅ Đã tải 7 cấp độ ma trận tính phí!' AS status,
    'Dựa trên % điểm rủi ro (0-100%)' AS loai,
    'Hoàn toàn linh hoạt với mọi tổng điểm' AS tinh_nang;
