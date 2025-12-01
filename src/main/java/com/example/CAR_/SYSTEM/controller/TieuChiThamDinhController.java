package com.example.CAR_.SYSTEM.controller;

import com.example.CAR_.SYSTEM.annotation.RequireRole;
import com.example.CAR_.SYSTEM.dto.request.TieuChiThamDinhDTO;
import com.example.CAR_.SYSTEM.dto.response.ApiResponse;
import com.example.CAR_.SYSTEM.model.TieuChiThamDinh;
import com.example.CAR_.SYSTEM.model.enums.Role;
import com.example.CAR_.SYSTEM.service.TieuChiThamDinhService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tieu-chi-tham-dinh")
@RequiredArgsConstructor
@RequireRole({Role.ADMIN, Role.UNDERWRITER})
public class TieuChiThamDinhController {
    
    private final TieuChiThamDinhService tieuChiThamDinhService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<TieuChiThamDinh>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(tieuChiThamDinhService.getAll()));
    }
    
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<TieuChiThamDinh>>> getActive() {
        return ResponseEntity.ok(ApiResponse.success(
            tieuChiThamDinhService.getAll().stream()
                .filter(tc -> tc.getActive())
                .sorted((a, b) -> a.getThuTu().compareTo(b.getThuTu()))
                .toList()
        ));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TieuChiThamDinh>> getById(@PathVariable Long id) {
        try {
            TieuChiThamDinh tieuChi = tieuChiThamDinhService.getById(id);
            return ResponseEntity.ok(ApiResponse.success(tieuChi));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<TieuChiThamDinh>> create(@Valid @RequestBody TieuChiThamDinhDTO dto) {
        try {
            TieuChiThamDinh tieuChi = tieuChiThamDinhService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Tạo tiêu chí thành công", tieuChi));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TieuChiThamDinh>> update(@PathVariable Long id, @Valid @RequestBody TieuChiThamDinhDTO dto) {
        try {
            TieuChiThamDinh tieuChi = tieuChiThamDinhService.update(id, dto);
            return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công", tieuChi));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        try {
            tieuChiThamDinhService.delete(id);
            return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}

