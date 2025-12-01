package com.example.CAR_.SYSTEM.dto.request;

import com.example.CAR_.SYSTEM.model.enums.PhuongThucThanhToan;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiaoDichHoanPhiDTO {
    @NotNull(message = "Hợp đồng không được để trống")
    private Long hopDongId;
    
    @NotNull(message = "Số tiền hoàn không được để trống")
    private BigDecimal soTienHoan;
    
    @NotNull(message = "Phương thức hoàn không được để trống")
    private PhuongThucThanhToan phuongThuc;
    
    private String soTaiKhoan;
    private String ghiChu;
}

