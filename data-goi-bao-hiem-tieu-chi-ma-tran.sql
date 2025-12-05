-- =====================================================
-- DỮ LIỆU CHI TIẾT: GÓI BẢO HIỂM, TIÊU CHÍ THẨM ĐỊNH, MA TRẬN TÍNH PHÍ
-- Hệ thống Bảo hiểm Xe - MySQL Database
-- =====================================================

SET FOREIGN_KEY_CHECKS = 0;
SET SQL_SAFE_UPDATES = 0;

-- =====================================================
-- 1. GÓI BẢO HIỂM (Insurance Packages)
-- =====================================================

-- Xóa dữ liệu cũ
DELETE FROM goi_bao_hiem;

-- Thêm dữ liệu mới
INSERT INTO goi_bao_hiem (id, ma_goi, ten_goi, phi_co_ban, mo_ta, active, created_at, updated_at) VALUES
-- Gói cơ bản
(1, 'GOI-BASIC-001', 'Gói Tiết kiệm', 5000000, 
'Bảo hiểm vật chất xe cơ bản
- Bồi thường thiệt hại vật chất do va chạm, lật đổ
- Giới hạn bồi thường: 80% giá trị xe
- Không bao gồm: Mất cắp, cháy nổ
- Phù hợp: Xe cũ, giá trị thấp', 
1, NOW(), NOW()),

(2, 'GOI-BASIC-002', 'Gói Cơ bản Plus', 6500000, 
'Bảo hiểm vật chất xe + Bảo hiểm trách nhiệm dân sự
- Bồi thường thiệt hại vật chất: 90% giá trị xe
- TNDS bắt buộc: 150 triệu/người
- Không bao gồm: Mất cắp toàn bộ
- Phù hợp: Xe 3-5 năm tuổi', 
1, NOW(), NOW()),

-- Gói tiêu chuẩn
(3, 'GOI-STANDARD-001', 'Gói Tiêu chuẩn', 8000000, 
'Bảo hiểm toàn diện cơ bản
- Bồi thường thiệt hại vật chất: 100% giá trị xe
- Bảo hiểm cháy nổ
- TNDS bắt buộc: 150 triệu/người
- Không khấu trừ: 5%
- Phù hợp: Xe mới 1-3 năm', 
1, NOW(), NOW()),

(4, 'GOI-STANDARD-002', 'Gói Tiêu chuẩn Plus', 10000000, 
'Bảo hiểm toàn diện nâng cao
- Bồi thường thiệt hại vật chất: 100% giá trị xe
- Bảo hiểm cháy nổ, mất cắp toàn bộ
- TNDS bắt buộc: 150 triệu/người
- TNDS tự nguyện: 50 triệu/người
- Không khấu trừ: 3%
- Phù hợp: Xe mới, giá trị trung bình', 
1, NOW(), NOW()),

-- Gói cao cấp
(5, 'GOI-PREMIUM-001', 'Gói Cao cấp', 12000000, 
'Bảo hiểm toàn diện cao cấp
- Bồi thường thiệt hại vật chất: 100% giá trị xe
- Bảo hiểm cháy nổ, mất cắp toàn bộ
- TNDS bắt buộc: 150 triệu/người
- TNDS tự nguyện: 100 triệu/người
- Bảo hiểm người ngồi trên xe: 20 triệu/người
- Không khấu trừ: 0%
- Phù hợp: Xe mới, giá trị cao', 
1, NOW(), NOW()),

(6, 'GOI-PREMIUM-002', 'Gói VIP', 15000000, 
'Bảo hiểm toàn diện VIP
- Bồi thường thiệt hại vật chất: 120% giá trị xe
- Bảo hiểm cháy nổ, mất cắp toàn bộ/từng phần
- TNDS bắt buộc: 150 triệu/người
- TNDS tự nguyện: 200 triệu/người
- Bảo hiểm người ngồi trên xe: 50 triệu/người
- Hỗ trợ cứu hộ 24/7 miễn phí
- Xe thay thế khi sửa chữa
- Không khấu trừ: 0%
- Phù hợp: Xe sang, xe mới, giá trị rất cao', 
1, NOW(), NOW()),

-- Gói đặc biệt
(7, 'GOI-SPECIAL-001', 'Gói Taxi/Grab', 9000000, 
'Bảo hiểm chuyên dụng cho xe kinh doanh vận tải
- Bồi thường thiệt hại vật chất: 100% giá trị xe
- Bảo hiểm cháy nổ
- TNDS bắt buộc: 150 triệu/người
- TNDS tự nguyện: 100 triệu/người
- Bảo hiểm hành khách: 30 triệu/người
- Không khấu trừ: 5%
- Phù hợp: Taxi, Grab, xe công nghệ', 
1, NOW(), NOW()),

