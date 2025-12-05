-- =====================================================
-- MA TRẬN TÍNH PHÍ LINH HOẠT (Dựa trên % điểm rủi ro)
-- Hệ thống Bảo hiểm Xe - MySQL Database
-- =====================================================

SET FOREIGN_KEY_CHECKS = 0;
SET SQL_SAFE_UPDATES = 0;

-- Xóa dữ liệu cũ
DELETE FROM ma_tran_tinh_phi;

-- =====================================================
-- MA TRẬN TÍNH PHÍ THEO % ĐIỂM RỦI RO
-- =====================================================
-- Lưu ý: diem_rui_ro_tu và diem_rui_ro_den giờ đại diện cho % (0-100)
-- Hệ thống sẽ tự động tính % điểm rủi ro và tìm ma trận phù hợp

INSERT INTO ma_tran_tinh_phi (id, diem_rui_ro_tu, diem_rui_ro_den, he_so_phi, mo_ta, active, created_at, updated_at) VALUES

-- =====================================================
-- CẤP ĐỘ THẤP (0-25% = CHẤP NHẬN)
-- =====================================================
(1, 0, 10, 0.8, 
'Rủi ro rất thấp (0-10% tổng điểm)
→ GIẢM 20% phí cơ bản', 
1, NOW(), NOW()),

(2, 11, 25, 1.0, 
'Rủi ro thấp (11-25% tổng điểm)
→ PHÍ CƠ BẢN CHUẨN', 
1, NOW(), NOW()),

-- =====================================================
-- CẤP ĐỘ TRUNG BÌNH (26-50% = XEM XÉT)
-- =====================================================
(3, 26, 35, 1.2, 
'Rủi ro trung bình thấp (26-35% tổng điểm)
→ TĂNG 20% phí cơ bản', 
1, NOW(), NOW()),

(4, 36, 50, 1.5, 
'Rủi ro trung bình (36-50% tổng điểm)
→ TĂNG 50% phí cơ bản', 
1, NOW(), NOW()),

-- =====================================================
-- CẤP ĐỘ CAO (51-100% = TỪ CHỐI hoặc PHÍ CAO)
-- =====================================================
(5, 51, 65, 1.8, 
'Rủi ro trung bình cao (51-65% tổng điểm)
→ TĂNG 80% phí cơ bản', 
1, NOW(), NOW()),

(6, 66, 80, 2.2, 
'Rủi ro cao (66-80% tổng điểm)
→ TĂNG 120% phí cơ bản', 
1, NOW(), NOW()),

(7, 81, 100, 2.5, 
'Rủi ro rất cao (81-100% tổng điểm)
→ TĂNG 150% phí cơ bản', 
1, NOW(), NOW());

-- Xóa các cấp độ cũ không dùng
DELETE FROM ma_tran_tinh_phi WHERE id > 7;

-- Reset lại các cấp độ cũ
UPDATE ma_tran_tinh_phi SET id = 1 WHERE diem_rui_ro_tu = 0 AND diem_rui_ro_den = 10;
UPDATE ma_tran_tinh_phi SET id = 2 WHERE diem_rui_ro_tu = 11 AND diem_rui_ro_den = 25;
UPDATE ma_tran_tinh_phi SET id = 3 WHERE diem_rui_ro_tu = 26 AND diem_rui_ro_den = 35;
UPDATE ma_tran_tinh_phi SET id = 4 WHERE diem_rui_ro_tu = 36 AND diem_rui_ro_den = 50;
UPDATE ma_tran_tinh_phi SET id = 5 WHERE diem_rui_ro_tu = 51 AND diem_rui_ro_den = 65;
UPDATE ma_tran_tinh_phi SET id = 6 WHERE diem_rui_ro_tu = 66 AND diem_rui_ro_den = 80;
UPDATE ma_tran_tinh_phi SET id = 7 WHERE diem_rui_ro_tu = 81 AND diem_rui_ro_den = 100;

