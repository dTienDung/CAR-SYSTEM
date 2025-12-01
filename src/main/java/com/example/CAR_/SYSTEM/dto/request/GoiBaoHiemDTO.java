package com.example.CAR_.SYSTEM.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class GoiBaoHiemDTO {
    private Long id;
    private String maGoi;
    
    @NotBlank(message = "Tên gói không được để trống")
    private String tenGoi;
    
    private String moTa;
    
    @NotNull(message = "Phí cơ bản không được để trống")
    @Positive(message = "Phí cơ bản phải lớn hơn 0")
    private BigDecimal phiCoBan;
    
    private String quyenLoi;
    private Boolean active = true;
}