(8, 'GOI-SPECIAL-002', 'Gói Doanh nghiệp', 11000000, 
'Bảo hiểm cho xe công ty, doanh nghiệp
- Bồi thường thiệt hại vật chất: 100% giá trị xe
- Bảo hiểm cháy nổ, mất cắp
- TNDS bắt buộc: 150 triệu/người
- TNDS tự nguyện: 150 triệu/người
- Bảo hiểm người ngồi trên xe: 30 triệu/người
- Hỗ trợ pháp lý
- Không khấu trừ: 2%
- Phù hợp: Xe công ty, xe doanh nghiệp', 
1, NOW(), NOW());

-- =====================================================
-- 2. TIÊU CHÍ THẨM ĐỊNH (Appraisal Criteria)
-- =====================================================

-- Xóa dữ liệu cũ
DELETE FROM tieu_chi_tham_dinh;

-- Thêm dữ liệu mới
INSERT INTO tieu_chi_tham_dinh (id, ma_tieu_chi, ten_tieu_chi, diem_toi_da, mo_ta, dieu_kien, thu_tu, active, created_at, updated_at) VALUES
-- Nhóm 1: Thông tin xe
(1, 'TC-XE-001', 'Tuổi xe', 10, 
'Đánh giá độ tuổi của xe so với năm sản xuất
- Xe mới (0-2 năm): 0-2 điểm
- Xe khá mới (3-5 năm): 3-4 điểm
- Xe trung bình (6-10 năm): 5-7 điểm
- Xe cũ (11-15 năm): 8-9 điểm
- Xe rất cũ (>15 năm): 10 điểm', 
'Tính từ năm sản xuất đến hiện tại', 
1, 1, NOW(), NOW()),

(2, 'TC-XE-002', 'Giá trị xe', 8, 
'Đánh giá theo trị giá thị trường của xe
- Xe giá rẻ (<300 triệu): 0-2 điểm
- Xe giá trung bình (300-600 triệu): 3-4 điểm
- Xe giá khá cao (600-1 tỷ): 5-6 điểm
- Xe giá cao (1-2 tỷ): 7 điểm
- Xe giá rất cao (>2 tỷ): 8 điểm', 
'Giá trị xe càng cao, rủi ro bồi thường càng lớn', 
2, 1, NOW(), NOW()),

(3, 'TC-XE-003', 'Hãng xe và độ tin cậy', 5, 
'Đánh giá theo thương hiệu và độ tin cậy
- Hãng cao cấp (Mercedes, BMW, Audi): 4-5 điểm
- Hãng phổ thông (Toyota, Honda, Mazda): 2-3 điểm
- Hãng bình dân (Kia, Hyundai, VinFast): 1-2 điểm
- Hãng Trung Quốc: 0-1 điểm', 
'Xe cao cấp có chi phí sửa chữa, phụ tùng đắt', 
3, 1, NOW(), NOW()),

(4, 'TC-XE-004', 'Tình trạng kỹ thuật', 7, 
'Đánh giá tình trạng hiện tại của xe
- Xe mới, nguyên bản: 0-1 điểm
- Xe tốt, bảo dưỡng định kỳ: 2-3 điểm
- Xe trung bình, có sửa chữa nhỏ: 4-5 điểm
- Xe kém, nhiều hư hỏng: 6-7 điểm', 
'Dựa trên báo cáo kiểm định kỹ thuật', 
4, 1, NOW(), NOW()),

-- Nhóm 2: Lịch sử sử dụng
(5, 'TC-LICHSU-001', 'Lịch sử tai nạn', 12, 
'Đánh giá số lần tai nạn trong quá khứ
- Không có tai nạn: 0 điểm
- 1 lần tai nạn nhỏ: 4 điểm
- 2 lần tai nạn: 8 điểm
- 3 lần trở lên: 12 điểm', 
'Mỗi lần tai nạn: +4 điểm. Tai nạn lớn: +6 điểm', 
5, 1, NOW(), NOW()),

(6, 'TC-LICHSU-002', 'Số km đã đi', 6, 
'Đánh giá quãng đường xe đã di chuyển
- <30,000 km: 0-1 điểm
- 30,000-60,000 km: 2-3 điểm
- 60,000-100,000 km: 4-5 điểm
- >100,000 km: 6 điểm', 
'Km càng cao, độ hao mòn càng lớn', 
6, 1, NOW(), NOW()),

(7, 'TC-LICHSU-003', 'Lịch sử bảo dưỡng', 4, 
'Đánh giá việc bảo dưỡng định kỳ
- Bảo dưỡng đầy đủ, đúng hạn: 0 điểm
- Bảo dưỡng không đều: 2 điểm
- Ít bảo dưỡng: 3 điểm
- Không có lịch sử bảo dưỡng: 4 điểm', 
'Xe bảo dưỡng tốt giảm rủi ro hư hỏng', 
7, 1, NOW(), NOW()),

