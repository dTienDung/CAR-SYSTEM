package com.example.CAR_.SYSTEM.constants;

import com.example.CAR_.SYSTEM.model.TieuChiThamDinh;

import java.util.ArrayList;
import java.util.List;

/**
 * Constants cho Tiêu chí thẩm định - HARDCODED
 * Không cho phép thay đổi qua API/UI
 */
public class TieuChiThamDinhConstants {
    
    /**
     * Danh sách tiêu chí thẩm định cố định
     * Tổng điểm tối đa: 8 điểm
     */
    public static final List<TieuChiData> TIEU_CHI_LIST = List.of(
        new TieuChiData(
            1L,
            "CT01",
            "Tuổi xe",
            "Đánh giá mức độ hao mòn và rủi ro kỹ thuật của xe theo năm sản xuất.",
            2,
            "<5 năm: 0 điểm; 5–10 năm: 1 điểm; >10 năm: 2 điểm",
            1
        ),
        new TieuChiData(
            2L,
            "CT02",
            "Mục đích sử dụng",
            "Đánh giá rủi ro theo mục đích khai thác xe. Xe kinh doanh thường có tần suất hoạt động cao.",
            2,
            "Cá nhân: 0 điểm; Kinh doanh: 2 điểm",
            2
        ),
        new TieuChiData(
            3L,
            "CT03",
            "Tuổi tài xế",
            "Đánh giá mức độ kinh nghiệm và phản xạ của người điều khiển phương tiện.",
            2,
            "26–55 tuổi: 0 điểm; 18–25 tuổi: 2 điểm; >55 tuổi: 1 điểm",
            3
        ),
        new TieuChiData(
            4L,
            "CT04",
            "Giá trị xe",
            "Giá trị xe phản ánh mức độ hiện đại và khả năng an toàn.",
            2,
            ">500 triệu: 0 điểm; 200–500 triệu: 1 điểm; <200 triệu: 2 điểm",
            4
        )
    );
    
    /**
     * Tổng điểm tối đa có thể đạt được
     */
    public static final int TONG_DIEM_TOI_DA = 8;
    
    /**
     * Data class để chứa thông tin tiêu chí
     */
    public static class TieuChiData {
        public final Long id;
        public final String maTieuChi;
        public final String tenTieuChi;
        public final String moTa;
        public final Integer diemToiDa;
        public final String dieuKien;
        public final Integer thuTu;
        
        public TieuChiData(Long id, String maTieuChi, String tenTieuChi, String moTa, 
                          Integer diemToiDa, String dieuKien, Integer thuTu) {
            this.id = id;
            this.maTieuChi = maTieuChi;
            this.tenTieuChi = tenTieuChi;
            this.moTa = moTa;
            this.diemToiDa = diemToiDa;
            this.dieuKien = dieuKien;
            this.thuTu = thuTu;
        }
        
        /**
         * Convert sang entity TieuChiThamDinh
         */
        public TieuChiThamDinh toEntity() {
            return TieuChiThamDinh.builder()
                    .id(this.id)
                    .maTieuChi(this.maTieuChi)
                    .tenTieuChi(this.tenTieuChi)
                    .moTa(this.moTa)
                    .diemToiDa(this.diemToiDa)
                    .dieuKien(this.dieuKien)
                    .thuTu(this.thuTu)
                    .active(true)
                    .chiTietThamDinh(new ArrayList<>())
                    .build();
        }
    }
}
