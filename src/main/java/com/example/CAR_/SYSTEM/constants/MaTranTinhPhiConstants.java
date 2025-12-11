package com.example.CAR_.SYSTEM.constants;

import com.example.CAR_.SYSTEM.model.MaTranTinhPhi;

import java.math.BigDecimal;
import java.util.List;

/**
 * Constants cho Ma trận tính phí - HARDCODED
 * Không cho phép thay đổi qua API/UI
 */
public class MaTranTinhPhiConstants {
    
    /**
     * Danh sách ma trận tính phí cố định
     * Ánh xạ từ điểm rủi ro (riskScore) sang hệ số phí (coef)
     * Công thức: premium = baseFee × coef
     */
    public static final List<MaTranData> MA_TRAN_LIST = List.of(
        new MaTranData(
            1L,
            0,
            2,
            BigDecimal.valueOf(1.0),
            "Rủi ro thấp - Hồ sơ an toàn, phí giữ nguyên"
        ),
        new MaTranData(
            2L,
            3,
            5,
            BigDecimal.valueOf(1.2),
            "Rủi ro trung bình - Phụ thu 20%"
        ),
        new MaTranData(
            3L,
            6,
            999,  // Tất cả điểm >= 6
            BigDecimal.valueOf(1.5),
            "Rủi ro cao - Phụ thu 50% hoặc xem xét từ chối"
        )
    );
    
    /**
     * Tìm hệ số phí theo điểm rủi ro
     * @param diemRuiRo Điểm rủi ro tổng
     * @return Hệ số phí tương ứng
     */
    public static BigDecimal getHeSoPhi(int diemRuiRo) {
        for (MaTranData maTran : MA_TRAN_LIST) {
            if (diemRuiRo >= maTran.diemRuiRoTu && diemRuiRo <= maTran.diemRuiRoDen) {
                return maTran.heSoPhi;
            }
        }
        // Mặc định trả về hệ số cao nhất nếu vượt quá
        return BigDecimal.valueOf(1.5);
    }
    
    /**
     * Data class để chứa thông tin ma trận
     */
    public static class MaTranData {
        public final Long id;
        public final Integer diemRuiRoTu;
        public final Integer diemRuiRoDen;
        public final BigDecimal heSoPhi;
        public final String moTa;
        
        public MaTranData(Long id, Integer diemRuiRoTu, Integer diemRuiRoDen, 
                         BigDecimal heSoPhi, String moTa) {
            this.id = id;
            this.diemRuiRoTu = diemRuiRoTu;
            this.diemRuiRoDen = diemRuiRoDen;
            this.heSoPhi = heSoPhi;
            this.moTa = moTa;
        }
        
        /**
         * Convert sang entity MaTranTinhPhi
         */
        public MaTranTinhPhi toEntity() {
            return MaTranTinhPhi.builder()
                    .id(this.id)
                    .diemRuiRoTu(this.diemRuiRoTu)
                    .diemRuiRoDen(this.diemRuiRoDen)
                    .heSoPhi(this.heSoPhi)
                    .moTa(this.moTa)
                    .active(true)
                    .build();
        }
    }
}
