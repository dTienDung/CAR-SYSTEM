-- ============================================
-- FILE: 04_insert_goi_bao_hiem.sql
-- MÔ TẢ: Insert dữ liệu cho bảng GOI_BAO_HIEM
-- ============================================

-- Xóa dữ liệu cũ (nếu có)
-- DELETE FROM goi_bao_hiem;

-- Insert dữ liệu Gói bảo hiểm
INSERT INTO goi_bao_hiem (ma_goi, ten_goi, mo_ta, phi_co_ban, quyen_loi, active, created_at, updated_at) VALUES
('GOI001', 'Gói Bảo Hiểm Cơ Bản', 'Gói bảo hiểm xe ô tô cơ bản với mức phí hợp lý', 3000000.00, 
'- Bảo hiểm trách nhiệm dân sự bắt buộc
- Bồi thường thiệt hại về người lên đến 150 triệu đồng
- Bồi thường thiệt hại về tài sản lên đến 100 triệu đồng
- Hỗ trợ pháp lý khi có tranh chấp', 
true, NOW(), NOW()),

('GOI002', 'Gói Bảo Hiểm Tiêu Chuẩn', 'Gói bảo hiểm mở rộng với nhiều quyền lợi hơn', 5000000.00, 
'- Tất cả quyền lợi của gói Cơ Bản
- Bảo hiểm vật chất xe (mất cắp, hỏa hoạn, thiên tai)
- Bồi thường thiệt hại về người lên đến 200 triệu đồng
- Bồi thường thiệt hại về tài sản lên đến 150 triệu đồng
- Hỗ trợ sửa chữa tại garage ủy quyền
- Hỗ trợ xe cứu hộ 24/7', 
true, NOW(), NOW()),

('GOI003', 'Gói Bảo Hiểm Toàn Diện', 'Gói bảo hiểm cao cấp bảo vệ toàn diện nhất', 8000000.00, 
'- Tất cả quyền lợi của gói Tiêu Chuẩn
- Bảo hiểm thân vỏ xe (va chạm, đâm va, lật xe)
- Bồi thường thiệt hại về người lên đến 300 triệu đồng
- Bồi thường thiệt hại về tài sản lên đến 200 triệu đồng
- Bảo hiểm người ngồi trên xe (tối đa 50 triệu/người)
- Xe thay thế trong thời gian sửa chữa
- Bảo hiểm phụ tùng, phụ kiện
- Miễn thẩm định với giá trị dưới 5 triệu đồng', 
true, NOW(), NOW()),

('GOI004', 'Gói Bảo Hiểm VIP', 'Gói bảo hiểm cao cấp dành cho xe sang', 15000000.00, 
'- Tất cả quyền lợi của gói Toàn Diện
- Bồi thường thiệt hại về người lên đến 500 triệu đồng
- Bồi thường thiệt hại về tài sản lên đến 300 triệu đồng
- Bảo hiểm người ngồi trên xe (tối đa 100 triệu/người)
- Xe thay thế cùng phân khúc
- Bảo hiểm phụ kiện cao cấp (âm thanh, nội thất...)
- Hỗ trợ pháp lý cao cấp
- Miễn thẩm định với giá trị dưới 20 triệu đồng
- Chăm sóc khách hàng VIP 24/7', 
true, NOW(), NOW()),

('GOI005', 'Gói Bảo Hiểm Kinh Doanh', 'Gói bảo hiểm dành cho xe kinh doanh, vận tải', 6000000.00, 
'- Bảo hiểm trách nhiệm dân sự bắt buộc
- Bồi thường thiệt hại về người lên đến 250 triệu đồng
- Bồi thường thiệt hại về tài sản lên đến 180 triệu đồng
- Bảo hiểm vật chất xe
- Bảo hiểm mất khả năng hoạt động kinh doanh
- Hỗ trợ sửa chữa nhanh
- Xe cứu hộ 24/7', 
true, NOW(), NOW()),

('GOI006', 'Gói Bảo Hiểm Linh Hoạt', 'Gói bảo hiểm có thể tùy chỉnh theo nhu cầu', 4000000.00, 
'- Quyền lợi cơ bản
- Khách hàng có thể chọn thêm các quyền lợi bổ sung
- Bồi thường thiệt hại về người lên đến 180 triệu đồng
- Bồi thường thiệt hại về tài sản lên đến 120 triệu đồng
- Tùy chọn bảo hiểm người ngồi trên xe
- Tùy chọn xe thay thế
- Tùy chọn bảo hiểm phụ tùng', 
true, NOW(), NOW());

SELECT 'Đã insert ' || COUNT(*) || ' gói bảo hiểm thành công!' as result FROM goi_bao_hiem;
