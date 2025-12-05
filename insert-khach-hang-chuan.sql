-- =====================================================
-- INSERT DỮ LIỆU KHÁCH HÀNG CHUẨN
-- Sử dụng DBeaver để chạy trực tiếp vào MySQL
-- Mã KH sẽ tự động sinh bởi hệ thống
-- =====================================================

-- Xóa dữ liệu cũ (nếu cần)
-- DELETE FROM khach_hang;

-- =====================================================
-- INSERT KHÁCH HÀNG (Không cần ma_kh - tự động sinh)
-- =====================================================

INSERT INTO khach_hang (
    ho_ten, 
    ngay_sinh, 
    gioi_tinh, 
    so_dien_thoai, 
    email, 
    dia_chi, 
    cccd, 
    nghe_nghiep, 
    created_at, 
    updated_at
) VALUES
-- Khách hàng 1
(
    'Nguyễn Văn An',
    '1985-03-15',
    'NAM',
    '0901234567',
    'nguyenvanan@gmail.com',
    '123 Lê Lợi, Quận 1, TP.HCM',
    '079085001234',
    'Văn phòng',
    NOW(),
    NOW()
),

-- Khách hàng 2
(
    'Trần Thị Bình',
    '1990-07-20',
    'NU',
    '0902345678',
    'tranthibinh@gmail.com',
    '456 Nguyễn Huệ, Quận 1, TP.HCM',
    '079090005678',
    'Giáo viên',
    NOW(),
    NOW()
),

-- Khách hàng 3
(
    'Lê Văn Cường',
    '1982-11-10',
    'NAM',
    '0903456789',
    'levancuong@gmail.com',
    '789 Trần Hưng Đạo, Quận 5, TP.HCM',
    '079082009012',
    'Kinh doanh',
    NOW(),
    NOW()
),

-- Khách hàng 4
(
    'Phạm Thị Dung',
    '1988-05-25',
    'NU',
    '0904567890',
    'phamthidung@gmail.com',
    '321 Hai Bà Trưng, Quận 3, TP.HCM',
    '079088003456',
    'Văn phòng',
    NOW(),
    NOW()
),

-- Khách hàng 5
(
    'Hoàng Văn Em',
    '1995-09-05',
    'NAM',
    '0905678901',
    'hoangvanem@gmail.com',
    '654 Võ Văn Tần, Quận 3, TP.HCM',
    '079095007890',
    'Lái xe',
    NOW(),
    NOW()
),

-- Khách hàng 6
(
    'Võ Thị Hoa',
    '1992-12-18',
    'NU',
    '0906789012',
    'vothihoa@gmail.com',
    '111 Điện Biên Phủ, Quận Bình Thạnh, TP.HCM',
    '079092008901',
    'Y tá',
    NOW(),
    NOW()
),

-- Khách hàng 7
(
    'Đặng Văn Giang',
    '1987-04-22',
    'NAM',
    '0907890123',
    'dangvangiang@gmail.com',
    '222 Cách Mạng Tháng 8, Quận 10, TP.HCM',
    '079087009012',
    'Kỹ sư',
    NOW(),
    NOW()
),

-- Khách hàng 8
(
    'Bùi Thị Hương',
    '1993-08-30',
    'NU',
    '0908901234',
    'buithihuong@gmail.com',
    '333 Lý Thường Kiệt, Quận 11, TP.HCM',
    '079093001234',
    'Kế toán',
    NOW(),
    NOW()
),

-- Khách hàng 9
(
    'Trương Văn Khoa',
    '1980-02-14',
    'NAM',
    '0909012345',
    'truongvankhoa@gmail.com',
    '444 Phan Văn Trị, Quận Gò Vấp, TP.HCM',
    '079080002345',
    'Bác sĩ',
    NOW(),
    NOW()
),

-- Khách hàng 10
(
    'Lý Thị Lan',
    '1991-06-08',
    'NU',
    '0910123456',
    'lythilan@gmail.com',
    '555 Quang Trung, Quận 12, TP.HCM',
    '079091003456',
    'Nhân viên ngân hàng',
    NOW(),
    NOW()
);

-- =====================================================
-- KIỂM TRA KẾT QUẢ
-- =====================================================

SELECT 
    '✅ Đã insert thành công!' AS status,
    COUNT(*) AS so_luong_khach_hang
FROM khach_hang;

SELECT 
    id,
    ma_kh,
    ho_ten,
    gioi_tinh,
    so_dien_thoai,
    email,
    nghe_nghiep
FROM khach_hang 
ORDER BY created_at DESC 
LIMIT 10;
