# 📋 CÁC CHỨC NĂNG QUẢN LÝ HỢP ĐỒNG BẢO HIỂM XE CƠ GIỚI

> **Phân tích hệ thống hiện tại** - Ngày: 05/12/2025

---

## 🎯 TỔNG QUAN

Hệ thống **Quản lý Hợp đồng Bảo hiểm Xe cơ giới** là module đặc thù của ngành bảo hiểm, khác biệt với quản lý hợp đồng chung vì:
- Đối tượng phức tạp: Khách hàng + Xe + Rủi ro + Phạm vi bảo hiểm
- Có thuật toán tính phí theo biểu phí, ma trận rủi ro
- Có quy trình thẩm định (underwriting) riêng
- Vòng đời đặc biệt: Phát hành → Hiệu lực → Tái tục → Hết hạn
- Tích hợp với quy trình bồi thường
- Ràng buộc pháp lý chuyên ngành (Luật Kinh doanh Bảo hiểm 2022, TT 04/2021/TT-BTC)

---

## 📊 CÁC CHỨC NĂNG ĐÃ CÓ TRONG HỆ THỐNG

### **1. QUẢN LÝ VÒNG ĐỜI HỢP ĐỒNG**

#### **1.1. Tạo Hợp đồng Mới**
- **API:** `POST /api/hop-dong`
- **Mô tả:** Tạo hợp đồng bảo hiểm từ hồ sơ thẩm định đã được chấp nhận
- **Input:**
  - `hoSoThamDinhId`: ID hồ sơ thẩm định (bắt buộc)
  - `ngayKy`: Ngày ký hợp đồng
  - `ngayHieuLuc`: Ngày bắt đầu hiệu lực
  - `ngayHetHan`: Ngày hết hạn (tự động = ngayHieuLuc + 1 năm nếu không nhập)
  - `ghiChu`: Ghi chú
- **Business Logic:**
  - ✅ Validate hồ sơ thẩm định phải ở trạng thái CHẤP NHẬN
  - ✅ Tự động sinh mã hợp đồng (format: HD-YYYYMMDD-XXXX)
  - ✅ Tự động tính phí bảo hiểm = phiCoBan × heSoPhi (từ ma trận tính phí)
  - ✅ Tự động lấy thông tin: Khách hàng, Xe, Gói bảo hiểm từ hồ sơ
  - ✅ Trạng thái ban đầu: DRAFT
  - ✅ Tracking: createdAt, updatedAt, nguoiTao
- **Output:** Hợp đồng mới được tạo

---

#### **1.2. Cập nhật Hợp đồng**
- **API:** `PUT /api/hop-dong/{id}`
- **Mô tả:** Chỉnh sửa thông tin hợp đồng
- **Input:**
  - `ngayKy`: Ngày ký (optional)
  - `ngayHieuLuc`: Ngày hiệu lực (optional)
  - `ngayHetHan`: Ngày hết hạn (optional)
  - `ghiChu`: Ghi chú (optional)
- **Vấn đề hiện tại:**
  - ⚠️ Không có validation theo trạng thái (có thể sửa bất kỳ lúc nào)
  - ⚠️ Không có audit log
  - ⚠️ Không kiểm soát ai được phép sửa gì
- **Output:** Hợp đồng đã cập nhật

---

#### **1.3. Xem Chi tiết Hợp đồng**
- **API:** `GET /api/hop-dong/{id}`
- **Mô tả:** Lấy thông tin chi tiết 1 hợp đồng
- **Output:** 
  - Thông tin hợp đồng đầy đủ
  - Thông tin khách hàng (lazy load)
  - Thông tin xe (lazy load)
  - Thông tin gói bảo hiểm (lazy load)
  - Danh sách thanh toán
  - Hợp đồng cũ (nếu là tái tục)

---

