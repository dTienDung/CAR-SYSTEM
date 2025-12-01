package com.example.CAR_.SYSTEM.controller;

import com.example.CAR_.SYSTEM.dto.request.LoginRequest;
import com.example.CAR_.SYSTEM.dto.request.RegisterRequest;
import com.example.CAR_.SYSTEM.dto.response.ApiResponse;
import com.example.CAR_.SYSTEM.dto.response.LoginResponse;
import com.example.CAR_.SYSTEM.model.User;
import com.example.CAR_.SYSTEM.model.enums.Role;
import com.example.CAR_.SYSTEM.repository.UserRepository;
import com.example.CAR_.SYSTEM.config.JwtConfig;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<LoginResponse>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            // Kiểm tra username đã tồn tại
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new RuntimeException("Username đã tồn tại");
            }
            
            // Kiểm tra email đã tồn tại
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email đã tồn tại");
            }
            
            // Tạo user mới
            User user = User.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .hoTen(request.getHoTen())
                    .email(request.getEmail())
                    .soDienThoai(request.getSoDienThoai())
                    .role(request.getRole())
                    .active(true)
                    .build();
            
            user = userRepository.save(user);
            
            // Tự động đăng nhập sau khi đăng ký
            String token = jwtConfig.generateToken(user.getUsername());
            
            LoginResponse response = LoginResponse.builder()
                    .token(token)
                    .username(user.getUsername())
                    .hoTen(user.getHoTen())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .build();
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Đăng ký thành công", response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("Tên đăng nhập hoặc mật khẩu không đúng"));
            
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new RuntimeException("Tên đăng nhập hoặc mật khẩu không đúng");
            }
            
            if (!user.getActive()) {
                throw new RuntimeException("Tài khoản đã bị khóa");
            }
            
            String token = jwtConfig.generateToken(user.getUsername());
            
            LoginResponse response = LoginResponse.builder()
                    .token(token)
                    .username(user.getUsername())
                    .hoTen(user.getHoTen())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .build();
            
            return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công", response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCurrentUser() {
        // TODO: Implement với JWT filter
        Map<String, Object> result = new HashMap<>();
        result.put("message", "Chức năng này cần JWT authentication");
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}

