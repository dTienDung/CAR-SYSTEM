vâ# 🎯 HƯỚNG DẪN HỆ THỐNG TIÊU CHÍ LINH HOẠT

## 📋 VẤN ĐỀ

Hiện tại, ngưỡng quyết định **CỐ ĐỊNH**:
- CHẤP NHẬN: < 15 điểm
- XEM XÉT: 15-24 điểm
- TỪ CHỐI: ≥ 25 điểm

**Vấn đề:** Khi thêm/bớt tiêu chí, tổng điểm thay đổi → Ngưỡng không còn phù hợp!

---

## ✅ GIẢI PHÁP: SỬ DỤNG TỶ LỆ PHẦN TRĂM

### **Công thức linh hoạt:**

```java
private RiskLevel determineRiskLevel(int totalScore) {
    // Tính tổng điểm tối đa từ tất cả tiêu chí active
    int maxPossibleScore = calculateMaxPossibleScore();
    
    // Tính tỷ lệ phần trăm
    double scorePercentage = (double) totalScore / maxPossibleScore * 100;
    
    // Quyết định dựa trên tỷ lệ %
    if (scorePercentage <= 25) {           // ≤ 25% tổng điểm
        return RiskLevel.CHAP_NHAN;        // Rủi ro thấp
    } else if (scorePercentage <= 50) {    // 26-50% tổng điểm
        return RiskLevel.XEM_XET;          // Rủi ro trung bình
    } else {                                // > 50% tổng điểm
        return RiskLevel.TU_CHOI;          // Rủi ro cao
    }
}

private int calculateMaxPossibleScore() {
    List<TieuChiThamDinh> tieuChis = tieuChiThamDinhRepository.findByActiveTrueOrderByThuTu();
    return tieuChis.stream()
            .mapToInt(TieuChiThamDinh::getDiemToiDa)
            .sum();
}
```

---

## 📊 VÍ DỤ HOẠT ĐỘNG

### **Trường hợp 1: 15 tiêu chí (Tổng 97 điểm)**

| Điểm | % | Kết quả |
|------|---|---------|
| 0-24 | 0-25% | CHẤP NHẬN ✅ |
| 25-48 | 26-50% | XEM XÉT ⚠️ |
| 49-97 | 51-100% | TỪ CHỐI ❌ |

### **Trường hợp 2: Thêm 5 tiêu chí (Tổng 130 điểm)**

| Điểm | % | Kết quả |
|------|---|---------|
| 0-32 | 0-25% | CHẤP NHẬN ✅ |
| 33-65 | 26-50% | XEM XÉT ⚠️ |
| 66-130 | 51-100% | TỪ CHỐI ❌ |

### **Trường hợp 3: Giảm xuống 10 tiêu chí (Tổng 50 điểm)**

| Điểm | % | Kết quả |
|------|---|---------|
| 0-12 | 0-25% | CHẤP NHẬN ✅ |
| 13-25 | 26-50% | XEM XÉT ⚠️ |
| 26-50 | 51-100% | TỪ CHỐI ❌ |

**→ Tự động điều chỉnh theo tổng điểm!** 🎉

---

## 🔧 CODE CẬP NHẬT

### **File: HoSoThamDinhServiceImpl.java**