#### **1.4. Danh sách & Lọc Hợp đồng**
- **API:** `GET /api/hop-dong`
- **Mô tả:** Lấy danh sách hợp đồng với bộ lọc
- **Filters:**
  - `trangThai`: Lọc theo trạng thái (DRAFT, PENDING_PAYMENT, ACTIVE, EXPIRED, CANCELLED, TERMINATED, RENEWED)
  - `khachHangId`: Lọc theo khách hàng
  - `fromDate`: Từ ngày
  - `toDate`: Đến ngày
- **Output:** Danh sách hợp đồng

---

#### **1.5. Xóa Hợp đồng**
- **API:** `DELETE /api/hop-dong/{id}`
- **Mô tả:** Xóa hợp đồng khỏi hệ thống
- **Vấn đề:**
  - ⚠️ Không có soft delete
  - ⚠️ Không kiểm tra trạng thái trước khi xóa
  - ⚠️ Có thể xóa hợp đồng đang ACTIVE (nguy hiểm)

---

### **2. QUẢN LÝ TÁI TỤC HỢP ĐỒNG**

#### **2.1. Tái tục Hợp đồng**
- **API:** `POST /api/hop-dong/{id}/renew`
- **Mô tả:** Tạo hợp đồng mới dựa trên hợp đồng cũ (renewal)
- **Input:**
  - `ngayKy`: Ngày ký hợp đồng mới
  - `ngayHieuLuc`: Ngày hiệu lực hợp đồng mới
  - `ngayHetHan`: Ngày hết hạn (tự động = ngayHieuLuc + 1 năm)
  - `ghiChu`: Ghi chú
- **Business Logic:**
  - ✅ Validate: Chỉ tái tục được hợp đồng ACTIVE hoặc EXPIRED
  - ✅ Validate: ngayKy mới >= ngayHetHan cũ
  - ✅ Validate: ngayHieuLuc >= ngayKy
  - ✅ Validate: Thời hạn tối thiểu 30 ngày
  - ✅ Copy toàn bộ thông tin từ hợp đồng cũ (KH, Xe, Gói, Phí)
  - ✅ Tự động sinh mã hợp đồng mới
  - ✅ Link hợp đồng: hopDongMoi.hopDongCu = hopDongCu
  - ✅ Đánh dấu loại quan hệ: TAI_TUC
  - ✅ Chuyển trạng thái hợp đồng cũ → RENEWED
  - ✅ Hợp đồng mới bắt đầu ở trạng thái DRAFT
- **Output:** Hợp đồng mới (tái tục)

---

### **3. QUẢN LÝ HỦY HỢP ĐỒNG**

#### **3.1. Hủy Hợp đồng**
- **API:** `POST /api/hop-dong/{id}/cancel`
- **Mô tả:** Hủy hợp đồng trước thời hạn
- **Input:**
  - `lyDo`: Lý do hủy
- **Business Logic:**
  - ✅ Validate: Không thể hủy hợp đồng đã bị hủy
  - ✅ Chuyển trạng thái → CANCELLED
  - ✅ Ghi nhận lý do hủy vào ghiChu
  - ⚠️ Chưa tự động xử lý hoàn phí
- **Output:** Hợp đồng đã hủy
- **Liên quan:** Hoàn phí được xử lý riêng trong ThanhToanService

---

### **4. QUẢN LÝ TRẠNG THÁI HỢP ĐỒNG**

#### **4.1. Các Trạng thái Hỗ trợ**
```
DRAFT              → Nháp (mới tạo, chưa thanh toán)
PENDING_PAYMENT    → Chờ thanh toán
ACTIVE             → Đang hiệu lực
EXPIRED            → Hết hạn
CANCELLED          → Đã hủy
TERMINATED         → Chấm dứt
RENEWED            → Đã tái tục (hợp đồng cũ)
```

