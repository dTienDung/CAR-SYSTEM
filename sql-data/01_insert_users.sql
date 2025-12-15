-- ============================================
-- FILE: 01_insert_users.sql
-- MÔ TẢ: Insert dữ liệu cho bảng USERS
-- ============================================

-- Xóa dữ liệu cũ (nếu có)
-- DELETE FROM users;

-- Insert dữ liệu Users (chỉ 4 tài khoản theo Role enum)
INSERT INTO users (username, password, ho_ten, email, so_dien_thoai, role, active, created_at, updated_at, ghi_chu) VALUES
-- ADMIN
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Nguyễn Văn Admin', 'admin@carsystem.com', '0901234567', 'ADMIN', true, NOW(), NOW(), 'Quản trị viên hệ thống - Quyền cao nhất'),

-- UNDERWRITER (Nhân viên thẩm định)
('underwriter', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Hoàng Văn Thẩm Định', 'underwriter@carsystem.com', '0901234571', 'UNDERWRITER', true, NOW(), NOW(), 'Chuyên viên thẩm định rủi ro'),

-- SALES (Nhân viên kinh doanh)
('sales', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Lê Văn Kinh Doanh', 'sales@carsystem.com', '0901234569', 'SALES', true, NOW(), NOW(), 'Nhân viên tư vấn bảo hiểm và quản lý khách hàng'),

-- ACCOUNTANT (Kế toán)
('accountant', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Phạm Thị Kế Toán', 'accountant@carsystem.com', '0901234570', 'ACCOUNTANT', true, NOW(), NOW(), 'Nhân viên kế toán - Quản lý thanh toán');

-- Password mặc định cho tất cả tài khoản: password123

SELECT 'Đã insert ' || COUNT(*) || ' users thành công!' as result FROM users;
