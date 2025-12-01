package com.example.CAR_.SYSTEM.controller;

import com.example.CAR_.SYSTEM.annotation.RequireRole;
import com.example.CAR_.SYSTEM.dto.request.HoSoThamDinhReportDTO;
import com.example.CAR_.SYSTEM.dto.response.ApiResponse;
import com.example.CAR_.SYSTEM.model.HoSoThamDinh;
import com.example.CAR_.SYSTEM.model.HopDong;
import com.example.CAR_.SYSTEM.model.ThanhToan;
import com.example.CAR_.SYSTEM.model.enums.Role;

import com.example.CAR_.SYSTEM.service.impl.HoSoThamDinhServiceImpl;
import lombok.RequiredArgsConstructor;


import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.CAR_.SYSTEM.service.impl.ThanhToanServiceImpl;
import com.example.CAR_.SYSTEM.service.impl.TieuChiThamDinhServiceImpl;

@Slf4j
@RestController
@RequestMapping("/api/bao-cao")
@RequiredArgsConstructor
@RequireRole({Role.ADMIN, Role.MANAGER})
public class BaoCaoController {


    private final HoSoThamDinhServiceImpl hoSoThamDinhServiceImpl;
    private final ThanhToanServiceImpl thanhToanServiceImpl;
    private final com.example.CAR_.SYSTEM.repository.HopDongRepository hopDongRepository;
    private final com.example.CAR_.SYSTEM.repository.HoSoThamDinhRepository hoSoThamDinhRepository;
    private final com.example.CAR_.SYSTEM.repository.ThanhToanRepository thanhToanRepository;
    @GetMapping("/doanh-thu")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDoanhThu(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate)
    {

        Map<String, Object> result = new HashMap<>();

        List<ThanhToan> thanhToans = thanhToanServiceImpl.getAll();

        BigDecimal tongDoanhThu = thanhToans.stream()
                .filter(tt -> (fromDate == null || !tt.getCreatedAt().toLocalDate().isBefore(fromDate)) &&
                        (toDate == null || !tt.getCreatedAt().toLocalDate().isAfter(toDate)))
                .map(ThanhToan::getSoTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        result.put("tongDoanhThu", tongDoanhThu);
        result.put("fromDate", fromDate);
        result.put("toDate", toDate);

        return ResponseEntity.ok(ApiResponse.success(result));
    }
    @GetMapping("/tai-tuc")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTaiTuc(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        Map<String, Object> result = new HashMap<>();

        List<HopDong> hopDongs = hoSoThamDinhServiceImpl.getAll().stream()
                .flatMap(hs -> hs.getDanhSachHopDong().stream())
                .filter(hd -> (fromDate == null || !hd.getCreatedAt().toLocalDate().isBefore(fromDate)) &&
                        (toDate == null || !hd.getCreatedAt().toLocalDate().isAfter(toDate)))
                .toList();

        result.put("soHopDongTaiTuc", hopDongs.size());
        result.put("fromDate", fromDate);
        result.put("toDate", toDate);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/tham-dinh")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getThamDinh(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        List<HoSoThamDinh> hoSoList = hoSoThamDinhServiceImpl.getAll().stream()
                .filter(hs -> (fromDate == null || !hs.getCreatedAt().toLocalDate().isBefore(fromDate)) &&
                        (toDate == null || !hs.getCreatedAt().toLocalDate().isAfter(toDate)))
                .toList();

        Map<String, Long> countByStatus = hoSoList.stream()
                .collect(Collectors.groupingBy(hs -> hs.getTrangThai().name(), Collectors.counting()));

        Double avgRiskScore = hoSoList.stream()
                .mapToInt(HoSoThamDinh::getRiskScore)
                .average().orElse(0.0);

        BigDecimal totalPhi = hoSoList.stream()
                .map(hs -> hs.getPhiBaoHiem() == null ? BigDecimal.ZERO : hs.getPhiBaoHiem())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Map sang DTO để trả frontend
        List<HoSoThamDinhReportDTO> details = hoSoList.stream()
                .map(hs -> new HoSoThamDinhReportDTO(
                        hs.getMaHS(),
                        hs.getKhachHang() != null ? hs.getKhachHang().getHoTen() : "",
                        hs.getXe() != null ? hs.getXe().getBienSo() : "",
                        hs.getGoiBaoHiem() != null ? hs.getGoiBaoHiem().getTenGoi() : "",
                        hs.getRiskScore(),
                        hs.getRiskLevel().name(),
                        hs.getTrangThai().name(),
                        hs.getPhiBaoHiem()
                ))
                .toList();

        Map<String, Object> result = new HashMap<>();
        result.put("countByStatus", countByStatus);
        result.put("avgRiskScore", avgRiskScore);
        result.put("totalPhi", totalPhi);
        result.put("details", details);  // dùng DTO thay vì entity
        log.info(" cfvb ++++++++++++++++++++++++++++++++++++++++++++++++ "+details.toString());

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // ===== DASHBOARD CHARTS ENDPOINTS =====
    
    @GetMapping("/hop-dong-lifecycle")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getHopDongLifecycle() {
        List<Object[]> results = hopDongRepository.countByTrangThai();
        Map<String, Long> data = new HashMap<>();
        for (Object[] row : results) {
            data.put(row[0].toString(), (Long) row[1]);
        }
        return ResponseEntity.ok(ApiResponse.success(data));
    }
    
    @GetMapping("/tham-dinh-result")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getThamDinhResult() {
        List<Object[]> results = hoSoThamDinhRepository.countByRiskLevel();
        Map<String, Long> data = new HashMap<>();
        for (Object[] row : results) {
            data.put(row[0].toString(), (Long) row[1]);
        }
        return ResponseEntity.ok(ApiResponse.success(data));
    }
    
    @GetMapping("/doanh-thu-timeline")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDoanhThuTimeline(
            @RequestParam(required = false) Integer days,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        
        java.time.LocalDateTime fromDateTime;
        
        // If startDate is provided, use it; otherwise use days (default 21)
        if (startDate != null) {
            fromDateTime = startDate.atStartOfDay();
        } else {
            int daysToUse = (days != null) ? days : 21;
            fromDateTime = java.time.LocalDateTime.now().minusDays(daysToUse);
        }
        
        List<Object[]> results = thanhToanRepository.sumByDateRange(fromDateTime);
        
        java.util.List<String> labels = new java.util.ArrayList<>();
        java.util.List<BigDecimal> data = new java.util.ArrayList<>();
        
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM");
        for (Object[] row : results) {
            java.time.LocalDate date = (java.time.LocalDate) row[0];
            BigDecimal amount = (BigDecimal) row[1];
            labels.add(date.format(formatter));
            data.add(amount);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("labels", labels);
        result.put("data", data);
        if (startDate != null) {
            result.put("startDate", startDate.toString());
            result.put("days", java.time.temporal.ChronoUnit.DAYS.between(startDate, LocalDate.now()) + 1);
        } else {
            result.put("days", days != null ? days : 21);
        }
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }
    
    @GetMapping("/tai-tuc-rate")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTaiTucRate(
            @RequestParam(defaultValue = "6") int months) {
        
        LocalDate fromDate = LocalDate.now().minusMonths(months);
        List<Object[]> results = hopDongRepository.countRenewalByMonth(fromDate);
        
        Map<Integer, Map<String, Long>> monthlyData = new HashMap<>();
        for (Object[] row : results) {
            Integer month = (Integer) row[0];
            String status = row[1].toString();
            Long count = (Long) row[2];
            
            monthlyData.putIfAbsent(month, new HashMap<>());
            monthlyData.get(month).put(status, count);
        }
        
        List<String> labels = new java.util.ArrayList<>();
        List<Long> renewed = new java.util.ArrayList<>();
        List<Long> expired = new java.util.ArrayList<>();
        
        for (int i = 0; i < months; i++) {
            int month = LocalDate.now().minusMonths(months - i - 1).getMonthValue();
            labels.add("Tháng " + month);
            
            Map<String, Long> data = monthlyData.getOrDefault(month, new HashMap<>());
            renewed.add(data.getOrDefault("RENEWED", 0L));
            expired.add(data.getOrDefault("EXPIRED", 0L));
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("labels", labels);
        result.put("renewed", renewed);
        result.put("expired", expired);
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }
    
    @GetMapping("/top-risk-vehicles")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTopRiskVehicles(
            @RequestParam(defaultValue = "10") int limit) {
        
        List<HoSoThamDinh> hoSoList = hoSoThamDinhRepository.findTopRiskVehicles();
        
        List<Map<String, Object>> result = hoSoList.stream()
                .limit(limit)
                .map(hs -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("bienSo", hs.getXe() != null ? hs.getXe().getBienSo() : "N/A");
                    item.put("model", hs.getXe() != null ? 
                        hs.getXe().getHangXe() + " " + hs.getXe().getDongXe() : "N/A");
                    item.put("chuXe", hs.getXe() != null && hs.getXe().getKhachHang() != null ? 
                        hs.getXe().getKhachHang().getHoTen() : "N/A");
                    item.put("riskScore", hs.getRiskScore());
                    item.put("riskLevel", hs.getRiskLevel().name());
                    item.put("xeId", hs.getXe() != null ? hs.getXe().getId() : null);
                    return item;
                })
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }

}
