package com.example.CAR_.SYSTEM.dto.response;

import com.example.CAR_.SYSTEM.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private String username;
    private String hoTen;
    private String email;
    private Role role;
}

