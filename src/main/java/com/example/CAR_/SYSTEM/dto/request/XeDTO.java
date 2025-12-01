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
public class XeDTO {
    private Long id;
    private String maXe;
    
    @NotBlank(message = "Biển số không được để trống")
    private String bienSo;
    
    @NotBlank(message = "Số khung không được để trống")
    private String soKhung;
    
    private String soMay;
    
    @NotBlank(message = "Hãng xe không được để trống")
    private String hangXe;
    
    @NotBlank(message = "Dòng xe không được để trống")
    private String dongXe;
    
    @NotNull(message = "Năm sản xuất không được để trống")
    private Integer namSanXuat;
    
    @NotNull(message = "Năm đăng ký không được để trống")
    private Integer namDangKy;
    
    @NotBlank(message = "Màu sắc không được để trống")
    private String mauSac;
    
    @NotBlank(message = "Mục đích sử dụng không được để trống")
    private String mucDichSuDung;
    
    @NotNull(message = "Giá trị xe không được để trống")
    @Positive(message = "Giá trị xe phải lớn hơn 0")
    private BigDecimal giaTriXe;
    
    private String thongTinKyThuat;
    
    @NotNull(message = "Khách hàng không được để trống")
    private Long khachHangId;
}

