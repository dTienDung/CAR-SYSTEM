package com.example.CAR_.SYSTEM.controller;

import com.example.CAR_.SYSTEM.annotation.RequireRole;
import com.example.CAR_.SYSTEM.dto.request.GoiBaoHiemDTO;
import com.example.CAR_.SYSTEM.dto.response.ApiResponse;
import com.example.CAR_.SYSTEM.model.GoiBaoHiem;
import com.example.CAR_.SYSTEM.model.enums.Role;
import com.example.CAR_.SYSTEM.service.GoiBaoHiemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goi-bao-hiem")
@RequiredArgsConstructor
@RequireRole({Role.ADMIN, Role.SALES})
public class GoiBaoHiemController {
    
    private final GoiBaoHiemService goiBaoHiemService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<GoiBaoHiem>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(goiBaoHiemService.getAll()));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GoiBaoHiem>> getById(@PathVariable Long id) {
        try {
            GoiBaoHiem goiBaoHiem = goiBaoHiemService.getById(id);
            return ResponseEntity.ok(ApiResponse.success(goiBaoHiem));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<GoiBaoHiem>> create(@Valid @RequestBody GoiBaoHiemDTO dto) {
        try {
            GoiBaoHiem goiBaoHiem = goiBaoHiemService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Tạo gói bảo hiểm thành công", goiBaoHiem));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GoiBaoHiem>> update(@PathVariable Long id, @Valid @RequestBody GoiBaoHiemDTO dto) {
        try {
            GoiBaoHiem goiBaoHiem = goiBaoHiemService.update(id, dto);
            return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công", goiBaoHiem));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        try {
            goiBaoHiemService.delete(id);
            return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}

