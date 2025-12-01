package com.example.CAR_.SYSTEM.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HopDongRenewDTO {
    @NotNull(message = "Ngày ký không được để trống")
    private LocalDate ngayKy;
    
    @NotNull(message = "Ngày hiệu lực không được để trống")
    private LocalDate ngayHieuLuc;
    
    @NotNull(message = "Ngày hết hạn không được để trống")
    private LocalDate ngayHetHan;
    
    private String ghiChu;
}

