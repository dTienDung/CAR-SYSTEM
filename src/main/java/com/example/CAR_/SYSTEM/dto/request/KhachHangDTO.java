package com.example.CAR_.SYSTEM.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class KhachHangDTO {
    private Long id;
    private String maKH;
    
    @NotBlank(message = "Họ tên không được để trống")
    private String hoTen;
    
    @NotBlank(message = "CCCD không được để trống")
    private String cccd;
   
    @NotNull(message = "Ngày sinh không được để trống")
    private LocalDate ngaySinh;
    
    @NotBlank(message = "Giới tính không được để trống")
    private String gioiTinh;
    
    @NotBlank(message = "Số điện thoại không được để trống")
    private String soDienThoai;
    
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;
    
    private String diaChi;
    private String ngheNghiep;
}

