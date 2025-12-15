-- ============================================
-- FILE: 06_insert_ho_so_tham_dinh.sql
-- MÔ TẢ: Insert dữ liệu cho bảng HO_SO_THAM_DINH
-- LƯU Ý: Cần chạy các file 01, 02, 03, 04 trước
--        Risk Score: 0-8 điểm (4 tiêu chí × 2 điểm)
--        Risk Level: LOW (0-2), MEDIUM (3-5), HIGH (6-8)
-- ============================================

-- Xóa dữ liệu cũ (nếu có)
-- DELETE FROM ho_so_tham_dinh;

-- Insert dữ liệu Hồ sơ thẩm định
INSERT INTO ho_so_tham_dinh (mahs, khach_hang_id, xe_id, goi_bao_hiem_id, risk_score, risk_level, trang_thai, phi_bao_hiem, ghi_chu, nguoi_tham_dinh_id, created_at, updated_at) VALUES
-- Hồ sơ đã hoàn thành
('HS-20231201-0001', 
 (SELECT id FROM khach_hang WHERE makh = 'KH000001'),
 (SELECT id FROM xe WHERE ma_xe = 'XE000001'), 
 (SELECT id FROM goi_bao_hiem WHERE ma_goi = 'GOI002'), 
 2, 'LOW', 'HOAN_THANH', 5000000.00, 'Xe mới, tài xế trung niên, an toàn', 
 (SELECT id FROM users WHERE username = 'underwriter'), 
 '2023-12-01 09:00:00', '2023-12-01 15:30:00'),

('HS-20231202-0002', 
 (SELECT id FROM khach_hang WHERE makh = 'KH000002'), 
 (SELECT id FROM xe WHERE ma_xe = 'XE000002'), 
 (SELECT id FROM goi_bao_hiem WHERE ma_goi = 'GOI001'), 
 3, 'MEDIUM', 'HOAN_THANH', 3600000.00, 'Xe 4 năm, rủi ro trung bình', 
 (SELECT id FROM users WHERE username = 'underwriter'), 
 '2023-12-02 10:00:00', '2023-12-02 16:00:00'),

('HS-20231205-0003', 
 (SELECT id FROM khach_hang WHERE makh = 'KH000003'), 
 (SELECT id FROM xe WHERE ma_xe = 'XE000003'), 
 (SELECT id FROM goi_bao_hiem WHERE ma_goi = 'GOI003'), 
 1, 'LOW', 'HOAN_THANH', 8000000.00, 'Xe cao cấp, tài xế có kinh nghiệm', 
 (SELECT id FROM users WHERE username = 'underwriter'), 
 '2023-12-05 08:30:00', '2023-12-05 17:00:00'),

('HS-20231206-0004', 
 (SELECT id FROM khach_hang WHERE makh = 'KH000004'), 
 (SELECT id FROM xe WHERE ma_xe = 'XE000004'), 
 (SELECT id FROM goi_bao_hiem WHERE ma_goi = 'GOI002'), 
 4, 'MEDIUM', 'HOAN_THANH', 6000000.00, 'Xe 5 năm, điểm rủi ro trung bình', 
 (SELECT id FROM users WHERE username = 'underwriter'), 
 '2023-12-06 09:15:00', '2023-12-06 14:45:00'),

('HS-20231208-0005', 
 (SELECT id FROM khach_hang WHERE makh = 'KH000005'), 
 (SELECT id FROM xe WHERE ma_xe = 'XE000005'), 
 (SELECT id FROM goi_bao_hiem WHERE ma_goi = 'GOI005'), 
 6, 'HIGH', 'HOAN_THANH', 9000000.00, 'Xe kinh doanh, rủi ro cao', 
 (SELECT id FROM users WHERE username = 'underwriter'), 
 '2023-12-08 10:00:00', '2023-12-08 16:30:00'),

('HS-20231210-0006', 
 (SELECT id FROM khach_hang WHERE makh = 'KH000006'), 
 (SELECT id FROM xe WHERE ma_xe = 'XE000006'), 
 (SELECT id FROM goi_bao_hiem WHERE ma_goi = 'GOI001'), 
 3, 'MEDIUM', 'HOAN_THANH', 3600000.00, 'Xe nhỏ, rủi ro trung bình', 
 (SELECT id FROM users WHERE username = 'underwriter'), 
 '2023-12-10 11:00:00', '2023-12-10 15:00:00'),

('HS-20231212-0007', 
 (SELECT id FROM khach_hang WHERE makh = 'KH000007'), 
 (SELECT id FROM xe WHERE ma_xe = 'XE000007'), 
 (SELECT id FROM goi_bao_hiem WHERE ma_goi = 'GOI004'), 
 0, 'LOW', 'HOAN_THANH', 15000000.00, 'Xe sang mới, khách hàng VIP', 
 (SELECT id FROM users WHERE username = 'underwriter'), 
 '2023-12-12 09:00:00', '2023-12-12 17:30:00'),

