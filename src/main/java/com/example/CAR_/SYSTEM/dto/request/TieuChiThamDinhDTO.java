package com.example.CAR_.SYSTEM.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TieuChiThamDinhDTO {
    private Long id;
    private String maTieuChi;
    
    @NotBlank(message = "Tên tiêu chí không được để trống")
    private String tenTieuChi;
    
    private String moTa;
    
    @NotNull(message = "Điểm tối đa không được để trống")
    @Positive(message = "Điểm tối đa phải lớn hơn 0")
    private Integer diemToiDa;
    
    @NotNull(message = "Thứ tự không được để trống")
    private Integer thuTu;
    
    private Boolean active = true;
    private String dieuKien;
}

