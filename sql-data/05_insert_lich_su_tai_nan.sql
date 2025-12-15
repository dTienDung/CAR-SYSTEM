-- ============================================
-- FILE: 07_insert_lich_su_tai_nan.sql
-- MÔ TẢ: Insert dữ liệu cho bảng LICH_SU_TAI_NAN
-- LƯU Ý: Cần chạy file 03_insert_xe.sql trước
-- ============================================

-- Xóa dữ liệu cũ (nếu có)
-- DELETE FROM lich_su_tai_nan;

-- Insert dữ liệu Lịch sử tai nạn
INSERT INTO lich_su_tai_nan (xe_id, ngay_xay_ra, mo_ta, thiet_hai, dia_diem, created_at) VALUES
-- Tai nạn của các xe
-- Xe XE000004 có 1 tai nạn nhẹ
((SELECT id FROM xe WHERE ma_xe = 'XE000004'), '2023-05-15', 'Va chạm nhẹ ở bãi đậu xe, trầy sơn cản trước', 3500000.00, 'Bãi đậu xe BigC Thăng Long, Hà Nội', NOW()),

-- Xe XE000005 có 2 tai nạn
((SELECT id FROM xe WHERE ma_xe = 'XE000005'), '2022-08-20', 'Đâm vào xe phía trước khi đang dừng đèn đỏ', 12000000.00, 'Ngã tư Láng Hạ - Thái Hà, Hà Nội', NOW()),
((SELECT id FROM xe WHERE ma_xe = 'XE000005'), '2023-11-10', 'Trầy xước thân xe do va chạm nhẹ', 5000000.00, 'Đường Giải Phóng, Hà Nội', NOW()),

-- Xe XE000006 có 1 tai nạn
((SELECT id FROM xe WHERE ma_xe = 'XE000006'), '2023-03-25', 'Gương chiếu hậu bị xe máy đâm vỡ', 2500000.00, 'Phố Tây Sơn, Đống Đa, Hà Nội', NOW()),

-- Xe XE000009 có 1 tai nạn trung bình
((SELECT id FROM xe WHERE ma_xe = 'XE000009'), '2023-06-12', 'Đâm vào dải phân cách, hư hỏng đầu xe', 18000000.00, 'Đại lộ Thăng Long, Hà Nội', NOW()),

-- Xe XE000011 có 1 tai nạn nhẹ
((SELECT id FROM xe WHERE ma_xe = 'XE000011'), '2023-09-08', 'Trầy sơn cửa xe do va chạm trong hẻm', 4000000.00, 'Ngõ Chùa Bộc, Đống Đa, Hà Nội', NOW()),

-- Xe XE000013 có 2 tai nạn
((SELECT id FROM xe WHERE ma_xe = 'XE000013'), '2022-12-05', 'Va chạm với xe máy, vỡ đèn trước', 6500000.00, 'Đường Nguyễn Trãi, Thanh Xuân, Hà Nội', NOW()),
((SELECT id FROM xe WHERE ma_xe = 'XE000013'), '2023-07-18', 'Hư hỏng cản sau do lùi xe không quan sát', 3000000.00, 'Khu đô thị Times City, Hà Nội', NOW()),

-- Xe XE000014 có 1 tai nạn nghiêm trọng
((SELECT id FROM xe WHERE ma_xe = 'XE000014'), '2023-01-20', 'Va chạm mạnh với xe tải, hư hỏng nặng phần đầu xe', 45000000.00, 'Quốc lộ 1A, đoạn qua Hà Nam', NOW()),

-- Thêm một số tai nạn khác
((SELECT id FROM xe WHERE ma_xe = 'XE000001'), '2023-04-10', 'Trầy sát-xi cản sau khi đỗ xe', 2000000.00, 'Tòa nhà Keangnam, Hà Nội', NOW()),

((SELECT id FROM xe WHERE ma_xe = 'XE000003'), '2023-08-22', 'Vỡ kính hông do trộm đập', 8000000.00, 'Bãi giữ xe phố Huế, Hà Nội', NOW()),

((SELECT id FROM xe WHERE ma_xe = 'XE000007'), '2023-02-14', 'Tai nạn nhẹ khi lùi xe', 4500000.00, 'Hầm gửi xe Vincom Metropolis, Hà Nội', NOW()),

((SELECT id FROM xe WHERE ma_xe = 'XE000010'), '2023-10-05', 'Va chạm với xe máy từ phía sau', 7500000.00, 'Đường Trường Chinh, Đống Đa, Hà Nội', NOW()),

((SELECT id FROM xe WHERE ma_xe = 'XE000012'), '2023-05-28', 'Trầy sơn và móp cửa xe', 9000000.00, 'Ngã tư Ô Chợ Dừa, Hà Nội', NOW());

SELECT 'Đã insert ' || COUNT(*) || ' bản ghi lịch sử tai nạn thành công!' as result FROM lich_su_tai_nan;
