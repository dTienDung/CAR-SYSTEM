package com.example.CAR_.SYSTEM.controller;

import com.example.CAR_.SYSTEM.annotation.RequireRole;
import com.example.CAR_.SYSTEM.dto.request.KhachHangDTO;
import com.example.CAR_.SYSTEM.dto.response.ApiResponse;
import com.example.CAR_.SYSTEM.model.KhachHang;
import com.example.CAR_.SYSTEM.model.enums.Role;
import com.example.CAR_.SYSTEM.service.KhachHangService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/khach-hang")
@RequiredArgsConstructor
@RequireRole({Role.ADMIN, Role.SALES})
public class KhachHangController {
    
    private final KhachHangService khachHangService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<KhachHang>>> getAll(
            @RequestParam(required = false) String keyword) {
        List<KhachHang> result;
        if (keyword != null && !keyword.trim().isEmpty()) {
            result = khachHangService.search(keyword);
        } else {
            result = khachHangService.getAll();
        }
        return ResponseEntity.ok(ApiResponse.success(result));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<KhachHang>> getById(@PathVariable Long id) {
        try {
            KhachHang khachHang = khachHangService.getById(id);
            return ResponseEntity.ok(ApiResponse.success(khachHang));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<KhachHang>> create(@Valid @RequestBody KhachHangDTO dto) {
        try {
            KhachHang khachHang = khachHangService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Tạo khách hàng thành công", khachHang));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<KhachHang>> update(@PathVariable Long id, @Valid @RequestBody KhachHangDTO dto) {
        try {
            KhachHang khachHang = khachHangService.update(id, dto);
            return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công", khachHang));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        try {
            khachHangService.delete(id);
            return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}