#### **4.2. Vấn đề State Management**
- ⚠️ **KHÔNG CÓ** method chuyển trạng thái có kiểm soát
- ⚠️ **KHÔNG CÓ** validation state transition
- ⚠️ **KHÔNG CÓ** auto chuyển DRAFT → PENDING_PAYMENT
- ⚠️ **KHÔNG CÓ** auto chuyển PENDING_PAYMENT → ACTIVE (khi thanh toán xong)
- ⚠️ **KHÔNG CÓ** auto chuyển ACTIVE → EXPIRED (khi hết hạn)
- ⚠️ **KHÔNG CÓ** audit log lịch sử chuyển trạng thái

---

### **5. BÁO CÁO & XUẤT DỮ LIỆU**

#### **5.1. Xuất Excel**
- **API:** `GET /api/hop-dong/export`
- **Mô tả:** Xuất danh sách hợp đồng ra file Excel
- **Filters:** Giống như API danh sách
- **Output:** File Excel (.xlsx)
- **Tính năng:**
  - ✅ Tên file tự động: HopDong_YYYYMMDD_HHmmss.xlsx
  - ✅ Hỗ trợ filter trước khi export

---

### **6. TÍNH PHÍ BẢO HIỂM**

#### **6.1. Thuật toán Tính Phí**
- **Logic:** `phiBaoHiem = phiCoBan × heSoPhi`
- **Input:**
  - `phiCoBan`: Từ GoiBaoHiem
  - `heSoPhi`: Từ MaTranTinhPhi (dựa trên riskScore)
- **Đặc điểm:**
  - ✅ Tự động tính khi tạo hợp đồng
  - ✅ Dựa trên điểm rủi ro từ thẩm định
  - ⚠️ Chưa hỗ trợ điều chỉnh phí thủ công
  - ⚠️ Chưa hỗ trợ giảm giá, khuyến mãi

---

### **7. PHÂN QUYỀN**

#### **7.1. Roles được phép truy cập**
```java
@RequireRole({Role.ADMIN, Role.MANAGER, Role.SALES, Role.ACCOUNTANT})
```
- **ADMIN:** Full quyền
- **MANAGER:** Full quyền
- **SALES:** Tạo, xem, tái tục hợp đồng
- **ACCOUNTANT:** Xem, quản lý thanh toán

#### **7.2. Vấn đề Phân quyền**
- ⚠️ Chưa phân quyền chi tiết theo action (tất cả role đều làm được mọi thứ)
- ⚠️ Chưa có phân quyền theo trạng thái (VD: chỉ DRAFT mới sửa được)

---

## 🔗 CÁC MODULE LIÊN QUAN

### **1. Quản lý Khách hàng**
- Lưu thông tin: Cá nhân/Tổ chức, CCCD/MST, địa chỉ, SĐT
- Liên kết: 1 khách hàng → N hợp đồng

### **2. Quản lý Xe**
- Lưu thông tin: Biển số, loại xe, năm SX, số khung/máy, giá trị xe
- Liên kết: 1 xe → N hợp đồng (qua các thời kỳ)

### **3. Quản lý Gói Bảo hiểm**
- Các loại: Vật chất, TNDS, Tai nạn lái phụ, Combo
- Phí cơ bản, phạm vi bảo hiểm, điều khoản
- Liên kết: 1 gói → N hợp đồng

### **4. Thẩm định Rủi ro (Underwriting)**
- Đánh giá rủi ro theo tiêu chí
- Tính điểm rủi ro (riskScore)
- Phân loại: CHAP_NHAN, TU_CHOI, CAN_DANH_GIA_THEM
- **Vấn đề:** Hiện tại BẮT BUỘC phải có hồ sơ thẩm định mới tạo được hợp đồng

### **5. Ma trận Tính phí**
- Mapping: riskScore → heSoPhi
- Dùng để tính phí bảo hiểm tự động

### **6. Quản lý Thanh toán**
- Ghi nhận thanh toán: Số tiền, ngày thanh toán, phương thức
- Tracking: tongPhiBaoHiem vs tongDaThanhToan
- Trạng thái: CHUA_THANH_TOAN, DA_THANH_TOAN_MOT_PHAN, DA_THANH_TOAN_DAY_DU, QUA_HAN
- **Vấn đề:** Chưa tự động chuyển trạng thái hợp đồng khi thanh toán xong