-- Nhóm 3: Thông tin chủ xe
(8, 'TC-CHUXE-001', 'Độ tuổi lái xe', 6, 
'Đánh giá độ tuổi của người lái chính
- 30-50 tuổi: 0-1 điểm
- 25-29 tuổi hoặc 51-60 tuổi: 2-3 điểm
- 18-24 tuổi hoặc >60 tuổi: 4-6 điểm', 
'Lái xe trẻ hoặc cao tuổi có rủi ro cao hơn', 
8, 1, NOW(), NOW()),

(9, 'TC-CHUXE-002', 'Kinh nghiệm lái xe', 5, 
'Đánh giá số năm kinh nghiệm lái xe
- >10 năm: 0 điểm
- 5-10 năm: 1-2 điểm
- 2-5 năm: 3-4 điểm
- <2 năm: 5 điểm', 
'Kinh nghiệm càng nhiều, rủi ro càng thấp', 
9, 1, NOW(), NOW()),

(10, 'TC-CHUXE-003', 'Nghề nghiệp', 4, 
'Đánh giá theo nghề nghiệp của chủ xe
- Văn phòng, giáo viên: 0-1 điểm
- Kinh doanh, tự do: 2 điểm
- Lái xe chuyên nghiệp: 3 điểm
- Nghề nguy hiểm: 4 điểm', 
'Nghề nghiệp ảnh hưởng đến tần suất sử dụng xe', 
10, 1, NOW(), NOW()),

-- Nhóm 4: Mục đích sử dụng
(11, 'TC-MUCDICH-001', 'Mục đích sử dụng xe', 8, 
'Đánh giá theo mục đích sử dụng
- Cá nhân, gia đình: 0-2 điểm
- Đi làm hàng ngày: 3-4 điểm
- Kinh doanh vận tải (Grab, taxi): 6-7 điểm
- Cho thuê, dịch vụ: 8 điểm', 
'Sử dụng thương mại có rủi ro cao hơn', 
11, 1, NOW(), NOW()),

(12, 'TC-MUCDICH-002', 'Khu vực hoạt động', 5, 
'Đánh giá khu vực xe thường xuyên hoạt động
- Nội thành, đường tốt: 0-1 điểm
- Ngoại thành: 2-3 điểm
- Vùng xa, đường xấu: 4-5 điểm', 
'Khu vực nguy hiểm tăng rủi ro tai nạn', 
12, 1, NOW(), NOW()),

-- Nhóm 5: Yếu tố khác
(13, 'TC-KHAC-001', 'Lịch sử bồi thường', 10, 
'Đánh giá lịch sử yêu cầu bồi thường trước đây
- Chưa từng yêu cầu: 0 điểm
- 1 lần yêu cầu: 3 điểm
- 2 lần yêu cầu: 6 điểm
- 3 lần trở lên: 10 điểm', 
'Lịch sử bồi thường nhiều = rủi ro cao', 
13, 1, NOW(), NOW()),

(14, 'TC-KHAC-002', 'Điều kiện bảo quản', 4, 
'Đánh giá nơi để xe
- Garage riêng, an toàn: 0 điểm
- Bãi đỗ có bảo vệ: 1 điểm
- Đỗ ngoài đường có camera: 2-3 điểm
- Đỗ ngoài đường không giám sát: 4 điểm', 
'Bảo quản tốt giảm rủi ro mất cắp, hư hỏng', 
14, 1, NOW(), NOW()),

(15, 'TC-KHAC-003', 'Tín dụng và thanh toán', 3, 
'Đánh giá lịch sử tín dụng của khách hàng
- Tín dụng tốt, thanh toán đúng hạn: 0 điểm
- Tín dụng trung bình: 1-2 điểm
- Tín dụng kém, nợ quá hạn: 3 điểm', 
'Khách hàng uy tín giảm rủi ro gian lận', 
15, 1, NOW(), NOW());

-- =====================================================
-- 3. MA TRẬN TÍNH PHÍ (Fee Calculation Matrix)
-- =====================================================

-- Xóa dữ liệu cũ
DELETE FROM ma_tran_tinh_phi;

-- Thêm dữ liệu mới
INSERT INTO ma_tran_tinh_phi (id, diem_rui_ro_tu, diem_rui_ro_den, he_so_phi, mo_ta, active, created_at, updated_at) VALUES
-- Cấp độ 1: Rủi ro rất thấp
(1, 0, 5, 0.8, 
'Rủi ro rất thấp - Khách hàng ưu tiên
- Xe mới, chủ xe có kinh nghiệm
- Không có lịch sử tai nạn
- Bảo dưỡng tốt
→ Giảm 20% phí cơ bản', 
1, NOW(), NOW()),

