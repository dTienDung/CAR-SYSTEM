package com.example.CAR_.SYSTEM.controller;

import com.example.CAR_.SYSTEM.annotation.RequireRole;
import com.example.CAR_.SYSTEM.dto.request.CancelDTO;
import com.example.CAR_.SYSTEM.dto.request.HopDongDTO;
import com.example.CAR_.SYSTEM.dto.request.HopDongRenewDTO;
import com.example.CAR_.SYSTEM.dto.response.ApiResponse;
import com.example.CAR_.SYSTEM.model.HopDong;
import com.example.CAR_.SYSTEM.model.enums.Role;
import com.example.CAR_.SYSTEM.model.enums.TrangThaiHopDong;
import com.example.CAR_.SYSTEM.service.ExcelExportService;
import com.example.CAR_.SYSTEM.service.HopDongService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/hop-dong")
@RequiredArgsConstructor
@RequireRole({Role.ADMIN, Role.MANAGER, Role.SALES, Role.ACCOUNTANT})
public class HopDongController {
    
    private final HopDongService hopDongService;
    private final ExcelExportService excelExportService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<HopDong>>> getAll(
            @RequestParam(required = false) TrangThaiHopDong trangThai,
            @RequestParam(required = false) Long khachHangId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        List<HopDong> result;
        if (trangThai != null || khachHangId != null || fromDate != null || toDate != null) {
            result = hopDongService.filter(trangThai, khachHangId, fromDate, toDate);
        } else {
            result = hopDongService.getAll();
        }
        return ResponseEntity.ok(ApiResponse.success(result));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HopDong>> getById(@PathVariable Long id) {
        try {
            HopDong hopDong = hopDongService.getById(id);
            return ResponseEntity.ok(ApiResponse.success(hopDong));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<HopDong>> create(@Valid @RequestBody HopDongDTO dto) {
        try {
            HopDong hopDong = hopDongService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Tạo hợp đồng thành công", hopDong));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HopDong>> update(@PathVariable Long id, @Valid @RequestBody HopDongDTO dto) {
        try {
            HopDong hopDong = hopDongService.update(id, dto);
            return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công", hopDong));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/{id}/renew")
    public ResponseEntity<ApiResponse<HopDong>> renew(@PathVariable Long id, @Valid @RequestBody HopDongRenewDTO dto) {
        try {
            HopDong hopDong = hopDongService.renew(id, dto);
            return ResponseEntity.ok(ApiResponse.success("Tái tục hợp đồng thành công", hopDong));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<HopDong>> cancel(@PathVariable Long id, @RequestBody CancelDTO dto) {
        try {
            HopDong hopDong = hopDongService.cancel(id, dto);
            return ResponseEntity.ok(ApiResponse.success("Hủy hợp đồng thành công", hopDong));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        try {
            hopDongService.delete(id);
            return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportToExcel(
            @RequestParam(required = false) TrangThaiHopDong trangThai,
            @RequestParam(required = false) Long khachHangId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        try {
            List<HopDong> hopDongList;
            if (trangThai != null || khachHangId != null || fromDate != null || toDate != null) {
                hopDongList = hopDongService.filter(trangThai, khachHangId, fromDate, toDate);
            } else {
                hopDongList = hopDongService.getAll();
            }
            
            byte[] excelData = excelExportService.exportHopDong(hopDongList);
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = "HopDong_" + timestamp + ".xlsx";
            
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
