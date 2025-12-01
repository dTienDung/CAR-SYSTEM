package com.example.CAR_.SYSTEM.dto.request;

import com.example.CAR_.SYSTEM.model.enums.PhuongThucThanhToan;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThanhToanDTO {
    private Long id;
    private String maTT;
    
    @NotNull(message = "Hợp đồng không được để trống")
    private Long hopDongId;
    
    @NotNull(message = "Số tiền không được để trống")
    @Positive(message = "Số tiền phải lớn hơn 0")
    private BigDecimal soTien;
    
    @NotNull(message = "Phương thức thanh toán không được để trống")
    private PhuongThucThanhToan phuongThuc;
    
    private String soTaiKhoan;
    private String soThe;
    private String ghiChu;
}