```java
package com.example.CAR_.SYSTEM.service.impl;

import com.example.CAR_.SYSTEM.dto.request.HoSoThamDinhDTO;
import com.example.CAR_.SYSTEM.dto.response.RiskScoreDTO;
import com.example.CAR_.SYSTEM.model.*;
import com.example.CAR_.SYSTEM.model.enums.RiskLevel;
import com.example.CAR_.SYSTEM.model.enums.TrangThaiHoSo;
import com.example.CAR_.SYSTEM.repository.*;
import com.example.CAR_.SYSTEM.service.HoSoThamDinhService;
import com.example.CAR_.SYSTEM.util.CodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HoSoThamDinhServiceImpl implements HoSoThamDinhService {

    private final HoSoThamDinhRepository hoSoThamDinhRepository;
    private final KhachHangRepository khachHangRepository;
    private final XeRepository xeRepository;
    private final GoiBaoHiemRepository goiBaoHiemRepository;
    private final TieuChiThamDinhRepository tieuChiThamDinhRepository;
    private final ChiTietThamDinhRepository chiTietThamDinhRepository;
    private final LichSuTaiNanRepository lichSuTaiNanRepository;
    private final MaTranTinhPhiRepository maTranTinhPhiRepository;

    // ... (các method khác giữ nguyên)

    /**
     * Xác định mức độ rủi ro dựa trên TỶ LỆ PHẦN TRĂM
     * → Linh hoạt khi thêm/bớt tiêu chí
     */
    private RiskLevel determineRiskLevel(int totalScore) {
        // Tính tổng điểm tối đa từ tất cả tiêu chí active
        int maxPossibleScore = calculateMaxPossibleScore();
        
        // Tránh chia cho 0
        if (maxPossibleScore == 0) {
            return RiskLevel.XEM_XET; // Mặc định nếu chưa có tiêu chí
        }
        
        // Tính tỷ lệ phần trăm
        double scorePercentage = (double) totalScore / maxPossibleScore * 100;
        
        // Quyết định dựa trên tỷ lệ %
        // Có thể điều chỉnh các ngưỡng này trong application.properties
        if (scorePercentage <= 25) {           // ≤ 25% tổng điểm
            return RiskLevel.CHAP_NHAN;        // Rủi ro thấp
        } else if (scorePercentage <= 50) {    // 26-50% tổng điểm
            return RiskLevel.XEM_XET;          // Rủi ro trung bình
        } else {                                // > 50% tổng điểm
            return RiskLevel.TU_CHOI;          // Rủi ro cao
        }
    }

    /**
     * Tính tổng điểm tối đa có thể đạt được
     */
    private int calculateMaxPossibleScore() {
        List<TieuChiThamDinh> tieuChis = tieuChiThamDinhRepository.findByActiveTrueOrderByThuTu();
        return tieuChis.stream()
                .mapToInt(TieuChiThamDinh::getDiemToiDa)
                .sum();
    }

    /**
     * Lấy mô tả chi tiết về risk level
     */
    private String getRiskLevelDescription(RiskLevel riskLevel) {
        int maxScore = calculateMaxPossibleScore();
        
        return switch (riskLevel) {
            case CHAP_NHAN -> String.format(
                "Chấp nhận - Rủi ro thấp (≤25%% tổng điểm, tối đa %d điểm)", 
                (int)(maxScore * 0.25)
            );
            case XEM_XET -> String.format(
                "Xem xét - Rủi ro trung bình (26-50%% tổng điểm, %d-%d điểm)", 
                (int)(maxScore * 0.26), 
                (int)(maxScore * 0.50)
            );
            case TU_CHOI -> String.format(
                "Từ chối - Rủi ro cao (>50%% tổng điểm, >%d điểm)", 
                (int)(maxScore * 0.50)
            );
        };
    }

    // ... (các method khác giữ nguyên)
}
```

---

## ⚙️ CẤU HÌNH LINH HOẠT HƠN (Tùy chọn)

### **File: application.properties**

```properties
# Cấu hình ngưỡng risk level (%)
risk.threshold.accept=25
risk.threshold.review=50
# Trên 50% sẽ là TỪ CHỐI
```

### **Code sử dụng config:**

```java
@Value("${risk.threshold.accept:25}")
private int acceptThreshold;

@Value("${risk.threshold.review:50}")
private int reviewThreshold;

private RiskLevel determineRiskLevel(int totalScore) {
    int maxPossibleScore = calculateMaxPossibleScore();
    
    if (maxPossibleScore == 0) {
        return RiskLevel.XEM_XET;
    }
    
    double scorePercentage = (double) totalScore / maxPossibleScore * 100;
    
    if (scorePercentage <= acceptThreshold) {
        return RiskLevel.CHAP_NHAN;
    } else if (scorePercentage <= reviewThreshold) {
        return RiskLevel.XEM_XET;
    } else {
        return RiskLevel.TU_CHOI;
    }
}
```

---

## 📊 MA TRẬN TÍNH PHÍ LINH HOẠT

### **Cách 1: Sử dụng % thay vì điểm cố định**

