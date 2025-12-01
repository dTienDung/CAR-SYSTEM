package com.example.CAR_.SYSTEM.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChiTietThamDinhRequestDTO {
    @NotNull(message = "Tiêu chí không được để trống")
    private Long tieuChiId;
    
    @NotNull(message = "Điểm không được để trống")
    @Min(value = 0, message = "Điểm phải >= 0")
    private Integer diem;
    
    private String ghiChu;
}

