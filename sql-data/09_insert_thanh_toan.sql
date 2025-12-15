-- ============================================
-- FILE: 09_insert_thanh_toan.sql
-- MÔ TẢ: Insert dữ liệu cho bảng THANH_TOAN
-- LƯU Ý: Cần chạy file 08_insert_hop_dong.sql trước
--        Người thu tiền: ACCOUNTANT
-- ============================================

-- Xóa dữ liệu cũ (nếu có)
-- DELETE FROM thanh_toan;

-- Insert dữ liệu Thanh toán (tất cả được xử lý bởi ACCOUNTANT)
INSERT INTO thanh_toan (matt, hop_dong_id, so_tien, phuong_thuc, so_tai_khoan, so_the, ghi_chu, is_hoan_phi, nguoi_thu_id, created_at) VALUES
-- Thanh toán cho hợp đồng HD-20231201-0001
('TT-20231205-0001',
 (SELECT id FROM hop_dong WHERE mahd = 'HD-20231201-0001'),
 5000000.00, 'CHUYEN_KHOAN', '19036668888888', NULL,
 'Thanh toán toàn bộ phí bảo hiểm',
 false,
 (SELECT id FROM users WHERE username = 'accountant'),
 '2023-12-05 09:30:00'),

-- Thanh toán cho hợp đồng HD-20231202-0002
('TT-20231206-0002',
 (SELECT id FROM hop_dong WHERE mahd = 'HD-20231202-0002'),
 3600000.00, 'TIEN_MAT', NULL, NULL,
 'Thanh toán bằng tiền mặt tại quầy',
 false,
 (SELECT id FROM users WHERE username = 'accountant'),
 '2023-12-06 08:45:00'),

-- Thanh toán cho hợp đồng HD-20231205-0003
('TT-20231210-0003',
 (SELECT id FROM hop_dong WHERE mahd = 'HD-20231205-0003'),
 8000000.00, 'CHUYEN_KHOAN', '19036669999999', NULL,
 'Chuyển khoản phí BH toàn diện',
 false,
 (SELECT id FROM users WHERE username = 'accountant'),
 '2023-12-10 13:30:00'),

-- Thanh toán cho hợp đồng HD-20231206-0004 (thanh toán 2 lần)
('TT-20231210-0004',
 (SELECT id FROM hop_dong WHERE mahd = 'HD-20231206-0004'),
 3000000.00, 'TIEN_MAT', NULL, NULL,
 'Thanh toán đợt 1',
 false,
 (SELECT id FROM users WHERE username = 'accountant'),
 '2023-12-10 10:15:00'),

('TT-20231220-0005',
 (SELECT id FROM hop_dong WHERE mahd = 'HD-20231206-0004'),
 3000000.00, 'CHUYEN_KHOAN', '19036660000001', NULL,
 'Thanh toán đợt 2 - hoàn tất hợp đồng',
 false,
 (SELECT id FROM users WHERE username = 'accountant'),
 '2023-12-20 14:20:00'),

-- Thanh toán cho hợp đồng HD-20231208-0005
('TT-20231212-0006',
 (SELECT id FROM hop_dong WHERE mahd = 'HD-20231208-0005'),
 9000000.00, 'CHUYEN_KHOAN', '19036660000002', NULL,
 'Thanh toán BH kinh doanh',
 false,
 (SELECT id FROM users WHERE username = 'accountant'),
 '2023-12-12 09:45:00'),

-- Thanh toán cho hợp đồng HD-20231210-0006
('TT-20231215-0007',
 (SELECT id FROM hop_dong WHERE mahd = 'HD-20231210-0006'),
 3600000.00, 'POS_THE', NULL, '9704XXXXXXXX3456',
 'Thanh toán qua POS',
 false,
 (SELECT id FROM users WHERE username = 'accountant'),
 '2023-12-15 08:30:00'),

-- Thanh toán cho hợp đồng HD-20231212-0007
('TT-20231215-0008',
 (SELECT id FROM hop_dong WHERE mahd = 'HD-20231212-0007'),
 15000000.00, 'CHUYEN_KHOAN', '19036660000003', NULL,
 'Thanh toán BH VIP Mercedes',
 false,
 (SELECT id FROM users WHERE username = 'accountant'),
 '2023-12-15 09:15:00'),

-- Thanh toán cho hợp đồng HD-20231213-0008
('TT-20231218-0009',
 (SELECT id FROM hop_dong WHERE mahd = 'HD-20231213-0008'),
 8000000.00, 'CHUYEN_KHOAN', '19036660000004', NULL,
 'Thanh toán BH Fortuner',
 false,
 (SELECT id FROM users WHERE username = 'accountant'),
 '2023-12-18 10:20:00'),

-- Thanh toán cho hợp đồng HD-20231215-0009
('TT-20231220-0010',
 (SELECT id FROM hop_dong WHERE mahd = 'HD-20231215-0009'),
 6000000.00, 'POS_THE', NULL, '9704XXXXXXXX5678',
 'Thanh toán qua thẻ tín dụng',
 false,
 (SELECT id FROM users WHERE username = 'accountant'),
 '2023-12-20 13:45:00'),

-- Thanh toán cho hợp đồng HD-20231218-0010
('TT-20231222-0011',
 (SELECT id FROM hop_dong WHERE mahd = 'HD-20231218-0010'),
 8000000.00, 'CHUYEN_KHOAN', '19036660000005', NULL,
 'Thanh toán BH Honda CR-V',
 false,
 (SELECT id FROM users WHERE username = 'accountant'),
 '2023-12-22 09:30:00'),

-- Thanh toán cho hợp đồng HD-20240105-0011
('TT-20240110-0012',
 (SELECT id FROM hop_dong WHERE mahd = 'HD-20240105-0011'),
 6000000.00, 'TIEN_MAT', NULL, NULL,
 'Thanh toán BH năm 2024',
 false,
 (SELECT id FROM users WHERE username = 'accountant'),
 '2024-01-10 08:15:00');

SELECT 'Đã insert ' || COUNT(*) || ' giao dịch thanh toán thành công!' as result FROM thanh_toan;
