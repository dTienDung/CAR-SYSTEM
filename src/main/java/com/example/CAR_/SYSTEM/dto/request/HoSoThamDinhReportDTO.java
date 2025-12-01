package com.example.CAR_.SYSTEM.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class HoSoThamDinhReportDTO {
    private String maHS;
    private String khachHang;   // tên khách hàng
    private String bienSo;      // biển số xe
    private String goiBaoHiem;  // tên gói BH
    private Integer riskScore;
    private String riskLevel;
    private String trangThai;
    private BigDecimal phiBaoHiem;
}