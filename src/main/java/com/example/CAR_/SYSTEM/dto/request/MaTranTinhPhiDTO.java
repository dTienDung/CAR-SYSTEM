package com.example.CAR_.SYSTEM.dto.request;

import jakarta.validation.constraints.Min;
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
public class MaTranTinhPhiDTO {
    
    @NotNull(message = "Điểm rủi ro từ không được để trống")
    @Min(value = 0, message = "Điểm rủi ro từ phải >= 0")
    private Integer diemRuiRoTu;
    
    @NotNull(message = "Điểm rủi ro đến không được để trống")
    @Min(value = 0, message = "Điểm rủi ro đến phải >= 0")
    private Integer diemRuiRoDen;
    
    @NotNull(message = "Hệ số phí không được để trống")
    @Positive(message = "Hệ số phí phải lớn hơn 0")
    private BigDecimal heSoPhi;
    
    private String moTa;
    
    private Boolean active = true;
}
