package com.example.CAR_.SYSTEM.controller;

import com.example.CAR_.SYSTEM.annotation.RequireRole;
import com.example.CAR_.SYSTEM.dto.request.XeDTO;
import com.example.CAR_.SYSTEM.dto.response.ApiResponse;
import com.example.CAR_.SYSTEM.model.Xe;
import com.example.CAR_.SYSTEM.model.enums.Role;
import com.example.CAR_.SYSTEM.service.XeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/xe")
@RequiredArgsConstructor
@RequireRole({Role.ADMIN, Role.SALES})
public class XeController {
    
    private final XeService xeService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<Xe>>> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long khachHangId) {
        List<Xe> result;
        if (khachHangId != null) {
            result = xeService.getByKhachHangId(khachHangId);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            result = xeService.search(keyword);
        } else {
            result = xeService.getAll();
        }
        return ResponseEntity.ok(ApiResponse.success(result));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Xe>> getById(@PathVariable Long id) {
        try {
            Xe xe = xeService.getById(id);
            return ResponseEntity.ok(ApiResponse.success(xe));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<Xe>> create(@Valid @RequestBody XeDTO dto) {
        try {
            Xe xe = xeService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Tạo xe thành công", xe));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Xe>> update(@PathVariable Long id, @Valid @RequestBody XeDTO dto) {
        try {
            Xe xe = xeService.update(id, dto);
            return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công", xe));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        try {
            xeService.delete(id);
            return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}

