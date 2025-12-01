package com.example.CAR_.SYSTEM.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HoSoThamDinhDTO {
    private Long id;
    private String maHS;
    
    @NotNull(message = "Khách hàng không được để trống")
    private Long khachHangId;
    
    @NotNull(message = "Xe không được để trống")
    private Long xeId;
    
    @NotNull(message = "Gói bảo hiểm không được để trống")
    private Long goiBaoHiemId;
    
    private String ghiChu;
    private Long nguoiThamDinhId;
    
    // Danh sách điểm từng tiêu chí (từ frontend)
    private List<ChiTietThamDinhRequestDTO> chiTietThamDinh;
}

