package com.example.CAR_.SYSTEM.dto.request;

import com.example.CAR_.SYSTEM.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    
    @NotBlank(message = "Username không được để trống")
    private String username;
    
    private String password; // Optional khi update
    
    @NotBlank(message = "Họ tên không được để trống")
    private String hoTen;
    
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;
    
    private String soDienThoai;
    
    @NotNull(message = "Role không được để trống")
    private Role role;
    
    private Boolean active = true;
    private String ghiChu;
}

