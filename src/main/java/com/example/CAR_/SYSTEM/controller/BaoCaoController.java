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
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false, defaultValue = "day") String groupBy)
    {
        Map<String, Object> result = new HashMap<>();

        // Nếu không có fromDate, mặc định 30 ngày trước
        if (fromDate == null) {
            fromDate = LocalDate.now().minusDays(30);
        }
        // Nếu không có toDate, mặc định hôm nay
        if (toDate == null) {
            toDate = LocalDate.now();
        }

        List<ThanhToan> thanhToans = thanhToanServiceImpl.getAll();

        // Lọc theo thời gian
        LocalDate finalFromDate = fromDate;
        LocalDate finalToDate = toDate;
        List<ThanhToan> filteredList = thanhToans.stream()
                .filter(tt -> !tt.getCreatedAt().toLocalDate().isBefore(finalFromDate) &&
                        !tt.getCreatedAt().toLocalDate().isAfter(finalToDate))
                .collect(Collectors.toList());

        // Tổng doanh thu
        BigDecimal tongDoanhThu = filteredList.stream()
                .map(ThanhToan::getSoTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Số giao dịch
        long soGiaoDich = filteredList.size();

        // Doanh thu trung bình
        BigDecimal doanhThuTrungBinh = soGiaoDich > 0 ? 
            tongDoanhThu.divide(BigDecimal.valueOf(soGiaoDich), 2, java.math.RoundingMode.HALF_UP) : 
            BigDecimal.ZERO;

        // Timeline data (theo ngày/tuần/tháng)
        Map<String, BigDecimal> timeline = new java.util.LinkedHashMap<>();
        java.time.format.DateTimeFormatter formatter;
        
        if ("month".equals(groupBy)) {
            formatter = java.time.format.DateTimeFormatter.ofPattern("MM/yyyy");
        } else if ("week".equals(groupBy)) {
            formatter = java.time.format.DateTimeFormatter.ofPattern("'W'w/yyyy");
        } else {
            formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM");
        }

        for (ThanhToan tt : filteredList) {
            String key = tt.getCreatedAt().toLocalDate().format(formatter);
            timeline.merge(key, tt.getSoTien(), BigDecimal::add);
        }

        // Doanh thu theo phương thức thanh toán
        Map<String, BigDecimal> theoPhuongThucThanhToan = filteredList.stream()
                .collect(Collectors.groupingBy(
                    tt -> tt.getPhuongThuc() != null ? tt.getPhuongThuc().name() : "KHAC",
                    Collectors.reducing(BigDecimal.ZERO, ThanhToan::getSoTien, BigDecimal::add)
                ));

        // Doanh thu theo loại (thu/hoàn phí)
        Map<String, BigDecimal> theoLoai = filteredList.stream()
                .collect(Collectors.groupingBy(
                    tt -> tt.getIsHoanPhi() ? "HOAN_PHI" : "THU_PHI",
                    Collectors.reducing(BigDecimal.ZERO, ThanhToan::getSoTien, BigDecimal::add)
                ));

        // KPI: Hôm nay, tuần này, tháng này
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(java.time.DayOfWeek.MONDAY);
        LocalDate startOfMonth = today.withDayOfMonth(1);

        BigDecimal doanhThuHomNay = thanhToans.stream()
                .filter(tt -> tt.getCreatedAt().toLocalDate().equals(today))
                .map(ThanhToan::getSoTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal doanhThuTuanNay = thanhToans.stream()
                .filter(tt -> !tt.getCreatedAt().toLocalDate().isBefore(startOfWeek))
                .map(ThanhToan::getSoTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal doanhThuThangNay = thanhToans.stream()
                .filter(tt -> !tt.getCreatedAt().toLocalDate().isBefore(startOfMonth))
                .map(ThanhToan::getSoTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Chi tiết giao dịch
        List<Map<String, Object>> chiTiet = filteredList.stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(100) // Giới hạn 100 giao dịch gần nhất
                .map(tt -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", tt.getId());
                    item.put("maTT", tt.getMaTT());
                    item.put("soTien", tt.getSoTien());
                    item.put("phuongThuc", tt.getPhuongThuc() != null ? tt.getPhuongThuc().name() : "");
                    item.put("isHoanPhi", tt.getIsHoanPhi());
                    item.put("ngayThanhToan", tt.getCreatedAt().toLocalDate());
                    item.put("hopDong", tt.getHopDong() != null ? tt.getHopDong().getMaHD() : "");
                    item.put("khachHang", tt.getHopDong() != null && tt.getHopDong().getHoSoThamDinh() != null && 
                            tt.getHopDong().getHoSoThamDinh().getKhachHang() != null ? 
                            tt.getHopDong().getHoSoThamDinh().getKhachHang().getHoTen() : "");
                    item.put("ghiChu", tt.getGhiChu());
                    return item;
                })
                .collect(Collectors.toList());

        result.put("tongDoanhThu", tongDoanhThu);
        result.put("soGiaoDich", soGiaoDich);
        result.put("doanhThuTrungBinh", doanhThuTrungBinh);
        result.put("doanhThuHomNay", doanhThuHomNay);
        result.put("doanhThuTuanNay", doanhThuTuanNay);
        result.put("doanhThuThangNay", doanhThuThangNay);
        result.put("timeline", timeline);
        result.put("theoPhuongThucThanhToan", theoPhuongThucThanhToan);
        result.put("theoLoai", theoLoai);
        result.put("chiTiet", chiTiet);
        result.put("fromDate", fromDate);
        result.put("toDate", toDate);
        result.put("groupBy", groupBy);

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
    
    @GetMapping("/khach-hang")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getKhachHang(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        Map<String, Object> result = new HashMap<>();

        // Lấy tất cả khách hàng
        List<com.example.CAR_.SYSTEM.model.KhachHang> allKhachHang = 
            hoSoThamDinhServiceImpl.getAll().stream()
                .map(HoSoThamDinh::getKhachHang)
                .filter(kh -> kh != null)
                .distinct()
                .collect(Collectors.toList());

        // Lọc theo thời gian nếu có
        List<com.example.CAR_.SYSTEM.model.KhachHang> filteredKhachHang = allKhachHang;
        if (fromDate != null || toDate != null) {
            filteredKhachHang = allKhachHang.stream()
                .filter(kh -> {
                    LocalDate createdDate = kh.getCreatedAt().toLocalDate();
                    return (fromDate == null || !createdDate.isBefore(fromDate)) &&
                           (toDate == null || !createdDate.isAfter(toDate));
                })
                .collect(Collectors.toList());
        }

        // Tổng số khách hàng
        long tongKhachHang = filteredKhachHang.size();

        // Phân loại theo giới tính
        Map<String, Long> theoGioiTinh = filteredKhachHang.stream()
            .collect(Collectors.groupingBy(
                kh -> kh.getGioiTinh() != null && !kh.getGioiTinh().isEmpty() ? 
                    kh.getGioiTinh() : "KHAC",
                Collectors.counting()
            ));

        // Phân loại theo nghề nghiệp
        Map<String, Long> theoNgheNghiep = filteredKhachHang.stream()
            .collect(Collectors.groupingBy(
                kh -> kh.getNgheNghiep() != null && !kh.getNgheNghiep().isEmpty() ? 
                    kh.getNgheNghiep() : "Chưa rõ",
                Collectors.counting()
            ));

        // Phân loại theo độ tuổi
        Map<String, Long> theoDoTuoi = filteredKhachHang.stream()
            .collect(Collectors.groupingBy(
                kh -> {
                    if (kh.getNgaySinh() == null) return "Chưa rõ";
                    int age = java.time.Period.between(kh.getNgaySinh(), LocalDate.now()).getYears();
                    if (age < 25) return "Dưới 25";
                    else if (age < 35) return "25-34";
                    else if (age < 45) return "35-44";
                    else if (age < 55) return "45-54";
                    else return "55+";
                },
                Collectors.counting()
            ));

        // Top khách hàng có nhiều xe nhất
        List<Map<String, Object>> topKhachHangNhieuXe = filteredKhachHang.stream()
            .map(kh -> {
                long soXe = kh.getDanhSachXe() != null ? kh.getDanhSachXe().size() : 0;
                Map<String, Object> item = new HashMap<>();
                item.put("maKH", kh.getMaKH());
                item.put("hoTen", kh.getHoTen());
                item.put("soXe", soXe);
                item.put("soDienThoai", kh.getSoDienThoai());
                item.put("email", kh.getEmail());
                return item;
            })
            .filter(item -> (Long)item.get("soXe") > 0)
            .sorted((a, b) -> Long.compare((Long)b.get("soXe"), (Long)a.get("soXe")))
            .limit(10)
            .collect(Collectors.toList());

        // Top khách hàng có hợp đồng giá trị cao
        List<Map<String, Object>> topKhachHangGiaTriCao = filteredKhachHang.stream()
            .map(kh -> {
                BigDecimal tongGiaTri = kh.getDanhSachHopDong() != null ?
                    kh.getDanhSachHopDong().stream()
                        .map(HopDong::getTongPhiBaoHiem)
                        .reduce(BigDecimal.ZERO, BigDecimal::add) :
                    BigDecimal.ZERO;
                
                long soHopDong = kh.getDanhSachHopDong() != null ? kh.getDanhSachHopDong().size() : 0;
                
                Map<String, Object> item = new HashMap<>();
                item.put("maKH", kh.getMaKH());
                item.put("hoTen", kh.getHoTen());
                item.put("tongGiaTri", tongGiaTri);
                item.put("soHopDong", soHopDong);
                item.put("soDienThoai", kh.getSoDienThoai());
                return item;
            })
            .filter(item -> ((BigDecimal)item.get("tongGiaTri")).compareTo(BigDecimal.ZERO) > 0)
            .sorted((a, b) -> ((BigDecimal)b.get("tongGiaTri")).compareTo((BigDecimal)a.get("tongGiaTri")))
            .limit(10)
            .collect(Collectors.toList());

        result.put("tongKhachHang", tongKhachHang);
        result.put("theoGioiTinh", theoGioiTinh);
        result.put("theoNgheNghiep", theoNgheNghiep);
        result.put("theoDoTuoi", theoDoTuoi);
        result.put("topKhachHangNhieuXe", topKhachHangNhieuXe);
        result.put("topKhachHangGiaTriCao", topKhachHangGiaTriCao);
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
