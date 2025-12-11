package com.example.CAR_.SYSTEM.controller;

import com.example.CAR_.SYSTEM.annotation.RequireRole;
import com.example.CAR_.SYSTEM.dto.response.ApiResponse;
import com.example.CAR_.SYSTEM.dto.response.LichSuTaiNanResponseDTO;
import com.example.CAR_.SYSTEM.model.LichSuTaiNan;
import com.example.CAR_.SYSTEM.model.enums.Role;
import com.example.CAR_.SYSTEM.service.LichSuTaiNanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lich-su-tai-nan")
@RequiredArgsConstructor
@RequireRole({Role.ADMIN, Role.MANAGER, Role.SALES})
public class LichSuTaiNanController {

    private final LichSuTaiNanService lichSuTaiNanService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<LichSuTaiNanResponseDTO>>> getAll(
            @RequestParam(required = false) Long xeId
    ) {
        List<LichSuTaiNan> result;
        if (xeId != null) {
            result = lichSuTaiNanService.getByXeId(xeId);
        } else {
            result = lichSuTaiNanService.getAll();
        }
        List<LichSuTaiNanResponseDTO> dtoList = result.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(dtoList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LichSuTaiNanResponseDTO>> getById(@PathVariable Long id) {
        try {
            LichSuTaiNan lichSuTaiNan = lichSuTaiNanService.getById(id);
            return ResponseEntity.ok(ApiResponse.success(toDto(lichSuTaiNan)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LichSuTaiNanResponseDTO>> create(@Valid @RequestBody LichSuTaiNan lichSuTaiNan) {
        try {
            LichSuTaiNan created = lichSuTaiNanService.create(lichSuTaiNan);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Tạo lịch sử tai nạn thành công", toDto(created)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        try {
            lichSuTaiNanService.delete(id);
            return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    private LichSuTaiNanResponseDTO toDto(LichSuTaiNan entity) {
        String bienSo = null;
        String khachHang = null;
        Long xeId = null;

        if (entity.getXe() != null) {
            xeId = entity.getXe().getId();
            bienSo = entity.getXe().getBienSo();
            if (entity.getXe().getKhachHang() != null) {
                khachHang = entity.getXe().getKhachHang().getHoTen();
            }
        }

        return LichSuTaiNanResponseDTO.builder()
                .id(entity.getId())
                .xeId(xeId)
                .bienSo(bienSo)
                .khachHang(khachHang)
                .ngayXayRa(entity.getNgayXayRa())
                .moTa(entity.getMoTa())
                .thietHai(entity.getThietHai())
                .diaDiem(entity.getDiaDiem())
                .build();
    }
}