```sql
-- Thay vì lưu điểm cố định, lưu % tổng điểm
INSERT INTO ma_tran_tinh_phi (id, phan_tram_tu, phan_tram_den, he_so_phi, mo_ta, active) VALUES
(1, 0, 10, 0.8, 'Rủi ro rất thấp (0-10%)', 1),
(2, 11, 25, 1.0, 'Rủi ro thấp (11-25%)', 1),
(3, 26, 40, 1.2, 'Rủi ro TB thấp (26-40%)', 1),
(4, 41, 50, 1.5, 'Rủi ro TB (41-50%)', 1),
(5, 51, 65, 1.8, 'Rủi ro TB cao (51-65%)', 1),
(6, 66, 80, 2.2, 'Rủi ro cao (66-80%)', 1),
(7, 81, 100, 2.5, 'Rủi ro rất cao (81-100%)', 1);
```

### **Cách 2: Tính động trong code**

```java
private void calculatePhiBaoHiem(HoSoThamDinh hoSo) {
    int maxScore = calculateMaxPossibleScore();
    double scorePercentage = (double) hoSo.getRiskScore() / maxScore * 100;
    
    // Tìm ma trận theo %
    var maTran = maTranTinhPhiRepository.findByPhanTram(scorePercentage);
    
    if (maTran.isPresent()) {
        BigDecimal phiBaoHiem = hoSo.getGoiBaoHiem().getPhiCoBan()
                .multiply(maTran.get().getHeSoPhi());
        hoSo.setPhiBaoHiem(phiBaoHiem);
    }
}
```

---

## 🎯 ƯU ĐIỂM CỦA GIẢI PHÁP

### ✅ **Linh hoạt tuyệt đối**
- Thêm tiêu chí mới → Tự động điều chỉnh
- Xóa tiêu chí → Tự động điều chỉnh
- Thay đổi điểm tối đa → Tự động điều chỉnh

### ✅ **Dễ bảo trì**
- Không cần sửa code khi thay đổi tiêu chí
- Logic rõ ràng, dễ hiểu
- Có thể config qua properties

### ✅ **Công bằng**
- Tỷ lệ % luôn nhất quán
- Không bị lệch khi thêm/bớt tiêu chí

### ✅ **Mở rộng dễ dàng**
- Có thể thêm nhiều cấp độ risk level
- Có thể điều chỉnh ngưỡng theo từng gói bảo hiểm

---

## 📝 VÍ DỤ THỰC TẾ

### **Scenario 1: Hệ thống ban đầu (15 tiêu chí, 97 điểm)**

Khách hàng A có 20 điểm:
- % = 20/97 × 100 = **20.6%**
- Kết quả: **CHẤP NHẬN** ✅

### **Scenario 2: Thêm 5 tiêu chí mới (20 tiêu chí, 130 điểm)**

Khách hàng A vẫn có 20 điểm:
- % = 20/130 × 100 = **15.4%**
- Kết quả: **CHẤP NHẬN** ✅ (vẫn nhất quán!)

### **Scenario 3: Khách hàng B có nhiều rủi ro**

Khách hàng B có 60 điểm (với 130 tổng điểm):
- % = 60/130 × 100 = **46.2%**
- Kết quả: **XEM XÉT** ⚠️

---

## 🚀 TRIỂN KHAI

### **Bước 1: Cập nhật Service**
Copy code trên vào `HoSoThamDinhServiceImpl.java`

### **Bước 2: Test**
```java
@Test
public void testFlexibleRiskLevel() {
    // Test với 15 tiêu chí
    // Test với 20 tiêu chí
    // Test với 10 tiêu chí
    // Kết quả phải nhất quán theo %
}
```

### **Bước 3: Cập nhật ma trận (tùy chọn)**
Chuyển sang sử dụng % thay vì điểm cố định

---

## 💡 KHUYẾN NGHỊ

1. ✅ **Sử dụng giải pháp % ngay lập tức**
2. ✅ **Giữ nguyên 15 tiêu chí hiện tại** (97 điểm)
3. ✅ **Thêm config vào properties** để dễ điều chỉnh
4. ⚠️ **Cân nhắc điều chỉnh ngưỡng** nếu cần:
   - Hiện tại: 25% / 50%
   - Có thể thử: 30% / 60% (dễ dàng hơn)

---

**Bạn có muốn tôi tạo file code hoàn chỉnh với giải pháp này không?**
