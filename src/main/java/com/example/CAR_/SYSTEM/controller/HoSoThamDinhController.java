package com.example.CAR_.SYSTEM.controller;

import com.example.CAR_.SYSTEM.annotation.RequireRole;
import com.example.CAR_.SYSTEM.dto.request.HoSoThamDinhDTO;
import com.example.CAR_.SYSTEM.dto.response.ApiResponse;
import com.example.CAR_.SYSTEM.dto.response.RiskScoreDTO;
import com.example.CAR_.SYSTEM.model.HoSoThamDinh;
import com.example.CAR_.SYSTEM.model.enums.RiskLevel;
import com.example.CAR_.SYSTEM.model.enums.Role;
import com.example.CAR_.SYSTEM.model.enums.TrangThaiHoSo;
import com.example.CAR_.SYSTEM.service.ExcelExportService;
import com.example.CAR_.SYSTEM.service.HoSoThamDinhService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/ho-so-tham-dinh")
@RequiredArgsConstructor
@RequireRole({Role.ADMIN, Role.MANAGER, Role.UNDERWRITER, Role.SALES})
public class HoSoThamDinhController {
    
    private final HoSoThamDinhService hoSoThamDinhService;
    private final ExcelExportService excelExportService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<HoSoThamDinh>>> getAll(
            @RequestParam(required = false) TrangThaiHoSo trangThai,
            @RequestParam(required = false) RiskLevel riskLevel) {
        List<HoSoThamDinh> result;
        if (trangThai != null || riskLevel != null) {
            result = hoSoThamDinhService.filter(trangThai, riskLevel);
        } else {
            result = hoSoThamDinhService.getAll();
        }
        return ResponseEntity.ok(ApiResponse.success(result));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HoSoThamDinh>> getById(@PathVariable Long id) {
        try {
            HoSoThamDinh hoSo = hoSoThamDinhService.getById(id);
            return ResponseEntity.ok(ApiResponse.success(hoSo));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<HoSoThamDinh>> create(@Valid @RequestBody HoSoThamDinhDTO dto) {
        try {
            HoSoThamDinh hoSo = hoSoThamDinhService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Tạo hồ sơ thẩm định thành công", hoSo));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HoSoThamDinh>> update(@PathVariable Long id, @Valid @RequestBody HoSoThamDinhDTO dto) {
        try {
            HoSoThamDinh hoSo = hoSoThamDinhService.update(id, dto);
            return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công", hoSo));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/{id}/risk-score")
    public ResponseEntity<ApiResponse<RiskScoreDTO>> calculateRiskScore(@PathVariable Long id) {
        try {
            RiskScoreDTO result = hoSoThamDinhService.calculateRiskScore(id);
            return ResponseEntity.ok(ApiResponse.success("Tính điểm rủi ro thành công", result));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        try {
            hoSoThamDinhService.delete(id);
            return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportToExcel(
            @RequestParam(required = false) TrangThaiHoSo trangThai,
            @RequestParam(required = false) RiskLevel riskLevel) {
        try {
            List<HoSoThamDinh> hoSoList;
            if (trangThai != null || riskLevel != null) {
                hoSoList = hoSoThamDinhService.filter(trangThai, riskLevel);
            } else {
                hoSoList = hoSoThamDinhService.getAllWithDetails();
            }
            
            byte[] excelData = excelExportService.exportHoSoThamDinh(hoSoList);
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = "HoSoThamDinh_" + timestamp + ".xlsx";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(excelData.length);
            
            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
