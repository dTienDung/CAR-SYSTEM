package com.example.CAR_.SYSTEM.config;

import com.example.CAR_.SYSTEM.constants.MaTranTinhPhiConstants;
import com.example.CAR_.SYSTEM.constants.TieuChiThamDinhConstants;
import com.example.CAR_.SYSTEM.model.*;
import com.example.CAR_.SYSTEM.model.enums.*;
import com.example.CAR_.SYSTEM.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Tự động seed dữ liệu từ constants vào database khi app khởi động
 * Đảm bảo dữ liệu luôn khớp với constants (xóa và insert lại)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitializer implements ApplicationRunner {
    
    private final TieuChiThamDinhRepository tieuChiThamDinhRepository;
    private final MaTranTinhPhiRepository maTranTinhPhiRepository;
    private final KhachHangRepository khachHangRepository;
    private final XeRepository xeRepository;
    private final GoiBaoHiemRepository goiBaoHiemRepository;
    private final HoSoThamDinhRepository hoSoThamDinhRepository;
    private final HopDongRepository hopDongRepository;
    private final ThanhToanRepository thanhToanRepository;
    private final UserRepository userRepository;
    
    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        log.info("=== BẮT ĐẦU KHỞI TẠO DỮ LIỆU TỪ CONSTANTS ===");
        
        // Seed Tiêu chí thẩm định
        seedTieuChiThamDinh();
        
        // Seed Ma trận tính phí
        seedMaTranTinhPhi();
        
        // Seed dữ liệu mẫu (chỉ khi chưa có dữ liệu)
        if (thanhToanRepository.count() == 0) {
            log.info("=== BẮT ĐẦU SEED DỮ LIỆU MẪU ===");
            seedSampleData();
            log.info("=== HOÀN TẤT SEED DỮ LIỆU MẪU ===");
        }
        
        log.info("=== HOÀN TẤT KHỞI TẠO DỮ LIỆU ===");
    }
    
    /**
     * Seed Tiêu chí thẩm định từ constants
     */
    private void seedTieuChiThamDinh() {
        log.info("Đang seed Tiêu chí thẩm định...");
        
        // Xóa tất cả tiêu chí cũ (nếu có)
        long oldCount = tieuChiThamDinhRepository.count();
        if (oldCount > 0) {
            log.info("Tìm thấy {} tiêu chí cũ, đang xóa để đồng bộ với constants...", oldCount);
            tieuChiThamDinhRepository.deleteAll();
            tieuChiThamDinhRepository.flush(); // Force commit DELETE
        }
        
        // Insert từ constants
        for (TieuChiThamDinhConstants.TieuChiData data : TieuChiThamDinhConstants.TIEU_CHI_LIST) {
            TieuChiThamDinh entity = data.toEntity();
            entity.setId(null); // Đảm bảo Hibernate sẽ persist thay vì merge
            tieuChiThamDinhRepository.save(entity);
            log.info("  ✓ Đã seed: {} - {} ({}đ)", entity.getMaTieuChi(), entity.getTenTieuChi(), entity.getDiemToiDa());
        }
        
        log.info("✓ Hoàn tất seed {} tiêu chí thẩm định (Tổng: {}đ)", 
                 TieuChiThamDinhConstants.TIEU_CHI_LIST.size(),
                 TieuChiThamDinhConstants.TONG_DIEM_TOI_DA);
    }
    
    /**
     * Seed Ma trận tính phí từ constants
     */
    private void seedMaTranTinhPhi() {
        log.info("Đang seed Ma trận tính phí...");
        
        // Xóa tất cả ma trận cũ (nếu có)
        long oldCount = maTranTinhPhiRepository.count();
        if (oldCount > 0) {
            log.info("Tìm thấy {} ma trận cũ, đang xóa để đồng bộ với constants...", oldCount);
            maTranTinhPhiRepository.deleteAll();
            maTranTinhPhiRepository.flush(); // Force commit DELETE
        }
        
        // Insert từ constants
        for (MaTranTinhPhiConstants.MaTranData data : MaTranTinhPhiConstants.MA_TRAN_LIST) {
            MaTranTinhPhi entity = data.toEntity();
            entity.setId(null); // Đảm bảo Hibernate sẽ persist thay vì merge
            maTranTinhPhiRepository.save(entity);
            log.info("  ✓ Đã seed: {}-{} điểm → hệ số {} ({})", 
                     entity.getDiemRuiRoTu(), 
                     entity.getDiemRuiRoDen(), 
                     entity.getHeSoPhi(),
                     entity.getMoTa());
        }
        
        log.info("✓ Hoàn tất seed {} ma trận tính phí", MaTranTinhPhiConstants.MA_TRAN_LIST.size());
    }
    
    /**
     * Seed dữ liệu mẫu cho báo cáo 2024-2025
     */
    private void seedSampleData() {
        Random random = new Random();
        
        // Tạo gói bảo hiểm mẫu
        List<GoiBaoHiem> goiList = new ArrayList<>();
        String[] tenGoi = {"Gói Cơ bản", "Gói Tiêu chuẩn", "Gói Nâng cao", "Gói Vàng"};
        for (String ten : tenGoi) {
            GoiBaoHiem goi = new GoiBaoHiem();
            goi.setTenGoi(ten);
            goi.setMoTa("Gói bảo hiểm " + ten);
            goi.setPhiCoBan(new BigDecimal(5000000 + random.nextInt(5000000)));
            goiList.add(goiBaoHiemRepository.save(goi));
        }
        log.info("✓ Đã tạo {} gói bảo hiểm", goiList.size());
        
        // Tạo khách hàng mẫu
        List<KhachHang> khachHangList = new ArrayList<>();
        String[] hoTen = {"Nguyễn Văn An", "Trần Thị Bình", "Lê Văn Cường", "Phạm Thị Dung", 
                         "Hoàng Văn Em", "Vũ Thị Phượng", "Đỗ Văn Giang", "Bùi Thị Hoa",
                         "Đinh Văn Khoa", "Ngô Thị Lan"};
        for (int i = 0; i < hoTen.length; i++) {
            KhachHang kh = new KhachHang();
            kh.setHoTen(hoTen[i]);
            kh.setSoDienThoai("09" + String.format("%08d", 10000000 + i));
            kh.setEmail("khach" + i + "@example.com");
            kh.setDiaChi("Địa chỉ " + hoTen[i]);
            kh.setNgaySinh(LocalDate.of(1980 + random.nextInt(25), 1 + random.nextInt(12), 1 + random.nextInt(28)));
            kh.setGioiTinh(i % 2 == 0 ? "Nam" : "Nữ");
            kh.setNgheNghiep(i % 3 == 0 ? "Kỹ sư" : i % 3 == 1 ? "Giáo viên" : "Kinh doanh");
            khachHangList.add(khachHangRepository.save(kh));
        }
        log.info("✓ Đã tạo {} khách hàng", khachHangList.size());
        
        // Tạo xe mẫu
        List<Xe> xeList = new ArrayList<>();
        String[] hangXe = {"Toyota", "Honda", "Mazda", "Hyundai", "Kia"};
        String[] dongXe = {"Vios", "City", "3", "Accent", "Morning"};
        for (int i = 0; i < khachHangList.size(); i++) {
            Xe xe = new Xe();
            xe.setBienSo("30A-" + String.format("%05d", 10000 + i));
            xe.setHangXe(hangXe[i % hangXe.length]);
            xe.setDongXe(dongXe[i % dongXe.length]);
            xe.setNamSanXuat(2015 + random.nextInt(9));
            xe.setGiaTriXe(new BigDecimal(300000000 + random.nextInt(500000000)));
            xe.setKhachHang(khachHangList.get(i));
            xeList.add(xeRepository.save(xe));
        }
        log.info("✓ Đã tạo {} xe", xeList.size());
        
        // Tạo hồ sơ thẩm định và hợp đồng với thanh toán cho 2024-2025
        int paymentCount = 0;
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.now();
        
        // Tạo 50 hợp đồng phân bố từ 2024 đến 2025
        for (int i = 0; i < 50; i++) {
            // Random ngày trong khoảng 2024-2025
            long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
            LocalDate ngayTao = startDate.plusDays(random.nextInt((int)daysBetween + 1));
            
            KhachHang kh = khachHangList.get(random.nextInt(khachHangList.size()));
            Xe xe = xeList.stream().filter(x -> x.getKhachHang().equals(kh)).findFirst()
                    .orElse(xeList.get(random.nextInt(xeList.size())));
            GoiBaoHiem goi = goiList.get(random.nextInt(goiList.size()));
            
            // Tạo hồ sơ thẩm định
            HoSoThamDinh hs = new HoSoThamDinh();
            hs.setKhachHang(kh);
            hs.setXe(xe);
            hs.setGoiBaoHiem(goi);
            hs.setRiskScore(300 + random.nextInt(500));
            hs.setRiskLevel(hs.getRiskScore() < 400 ? RiskLevel.CHAP_NHAN : 
                           hs.getRiskScore() < 600 ? RiskLevel.XEM_XET : RiskLevel.TU_CHOI);
            hs.setTrangThai(TrangThaiHoSo.CHAP_NHAN);
            BigDecimal phiBH = goi.getPhiCoBan().multiply(
                    BigDecimal.valueOf(1.0 + (hs.getRiskScore() - 400) * 0.001));
            hs.setPhiBaoHiem(phiBH);
            hs.setCreatedAt(ngayTao.atStartOfDay());
            hs = hoSoThamDinhRepository.save(hs);
            
            // Tạo hợp đồng
            HopDong hd = new HopDong();
            hd.setHoSoThamDinh(hs);
            hd.setKhachHang(kh);
            hd.setXe(xe);
            hd.setGoiBaoHiem(goi);
            hd.setNgayKy(ngayTao);
            hd.setNgayHieuLuc(ngayTao.plusDays(1));
            hd.setNgayHetHan(ngayTao.plusYears(1));
            hd.setTongPhiBaoHiem(phiBH);
            hd.setTongDaThanhToan(BigDecimal.ZERO);
            hd.setTrangThai(TrangThaiHopDong.ACTIVE);
            hd.setLoaiQuanHe(random.nextBoolean() ? LoaiQuanHeHopDong.TAI_TUC : LoaiQuanHeHopDong.CHUYEN_NHUONG);
            hd.setCreatedAt(ngayTao.atStartOfDay());
            hd = hopDongRepository.save(hd);
            
            // Tạo 1-3 giao dịch thanh toán cho mỗi hợp đồng
            int soGiaoDich = 1 + random.nextInt(3);
            BigDecimal tongDaThanhToan = BigDecimal.ZERO;
            
            for (int j = 0; j < soGiaoDich; j++) {
                ThanhToan tt = new ThanhToan();
                tt.setHopDong(hd);
                
                // Số tiền thanh toán
                BigDecimal soTien;
                if (j == soGiaoDich - 1) {
                    // Thanh toán cuối cùng = tổng phí - đã thanh toán
                    soTien = phiBH.subtract(tongDaThanhToan);
                } else {
                    // Thanh toán một phần
                    soTien = phiBH.divide(BigDecimal.valueOf(soGiaoDich), 0, java.math.RoundingMode.HALF_UP);
                }
                
                tt.setSoTien(soTien);
                tongDaThanhToan = tongDaThanhToan.add(soTien);
                
                // Ngày thanh toán sau ngày ký hợp đồng
                LocalDate ngayThanhToan = ngayTao.plusDays(random.nextInt(30) + j * 30);
                tt.setCreatedAt(ngayThanhToan.atTime(9 + random.nextInt(8), random.nextInt(60)));
                
                // Phương thức thanh toán
                PhuongThucThanhToan[] methods = PhuongThucThanhToan.values();
                tt.setPhuongThuc(methods[random.nextInt(methods.length)]);
                
                // 5% là hoàn phí
                tt.setIsHoanPhi(random.nextInt(100) < 5);
                
                tt.setGhiChu(tt.getIsHoanPhi() ? "Hoàn phí do hủy hợp đồng" : "Thanh toán phí bảo hiểm");
                
                thanhToanRepository.save(tt);
                paymentCount++;
            }
            
            // Cập nhật tổng đã thanh toán cho hợp đồng
            hd.setTongDaThanhToan(tongDaThanhToan);
            hopDongRepository.save(hd);
        }
        
        log.info("✓ Đã tạo 50 hồ sơ thẩm định, 50 hợp đồng và {} giao dịch thanh toán (2024-2025)", paymentCount);
    }
}
