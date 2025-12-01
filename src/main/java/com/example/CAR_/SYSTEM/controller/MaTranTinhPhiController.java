package com.example.CAR_.SYSTEM.controller;

import com.example.CAR_.SYSTEM.annotation.RequireRole;
import com.example.CAR_.SYSTEM.dto.request.MaTranTinhPhiDTO;
import com.example.CAR_.SYSTEM.dto.response.ApiResponse;
import com.example.CAR_.SYSTEM.model.MaTranTinhPhi;
import com.example.CAR_.SYSTEM.model.enums.Role;
import com.example.CAR_.SYSTEM.service.MaTranTinhPhiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ma-tran-tinh-phi")
@RequiredArgsConstructor
@RequireRole({Role.ADMIN})
public class MaTranTinhPhiController {
    
    private final MaTranTinhPhiService maTranTinhPhiService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<MaTranTinhPhi>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(maTranTinhPhiService.getAll()));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MaTranTinhPhi>> getById(@PathVariable Long id) {
        try {
            MaTranTinhPhi maTran = maTranTinhPhiService.getById(id);
            return ResponseEntity.ok(ApiResponse.success(maTran));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<MaTranTinhPhi>> create(@Valid @RequestBody MaTranTinhPhiDTO dto) {
        try {
            MaTranTinhPhi maTran = maTranTinhPhiService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Tạo ma trận tính phí thành công", maTran));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MaTranTinhPhi>> update(@PathVariable Long id, @Valid @RequestBody MaTranTinhPhiDTO dto) {
        try {
            MaTranTinhPhi maTran = maTranTinhPhiService.update(id, dto);
            return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công", maTran));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        try {
            maTranTinhPhiService.delete(id);
            return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}