('HS-20231213-0008', 
 (SELECT id FROM khach_hang WHERE makh = 'KH000007'), 
 (SELECT id FROM xe WHERE ma_xe = 'XE000008'), 
 (SELECT id FROM goi_bao_hiem WHERE ma_goi = 'GOI003'), 
 1, 'LOW', 'HOAN_THANH', 8000000.00, 'Xe 7 chỗ mới, rủi ro thấp', 
 (SELECT id FROM users WHERE username = 'underwriter'), 
 '2023-12-13 10:30:00', '2023-12-13 16:00:00'),

('HS-20231215-0009', 
 (SELECT id FROM khach_hang WHERE makh = 'KH000008'), 
 (SELECT id FROM xe WHERE ma_xe = 'XE000009'), 
 (SELECT id FROM goi_bao_hiem WHERE ma_goi = 'GOI002'), 
 5, 'MEDIUM', 'HOAN_THANH', 6000000.00, 'Xe cũ, rủi ro cao hơn', 
 (SELECT id FROM users WHERE username = 'underwriter'), 
 '2023-12-15 08:00:00', '2023-12-15 14:00:00'),

('HS-20231218-0010', 
 (SELECT id FROM khach_hang WHERE makh = 'KH000009'), 
 (SELECT id FROM xe WHERE ma_xe = 'XE000010'), 
 (SELECT id FROM goi_bao_hiem WHERE ma_goi = 'GOI003'), 
 2, 'LOW', 'HOAN_THANH', 8000000.00, 'Xe SUV, tình trạng tốt', 
 (SELECT id FROM users WHERE username = 'underwriter'), 
 '2023-12-18 09:30:00', '2023-12-18 15:30:00'),

-- Hồ sơ đang xử lý
('HS-20240101-0011', 
 (SELECT id FROM khach_hang WHERE makh = 'KH000011'), 
 (SELECT id FROM xe WHERE ma_xe = 'XE000012'), 
 (SELECT id FROM goi_bao_hiem WHERE ma_goi = 'GOI003'), 
 0, 'LOW', 'DANG_XU_LY', NULL, 'Đang chờ thẩm định', 
 (SELECT id FROM users WHERE username = 'underwriter'), 
 NOW(), NOW()),

('HS-20240102-0012', 
 (SELECT id FROM khach_hang WHERE makh = 'KH000012'), 
 (SELECT id FROM xe WHERE ma_xe = 'XE000013'), 
 (SELECT id FROM goi_bao_hiem WHERE ma_goi = 'GOI002'), 
 0, 'LOW', 'CHO_BO_SUNG', NULL, 'Cần bổ sung giấy tờ xe', 
 (SELECT id FROM users WHERE username = 'underwriter'), 
 NOW(), NOW()),

('HS-20240103-0013', 
 (SELECT id FROM khach_hang WHERE makh = 'KH000013'), 
 (SELECT id FROM xe WHERE ma_xe = 'XE000014'), 
 (SELECT id FROM goi_bao_hiem WHERE ma_goi = 'GOI006'), 
 0, 'LOW', 'DANG_XU_LY', NULL, 'Đang thẩm định', 
 (SELECT id FROM users WHERE username = 'underwriter'), 
 NOW(), NOW()),

('HS-20240104-0014', 
 (SELECT id FROM khach_hang WHERE makh = 'KH000015'), 
 (SELECT id FROM xe WHERE ma_xe = 'XE000015'), 
 (SELECT id FROM goi_bao_hiem WHERE ma_goi = 'GOI004'), 
 0, 'LOW', 'CHO_DUYET', NULL, 'Chờ phê duyệt', 
 (SELECT id FROM users WHERE username = 'underwriter'), 
 NOW(), NOW()),

('HS-20240105-0015', 
 (SELECT id FROM khach_hang WHERE makh = 'KH000010'),
 (SELECT id FROM xe WHERE ma_xe = 'XE000011'), 
 (SELECT id FROM goi_bao_hiem WHERE ma_goi = 'GOI002'), 
 3, 'MEDIUM', 'HOAN_THANH', 6000000.00, 'Xe Mazda 3, tình trạng ổn', 
 (SELECT id FROM users WHERE username = 'underwriter'), 
 '2024-01-05 10:00:00', '2024-01-05 16:00:00');

SELECT 'Đã insert ' || COUNT(*) || ' hồ sơ thẩm định thành công!' as result FROM ho_so_tham_dinh;