### **7. Lịch sử Tai nạn**
- Lưu lịch sử tai nạn của xe
- Ảnh hưởng đến điểm rủi ro khi tái tục

---

## ❌ CÁC CHỨC NĂNG CHƯA CÓ (CẦN BỔ SUNG)

### **1. Quản lý State Transition**
- [ ] API chuyển trạng thái có kiểm soát
- [ ] Validation state transition rules
- [ ] Audit log lịch sử chuyển trạng thái

### **2. Tự động hóa**
- [ ] Scheduled job: Auto chuyển ACTIVE → EXPIRED khi hết hạn
- [ ] Auto chuyển PENDING_PAYMENT → ACTIVE khi thanh toán đủ
- [ ] Email/SMS nhắc nhở sắp hết hạn

### **3. Dashboard & Thống kê**
- [ ] API: Hợp đồng sắp hết hạn (7/15/30 ngày)
- [ ] API: Hợp đồng cần thanh toán
- [ ] API: Thống kê tổng quan (doanh thu, số lượng, tỷ lệ tái tục)
- [ ] Biểu đồ trực quan

### **4. Đơn giản hóa Quy trình**
- [ ] Cho phép tạo hợp đồng KHÔNG CẦN thẩm định (simplified flow)
- [ ] Quick Renew (1-click tái tục)
- [ ] Bulk operations (duyệt nhiều hợp đồng cùng lúc)

### **5. In ấn & Xuất file**
- [ ] In hợp đồng PDF (theo mẫu chuẩn)
- [ ] Gửi email hợp đồng cho khách hàng
- [ ] QR code tra cứu hợp đồng

### **6. Quản lý Bồi thường**
- [ ] Tạo yêu cầu bồi thường từ hợp đồng
- [ ] Tracking lịch sử bồi thường
- [ ] Ảnh hưởng đến tái tục

---

## 📈 ĐỀ XUẤT ƯU TIÊN PHÁT TRIỂN

### **Priority 1: State Management (Nền tảng)**
1. Implement state transition logic với validation
2. Thêm audit log
3. Auto expire scheduler

### **Priority 2: Dashboard (Giá trị nghiệp vụ cao)**
4. API hợp đồng sắp hết hạn
5. API thống kê tổng quan
6. Cải thiện UI dashboard

### **Priority 3: Đơn giản hóa (UX)**
7. Cho phép tạo hợp đồng không cần thẩm định
8. Quick renew
9. Cải thiện form tạo hợp đồng

### **Priority 4: Automation (Tiết kiệm thời gian)**
10. Email/SMS tự động
11. Auto chuyển trạng thái khi thanh toán
12. Bulk operations

---

## 📝 KẾT LUẬN

Hệ thống hiện tại đã có **nền tảng tốt** cho quản lý hợp đồng bảo hiểm xe:
- ✅ CRUD đầy đủ
- ✅ Tái tục hợp đồng
- ✅ Tính phí tự động
- ✅ Tracking thanh toán
- ✅ Export Excel

**Nhưng cần cải thiện:**
- ⚠️ State management chưa chặt chẽ
- ⚠️ Thiếu automation
- ⚠️ Thiếu dashboard/báo cáo
- ⚠️ UX chưa tối ưu

**Đây là module đặc thù bảo hiểm** vì:
1. Tích hợp thẩm định rủi ro
2. Thuật toán tính phí phức tạp
3. Vòng đời đặc biệt (tái tục)
4. Liên kết với xe, lịch sử tai nạn
5. Ràng buộc pháp lý ngành bảo hiểm

---

**Tài liệu này phục vụ cho:**
- ✅ Viết SRS/BA document
- ✅ Viết khóa luận (Chương 3: Phân tích & Thiết kế)
- ✅ Planning phát triển tiếp
