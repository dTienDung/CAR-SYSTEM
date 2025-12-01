package com.example.CAR_.SYSTEM.controller;

import com.example.CAR_.SYSTEM.annotation.RequireRole;
import com.example.CAR_.SYSTEM.dto.request.GiaoDichHoanPhiDTO;
import com.example.CAR_.SYSTEM.dto.request.ThanhToanDTO;
import com.example.CAR_.SYSTEM.dto.response.ApiResponse;
import com.example.CAR_.SYSTEM.model.ThanhToan;
import com.example.CAR_.SYSTEM.model.enums.Role;
import com.example.CAR_.SYSTEM.service.ThanhToanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/thanh-toan")
@RequiredArgsConstructor
@RequireRole({Role.ADMIN, Role.MANAGER, Role.ACCOUNTANT})
public class ThanhToanController {
    
    private final ThanhToanService thanhToanService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<ThanhToan>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(thanhToanService.getAll()));
    }
    
    @GetMapping("/hop-dong/{hopDongId}")
    public ResponseEntity<ApiResponse<List<ThanhToan>>> getByHopDongId(@PathVariable Long hopDongId) {
        return ResponseEntity.ok(ApiResponse.success(thanhToanService.getByHopDongId(hopDongId)));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ThanhToan>> getById(@PathVariable Long id) {
        try {
            ThanhToan thanhToan = thanhToanService.getById(id);
            return ResponseEntity.ok(ApiResponse.success(thanhToan));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<ThanhToan>> create(@Valid @RequestBody ThanhToanDTO dto) {
        try {
            ThanhToan thanhToan = thanhToanService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Thanh toán thành công", thanhToan));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/giao-dich-hoan-phi")
    public ResponseEntity<ApiResponse<ThanhToan>> createHoanPhi(@Valid @RequestBody GiaoDichHoanPhiDTO dto) {
        try {
            ThanhToan thanhToan = thanhToanService.createHoanPhi(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Tạo giao dịch hoàn phí thành công", thanhToan));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        try {
            thanhToanService.delete(id);
            return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}

