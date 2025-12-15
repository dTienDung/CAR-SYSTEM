-- ============================================
-- FILE: 02_insert_khach_hang.sql
-- MÔ TẢ: Insert dữ liệu cho bảng KHACH_HANG
-- ============================================

-- Xóa dữ liệu cũ (nếu có)
-- DELETE FROM khach_hang;

-- Insert dữ liệu Khách hàng
INSERT INTO khach_hang (makh, ho_ten, cccd, ngay_sinh, gioi_tinh, so_dien_thoai, email, dia_chi, nghe_nghiep, created_at, updated_at) VALUES
('KH000001', 'Nguyễn Văn An', '001234567890', '1985-03-15', 'Nam', '0987654321', 'an.nguyen@gmail.com', '123 Đường Láng, Đống Đa, Hà Nội', 'Kỹ sư phần mềm', NOW(), NOW()),
('KH000002', 'Trần Thị Bình', '001234567891', '1990-07-22', 'Nữ', '0987654322', 'binh.tran@gmail.com', '456 Nguyễn Trãi, Thanh Xuân, Hà Nội', 'Giáo viên', NOW(), NOW()),
('KH000003', 'Lê Hoàng Cường', '001234567892', '1988-11-08', 'Nam', '0987654323', 'cuong.le@gmail.com', '789 Giải Phóng, Hai Bà Trưng, Hà Nội', 'Kinh doanh', NOW(), NOW()),
('KH000004', 'Phạm Thị Dung', '001234567893', '1992-05-18', 'Nữ', '0987654324', 'dung.pham@gmail.com', '321 Láng Hạ, Ba Đình, Hà Nội', 'Y tá', NOW(), NOW()),
('KH000005', 'Hoàng Văn Em', '001234567894', '1987-09-25', 'Nam', '0987654325', 'em.hoang@gmail.com', '654 Cầu Giấy, Cầu Giấy, Hà Nội', 'Quản lý dự án', NOW(), NOW()),
('KH000006', 'Vũ Thị Phương', '001234567895', '1995-02-14', 'Nữ', '0987654326', 'phuong.vu@gmail.com', '987 Trường Chinh, Đống Đa, Hà Nội', 'Kế toán', NOW(), NOW()),
('KH000007', 'Đỗ Văn Giang', '001234567896', '1983-12-30', 'Nam', '0987654327', 'giang.do@gmail.com', '147 Khương Thượng, Đống Đa, Hà Nội', 'Bác sĩ', NOW(), NOW()),
('KH000008', 'Bùi Thị Hà', '001234567897', '1991-06-11', 'Nữ', '0987654328', 'ha.bui@gmail.com', '258 Tây Sơn, Đống Đa, Hà Nội', 'Nhân viên văn phòng', NOW(), NOW()),
('KH000009', 'Ngô Văn Ích', '001234567898', '1989-08-07', 'Nam', '0987654329', 'ich.ngo@gmail.com', '369 Xã Đàn, Đống Đa, Hà Nội', 'Lập trình viên', NOW(), NOW()),
('KH000010', 'Lý Thị Kim', '001234567899', '1993-04-20', 'Nữ', '0987654330', 'kim.ly@gmail.com', '741 Nguyễn Lương Bằng, Đống Đa, Hà Nội', 'Designer', NOW(), NOW()),
('KH000011', 'Trương Văn Long', '001234567900', '1986-10-12', 'Nam', '0987654331', 'long.truong@gmail.com', '852 Kim Mã, Ba Đình, Hà Nội', 'Kỹ sư xây dựng', NOW(), NOW()),
('KH000012', 'Phan Thị Minh', '001234567901', '1994-01-28', 'Nữ', '0987654332', 'minh.phan@gmail.com', '963 Hoàng Quốc Việt, Cầu Giấy, Hà Nội', 'Nhân viên ngân hàng', NOW(), NOW()),
('KH000013', 'Văn Văn Nam', '001234567902', '1990-05-16', 'Nam', '0987654333', 'nam.van@gmail.com', '147 Lê Duẩn, Hoàn Kiếm, Hà Nội', 'Luật sư', NOW(), NOW()),
('KH000014', 'Đinh Thị Oanh', '001234567903', '1988-09-03', 'Nữ', '0987654334', 'oanh.dinh@gmail.com', '258 Nguyễn Chí Thanh, Đống Đa, Hà Nội', 'Dược sĩ', NOW(), NOW()),
('KH000015', 'Mai Văn Phúc', '001234567904', '1985-12-22', 'Nam', '0987654335', 'phuc.mai@gmail.com', '369 Phạm Văn Đồng, Bắc Từ Liêm, Hà Nội', 'Giám đốc kinh doanh', NOW(), NOW()),
('KH000016', 'Quan Thị Quỳnh', '001234567905', '1992-03-09', 'Nữ', '0987654336', 'quynh.quan@gmail.com', '741 Minh Khai, Hai Bà Trưng, Hà Nội', 'Marketing Manager', NOW(), NOW()),
('KH000017', 'Chu Văn Rộng', '001234567906', '1987-07-18', 'Nam', '0987654337', 'rong.chu@gmail.com', '852 Đê La Thành, Đống Đa, Hà Nội', 'Kiến trúc sư', NOW(), NOW()),
('KH000018', 'Tạ Thị Sương', '001234567907', '1991-11-25', 'Nữ', '0987654338', 'suong.ta@gmail.com', '963 Thái Hà, Đống Đa, Hà Nội', 'Nhà thiết kế nội thất', NOW(), NOW()),
('KH000019', 'Lương Văn Tài', '001234567908', '1989-02-14', 'Nam', '0987654339', 'tai.luong@gmail.com', '147 Láng, Đống Đa, Hà Nội', 'Công chức', NOW(), NOW()),
('KH000020', 'Hồ Thị Uyên', '001234567909', '1993-06-08', 'Nữ', '0987654340', 'uyen.ho@gmail.com', '258 Chùa Bộc, Đống Đa, Hà Nội', 'Biên tập viên', NOW(), NOW()),
('KH000021', 'Lê Văn Vũ', '001234567910', '1986-08-30', 'Nam', '0987654341', 'vu.le2@gmail.com', '369 Tôn Đức Thắng, Đống Đa, Hà Nội', 'Chủ doanh nghiệp', NOW(), NOW()),
('KH000022', 'Đặng Thị Xuân', '001234567911', '1990-12-05', 'Nữ', '0987654342', 'xuan.dang@gmail.com', '741 Ô Chợ Dừa, Đống Đa, Hà Nội', 'Giáo viên tiểu học', NOW(), NOW()),
('KH000023', 'Cao Văn Yên', '001234567912', '1988-04-19', 'Nam', '0987654343', 'yen.cao@gmail.com', '852 Phố Huế, Hai Bà Trưng, Hà Nội', 'Nhân viên IT', NOW(), NOW()),
('KH000024', 'Từ Thị Ánh', '001234567913', '1995-01-07', 'Nữ', '0987654344', 'anh.tu@gmail.com', '963 Bà Triệu, Hai Bà Trưng, Hà Nội', 'Thư ký', NOW(), NOW()),
('KH000025', 'Nghiêm Văn Bảo', '001234567914', '1984-09-23', 'Nam', '0987654345', 'bao.nghiem@gmail.com', '147 Lý Thường Kiệt, Hoàn Kiếm, Hà Nội', 'Quản lý nhà hàng', NOW(), NOW()),
('KH000026', 'Tô Thị Châu', '001234567915', '1992-05-11', 'Nữ', '0987654346', 'chau.to@gmail.com', '258 Hàng Bài, Hoàn Kiếm, Hà Nội', 'Nhân viên bán hàng', NOW(), NOW()),
('KH000027', 'Lã Văn Đạt', '001234567916', '1987-10-27', 'Nam', '0987654347', 'dat.la@gmail.com', '369 Trần Hưng Đạo, Hoàn Kiếm, Hà Nội', 'Kỹ thuật viên điện', NOW(), NOW()),
('KH000028', 'Ứng Thị Ê', '001234567917', '1991-03-15', 'Nữ', '0987654348', 'e.ung@gmail.com', '741 Nguyễn Khang, Cầu Giấy, Hà Nội', 'Chuyên viên phân tích', NOW(), NOW()),
('KH000029', 'Hà Văn Phi', '001234567918', '1989-07-22', 'Nam', '0987654349', 'phi.ha@gmail.com', '852 Hoàng Cầu, Đống Đa, Hà Nội', 'Kỹ sư cơ khí', NOW(), NOW()),
('KH000030', 'Gia Thị Giang', '001234567919', '1994-11-08', 'Nữ', '0987654350', 'giang.gia@gmail.com', '963 Kim Liên, Đống Đa, Hà Nội', 'Nhân viên hành chính', NOW(), NOW());

SELECT 'Đã insert ' || COUNT(*) || ' khách hàng thành công!' as result FROM khach_hang;