-- Bỏ phần còn lại
/*
(3, 11, 25, 1.0,

(3, 11, 25, 1.0, 
'✅ RỦI RO THẤP (11-25% tổng điểm)
- Xe còn mới (1-3 năm)
- Lịch sử sử dụng tốt
- Ít hoặc không có tai nạn
- Chủ xe ổn định, có kinh nghiệm
- Điều kiện bảo quản tốt
→ PHÍ CƠ BẢN CHUẨN
→ Khách hàng tốt', 
1, NOW(), NOW()),

-- =====================================================
-- CẤP ĐỘ TRUNG BÌNH (26-50% = XEM XÉT)
-- =====================================================
(4, 26, 35, 1.2, 
'⚠️ RỦI RO TRUNG BÌNH THẤP (26-35% tổng điểm)
- Xe 3-5 năm tuổi
- Có thể có 1 tai nạn nhỏ trong quá khứ
- Điều kiện sử dụng bình thường
- Chủ xe có kinh nghiệm trung bình
- Bảo dưỡng định kỳ
→ TĂNG 20% phí cơ bản
→ Cần xem xét kỹ hồ sơ', 
1, NOW(), NOW()),

(5, 36, 50, 1.5, 
'⚠️ RỦI RO TRUNG BÌNH (36-50% tổng điểm)
- Xe 5-8 năm tuổi
- Có lịch sử tai nạn (1-2 lần)
- Sử dụng thương mại hoặc khu vực nguy hiểm
- Chủ xe ít kinh nghiệm hoặc cao tuổi
- Bảo dưỡng không đều
→ TĂNG 50% phí cơ bản
→ Xem xét kỹ trước khi chấp nhận', 
1, NOW(), NOW()),

-- =====================================================
-- CẤP ĐỘ CAO (51-100% = TỪ CHỐI hoặc PHÍ CAO)
-- =====================================================
(6, 51, 65, 1.8, 
'❌ RỦI RO TRUNG BÌNH CAO (51-65% tổng điểm)
- Xe cũ (8-12 năm)
- Nhiều lịch sử tai nạn (2-3 lần)
- Lái xe ít kinh nghiệm hoặc cao tuổi
- Sử dụng thương mại, khu vực nguy hiểm
- Điều kiện bảo quản kém
→ TĂNG 80% phí cơ bản
→ Cân nhắc kỹ, có thể yêu cầu điều kiện đặc biệt', 
1, NOW(), NOW()),

(7, 66, 80, 2.2, 
'❌ RỦI RO CAO (66-80% tổng điểm)
- Xe rất cũ (>12 năm)
- Lịch sử tai nạn nhiều (>3 lần)
- Điều kiện sử dụng xấu
- Nhiều yếu tố rủi ro kết hợp
- Lịch sử bồi thường nhiều
→ TĂNG 120% phí cơ bản
→ Cân nhắc từ chối hoặc yêu cầu điều kiện đặc biệt', 
1, NOW(), NOW()),

(8, 81, 90, 2.5, 
'❌ RỦI RO RẤT CAO (81-90% tổng điểm)
- Xe quá cũ (>15 năm)
- Lịch sử tai nạn rất nhiều
- Nhiều yếu tố rủi ro nghiêm trọng
- Tình trạng xe kém
- Không đủ điều kiện bảo hiểm thông thường
→ TĂNG 150% phí cơ bản
→ Nên từ chối hoặc yêu cầu thẩm định đặc biệt', 
1, NOW(), NOW()),

(9, 91, 100, 3.0, 
'🚫 RỦI RO CỰC CAO (91-100% tổng điểm)
- Tổng hợp nhiều yếu tố rủi ro cực kỳ nghiêm trọng
- Không đủ điều kiện bảo hiểm
- Rủi ro quá cao, không thể chấp nhận
→ TĂNG 200% phí cơ bản
→ TỪ CHỐI BẢO HIỂM hoặc yêu cầu điều kiện đặc biệt rất nghiêm ngặt', 
1, NOW(), NOW());

-- =====================================================
-- THỐNG KÊ MA TRẬN
-- =====================================================

SELECT 
    '📊 THỐNG KÊ MA TRẬN TÍNH PHÍ' AS thong_ke,
    '' AS gia_tri;

SELECT 
    CONCAT('Cấp độ ', id) AS cap_do,
    CONCAT(diem_rui_ro_tu, '% - ', diem_rui_ro_den, '%') AS khoang_phan_tram,
    CONCAT('x', he_so_phi) AS he_so,
    CASE 
        WHEN he_so_phi < 1.0 THEN CONCAT('Giảm ', ROUND((1.0 - he_so_phi) * 100), '%')
        WHEN he_so_phi = 1.0 THEN 'Phí chuẩn'
        ELSE CONCAT('Tăng ', ROUND((he_so_phi - 1.0) * 100), '%')
    END AS thay_doi,
    mo_ta
FROM ma_tran_tinh_phi
WHERE active = 1
ORDER BY diem_rui_ro_tu;

-- =====================================================
-- VÍ DỤ TÍNH PHÍ
-- =====================================================

SELECT 
    '💡 VÍ DỤ TÍNH PHÍ' AS vi_du,
    '' AS gia_tri;

-- Giả sử gói bảo hiểm cơ bản: 8,000,000 VNĐ
SELECT 
    'Gói Tiêu chuẩn' AS goi_bao_hiem,
    '8,000,000 VNĐ' AS phi_co_ban,
    '' AS diem_rui_ro,
    '' AS phan_tram,
    '' AS he_so,
    '' AS phi_thuc_te;

SELECT 
    'Khách hàng VIP' AS loai_khach,
    '8,000,000' AS phi_co_ban,
    '-5 điểm' AS diem_rui_ro,
    'VIP' AS phan_tram,
    '0.7' AS he_so,
    '5,600,000 VNĐ' AS phi_thuc_te
UNION ALL
SELECT 
    'Rủi ro rất thấp',
    '8,000,000',
    '5 điểm',
    '5%',
    '0.8',
    '6,400,000 VNĐ'
UNION ALL
SELECT 
    'Rủi ro thấp',
    '8,000,000',
    '15 điểm',
    '15%',
    '1.0',
    '8,000,000 VNĐ'
UNION ALL
SELECT 
    'Rủi ro TB thấp',
    '8,000,000',
    '30 điểm',
    '30%',
    '1.2',
    '9,600,000 VNĐ'
UNION ALL
SELECT 
    'Rủi ro TB',
    '8,000,000',
    '45 điểm',
    '45%',
    '1.5',
    '12,000,000 VNĐ'
UNION ALL
SELECT 
    'Rủi ro TB cao',
    '8,000,000',
    '60 điểm',
    '60%',
    '1.8',
    '14,400,000 VNĐ'
UNION ALL
SELECT 
    'Rủi ro cao',
    '8,000,000',
    '75 điểm',
    '75%',
    '2.2',
    '17,600,000 VNĐ'
UNION ALL
SELECT 
    'Rủi ro rất cao',
    '8,000,000',
    '85 điểm',
    '85%',
    '2.5',
    '20,000,000 VNĐ'
UNION ALL
SELECT 
    'Rủi ro cực cao',
    '8,000,000',
    '95 điểm',
    '95%',
    '3.0',
    '24,000,000 VNĐ';

-- =====================================================
-- HƯỚNG DẪN SỬ DỤNG
-- =====================================================

SELECT 
    '📖 HƯỚNG DẪN' AS huong_dan,
    '' AS noi_dung;

SELECT 
    '1. Hệ thống tự động tính % điểm rủi ro' AS buoc,
    'Ví dụ: 20 điểm / 80 tổng điểm = 25%' AS mo_ta
UNION ALL
SELECT 
    '2. Tìm ma trận phù hợp theo %',
    '25% → Rơi vào khoảng 11-25% → Hệ số 1.0'
UNION ALL
SELECT 
    '3. Tính phí bảo hiểm',
    'Phí = Phí cơ bản × Hệ số'
UNION ALL
SELECT 
    '4. Linh hoạt với mọi tổng điểm',
    'Thêm/bớt tiêu chí → Tự động điều chỉnh';

-- =====================================================
-- HOÀN TẤT!
-- =====================================================

SET FOREIGN_KEY_CHECKS = 1;
SET SQL_SAFE_UPDATES = 1;

SELECT 
    '✅ Dữ liệu ma trận tính phí đã được tải thành công!' AS status,
    '9 cấp độ rủi ro' AS so_luong,
    'Dựa trên % điểm (0-100%)' AS loai,
    'Hoàn toàn linh hoạt' AS tinh_nang;
