package com.example.CAR_.SYSTEM.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HopDongDTO {
    private Long id;
    private String maHD;
    
    @NotNull(message = "Hồ sơ thẩm định không được để trống")
    private Long hoSoThamDinhId;
    
    @NotNull(message = "Ngày ký không được để trống")
    private LocalDate ngayKy;
    
    @NotNull(message = "Ngày hiệu lực không được để trống")
    private LocalDate ngayHieuLuc;
   
    @NotNull(message = "Ngày hết hạn không được để trống")
    private LocalDate ngayHetHan;
    
    private String ghiChu;
}

