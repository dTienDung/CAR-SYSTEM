package com.example.CAR_.SYSTEM.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichSuTaiNanResponseDTO {
    private Long id;
    private Long xeId;
    private String bienSo;
    private String khachHang;
    private LocalDate ngayXayRa;
    private String moTa;
    private BigDecimal thietHai;
    private String diaDiem;
}


