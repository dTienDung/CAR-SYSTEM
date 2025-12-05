-- =====================================================
-- INSERT DỮ LIỆU XE MẪU
-- 8 xe cho 8 khách hàng đầu tiên
-- Sử dụng DBeaver để chạy trực tiếp vào MySQL
-- =====================================================

-- =====================================================
-- LƯU Ý: Chạy sau khi đã insert khách hàng
-- =====================================================

-- Kiểm tra khách hàng đã tồn tại
SELECT 
    '📋 Danh sách khách hàng hiện có:' AS thong_bao,
    '' AS gia_tri;

SELECT id, ma_kh, ho_ten FROM khach_hang ORDER BY id LIMIT 8;

-- =====================================================
-- INSERT XE (Tự động sinh ma_xe)
-- =====================================================

SET @row_number = 0;
SET @current_date = DATE_FORMAT(NOW(), '%Y%m%d');

INSERT INTO xe (
    ma_xe,
    bien_so,
    so_khung,
    so_may,
    hang_xe,
    dong_xe,
    nam_san_xuat,
    nam_dang_ky,
    mau_sac,
    muc_dich_su_dung,
    gia_tri_xe,
    thong_tin_ky_thuat,
    khach_hang_id,
    created_at,
    updated_at
)
SELECT 
    CONCAT('XE-', @current_date, '-', LPAD(@row_number := @row_number + 1, 4, '0')) AS ma_xe,
    bien_so,
    so_khung,
    so_may,
    hang_xe,
    dong_xe,
    nam_san_xuat,
    nam_dang_ky,
    mau_sac,
    muc_dich_su_dung,
    gia_tri_xe,
    thong_tin_ky_thuat,
    khach_hang_id,
    created_at,
    updated_at
FROM (
    -- Xe 1: Toyota Vios 2020 - Khách hàng 1 (Nguyễn Văn An)
    SELECT 
        '51A-12345' AS bien_so,
        'VNKKM8DB5LA123456' AS so_khung,
        '2NR1234567' AS so_may,
        'Toyota' AS hang_xe,
        'Vios 1.5E CVT' AS dong_xe,
        2020 AS nam_san_xuat,
        2020 AS nam_dang_ky,
        'Trắng' AS mau_sac,
        'Cá nhân' AS muc_dich_su_dung,
        450000000 AS gia_tri_xe,
        'Động cơ 1.5L, Hộp số CVT, 4 chỗ ngồi' AS thong_tin_ky_thuat,
        1 AS khach_hang_id,
        NOW() AS created_at,
        NOW() AS updated_at
    
    UNION ALL
    -- Xe 2: Honda City 2019 - Khách hàng 2 (Trần Thị Bình)
    SELECT 
        '51B-23456',
        'VNHGM8DB4KA234567',
        'L15Z1234567',
        'Honda',
        'City 1.5 TOP',
        2019,
        2019,
        'Đỏ',
        'Cá nhân',
        420000000,
        'Động cơ 1.5L VTEC, Hộp số CVT, 5 chỗ ngồi',
        2,
        NOW(),
        NOW()
    
    UNION ALL
    -- Xe 3: Mazda 3 2018 - Khách hàng 3 (Lê Văn Cường)
    SELECT 
        '51C-34567',
        'VNMAM8DB3JA345678',
        'PE1234567',
        'Mazda',
        'Mazda 3 1.5 Luxury',
        2018,
        2018,
        'Xanh',
        'Kinh doanh',
        550000000,
        'Động cơ 1.5L Skyactiv-G, Hộp số tự động 6 cấp',
        3,
        NOW(),
        NOW()
    
    UNION ALL
    -- Xe 4: Hyundai Accent 2021 - Khách hàng 4 (Phạm Thị Dung)
    SELECT 
        '51D-45678',
        'VNHHM8DB5MA456789',
        'G4LC1234567',
        'Hyundai',
        'Accent 1.4 AT Đặc biệt',
        2021,
        2021,
        'Bạc',
        'Cá nhân',
        480000000,
        'Động cơ 1.4L Kappa, Hộp số tự động 6 cấp',
        4,
        NOW(),
        NOW()
    
    UNION ALL
    -- Xe 5: Ford Ranger 2017 - Khách hàng 5 (Hoàng Văn Em - Lái xe)
    SELECT 
        '51E-56789',
        'VNFPM8DB2HA567890',
        'PUMA1234567',
        'Ford',
        'Ranger XLS 2.2L 4x2 MT',
        2017,
        2017,
        'Đen',
        'Kinh doanh vận tải',
        580000000,
        'Động cơ Diesel 2.2L, Hộp số sàn 6 cấp, Bán tải',
        5,
        NOW(),
        NOW()
    
    UNION ALL
    -- Xe 6: Kia Morning 2022 - Khách hàng 6 (Võ Thị Hoa)
    SELECT 
        '51F-67890',
        'VNKKM8DB5NA678901',
        'G3LA1234567',
        'Kia',
        'Morning X-Line 1.25 AT',
        2022,
        2022,
        'Vàng',
        'Cá nhân',
        380000000,
        'Động cơ 1.25L Kappa, Hộp số tự động 4 cấp',
        6,
        NOW(),
        NOW()
    
    UNION ALL
    -- Xe 7: Mercedes C200 2016 - Khách hàng 7 (Đặng Văn Giang - Kỹ sư)
    SELECT 
        '51G-78901',
        'VNMBM8DB4GA789012',
        'M274DE20AL123456',
        'Mercedes-Benz',
        'C200 Exclusive',
        2016,
        2016,
        'Đen',
        'Cá nhân',
        950000000,
        'Động cơ 2.0L Turbo, Hộp số tự động 7 cấp, Sedan hạng sang',
        7,
        NOW(),
        NOW()
    
    UNION ALL
    -- Xe 8: Vinfast Lux A2.0 2020 - Khách hàng 8 (Bùi Thị Hương)
    SELECT 
        '51H-89012',
        'VNVFM8DB5LA890123',
        'VF8901234567',
        'VinFast',
        'Lux A2.0 Premium',
        2020,
        2020,
        'Trắng ngọc trai',
        'Cá nhân',
        880000000,
        'Động cơ 2.0L Turbo, Hộp số tự động 8 cấp, Sedan hạng D',
        8,
        NOW(),
        NOW()
) AS temp_data;

-- =====================================================
-- KIỂM TRA KẾT QUẢ
-- =====================================================

SELECT 
    '✅ Đã insert thành công!' AS status,
    COUNT(*) AS so_luong_xe
FROM xe;

SELECT 
    x.id,
    x.ma_xe,
    x.bien_so,
    x.hang_xe,
    x.dong_xe,
    x.nam_san_xuat,
    FORMAT(x.gia_tri_xe, 0) AS gia_tri_xe,
    k.ho_ten AS chu_xe
FROM xe x
JOIN khach_hang k ON x.khach_hang_id = k.id
ORDER BY x.created_at DESC
LIMIT 10;

-- =====================================================
-- THỐNG KÊ THEO HÃNG XE
-- =====================================================

SELECT 
    '📊 Thống kê theo hãng xe:' AS thong_ke,
    '' AS gia_tri;

SELECT 
    hang_xe,
    COUNT(*) AS so_luong,
    MIN(nam_san_xuat) AS nam_cu_nhat,
    MAX(nam_san_xuat) AS nam_moi_nhat,
    FORMAT(AVG(gia_tri_xe), 0) AS gia_tri_trung_binh
FROM xe
GROUP BY hang_xe
ORDER BY so_luong DESC;