-- Cấp độ 2: Rủi ro thấp
(2, 6, 10, 1.0, 
'Rủi ro thấp - Khách hàng tốt
- Xe còn mới (1-3 năm)
- Lịch sử sử dụng tốt
- Ít hoặc không có tai nạn
→ Phí cơ bản chuẩn', 
1, NOW(), NOW()),

-- Cấp độ 3: Rủi ro trung bình thấp
(3, 11, 14, 1.2, 
'Rủi ro trung bình thấp
- Xe 3-5 năm tuổi
- Có thể có 1 tai nạn nhỏ
- Điều kiện sử dụng bình thường
→ Tăng 20% phí cơ bản', 
1, NOW(), NOW()),

-- Cấp độ 4: Rủi ro trung bình
(4, 15, 20, 1.5, 
'Rủi ro trung bình - Cần xem xét
- Xe 5-8 năm tuổi
- Có lịch sử tai nạn (1-2 lần)
- Sử dụng thương mại hoặc khu vực nguy hiểm
→ Tăng 50% phí cơ bản', 
1, NOW(), NOW()),

-- Cấp độ 5: Rủi ro trung bình cao
(5, 21, 24, 1.8, 
'Rủi ro trung bình cao - Xem xét kỹ
- Xe cũ (8-12 năm)
- Nhiều lịch sử tai nạn (2-3 lần)
- Lái xe ít kinh nghiệm hoặc cao tuổi
→ Tăng 80% phí cơ bản', 
1, NOW(), NOW()),

-- Cấp độ 6: Rủi ro cao
(6, 25, 30, 2.2, 
'Rủi ro cao - Cân nhắc từ chối
- Xe rất cũ (>12 năm)
- Lịch sử tai nạn nhiều (>3 lần)
- Điều kiện sử dụng xấu
→ Tăng 120% phí cơ bản', 
1, NOW(), NOW()),

-- Cấp độ 7: Rủi ro rất cao
(7, 31, 40, 2.5, 
'Rủi ro rất cao - Nên từ chối
- Xe quá cũ (>15 năm)
- Lịch sử tai nạn rất nhiều
- Nhiều yếu tố rủi ro kết hợp
→ Tăng 150% phí cơ bản hoặc từ chối', 
1, NOW(), NOW()),

-- Cấp độ 8: Rủi ro cực cao
(8, 41, 50, 3.0, 
'Rủi ro cực cao - Từ chối bảo hiểm
- Tổng hợp nhiều yếu tố rủi ro nghiêm trọng
- Không đủ điều kiện bảo hiểm
→ Từ chối hoặc yêu cầu điều kiện đặc biệt', 
1, NOW(), NOW()),

-- Cấp độ đặc biệt: Khách hàng VIP
(9, -10, -1, 0.7, 
'Khách hàng VIP - Ưu đãi đặc biệt
- Khách hàng lâu năm, uy tín
- Không có lịch sử yêu cầu bồi thường
- Mua nhiều gói bảo hiểm
→ Giảm 30% phí cơ bản', 
1, NOW(), NOW());

-- =====================================================
-- THỐNG KÊ DỮ LIỆU
-- =====================================================

SELECT 
    'Gói bảo hiểm' AS loai_du_lieu,
    COUNT(*) AS so_luong,
    MIN(phi_co_ban) AS phi_thap_nhat,
    MAX(phi_co_ban) AS phi_cao_nhat,
    AVG(phi_co_ban) AS phi_trung_binh
FROM goi_bao_hiem
WHERE active = 1

UNION ALL

SELECT 
    'Tiêu chí thẩm định' AS loai_du_lieu,
    COUNT(*) AS so_luong,
    MIN(diem_toi_da) AS diem_thap_nhat,
    MAX(diem_toi_da) AS diem_cao_nhat,
    AVG(diem_toi_da) AS diem_trung_binh
FROM tieu_chi_tham_dinh
WHERE active = 1

UNION ALL

SELECT 
    'Ma trận tính phí' AS loai_du_lieu,
    COUNT(*) AS so_luong,
    MIN(he_so_phi) AS he_so_thap_nhat,
    MAX(he_so_phi) AS he_so_cao_nhat,
    AVG(he_so_phi) AS he_so_trung_binh
FROM ma_tran_tinh_phi
WHERE active = 1;

-- =====================================================
-- HOÀN TẤT!
-- =====================================================

SET FOREIGN_KEY_CHECKS = 1;
SET SQL_SAFE_UPDATES = 1;

SELECT '✅ Dữ liệu đã được tải thành công!' AS status,
       '📦 8 Gói bảo hiểm' AS goi_bao_hiem,
       '📋 15 Tiêu chí thẩm định' AS tieu_chi,
       '📊 9 Cấp độ ma trận tính phí' AS ma_tran;
