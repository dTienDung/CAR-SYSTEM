package com.example.CAR_.SYSTEM.service.impl;

import com.example.CAR_.SYSTEM.dto.request.CancelDTO;
import com.example.CAR_.SYSTEM.dto.request.HopDongDTO;
import com.example.CAR_.SYSTEM.dto.request.HopDongRenewDTO;
import com.example.CAR_.SYSTEM.model.*;
import com.example.CAR_.SYSTEM.model.enums.LoaiQuanHeHopDong;
import com.example.CAR_.SYSTEM.model.enums.TrangThaiHopDong;
import com.example.CAR_.SYSTEM.repository.*;
import com.example.CAR_.SYSTEM.service.HopDongService;
import com.example.CAR_.SYSTEM.util.CodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HopDongServiceImpl implements HopDongService {
    
    private final HopDongRepository hopDongRepository;
    private final HoSoThamDinhRepository hoSoThamDinhRepository;
    private final GoiBaoHiemRepository goiBaoHiemRepository;
    private final MaTranTinhPhiRepository maTranTinhPhiRepository;
    private final ThanhToanRepository thanhToanRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<HopDong> getAll() {
        return hopDongRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<HopDong> filter(TrangThaiHopDong trangThai, Long khachHangId, LocalDate fromDate, LocalDate toDate) {
        return hopDongRepository.filter(trangThai, khachHangId, fromDate, toDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public HopDong getById(Long id) {
        return hopDongRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hợp đồng với ID: " + id));
    }
    
    @Override
    @Transactional
    public HopDong create(HopDongDTO dto) {
        HoSoThamDinh hoSo = hoSoThamDinhRepository.findById(dto.getHoSoThamDinhId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ thẩm định"));
        
        if (hoSo.getRiskLevel() != com.example.CAR_.SYSTEM.model.enums.RiskLevel.CHAP_NHAN) {
            throw new RuntimeException("Chỉ có thể tạo hợp đồng từ hồ sơ đã CHẤP NHẬN");
        }
        
        Long nextSequence = hopDongRepository.count() + 1;
        String maHD = CodeGenerator.generateMaHD(nextSequence);
        
        // Tính phí bảo hiểm
        BigDecimal phiBaoHiem = calculatePhiBaoHiem(hoSo);
        
        // Tự động tính ngày hết hạn nếu không được cung cấp (mặc định 1 năm sau ngày hiệu lực)
        LocalDate ngayHetHan = dto.getNgayHetHan();
        if (ngayHetHan == null && dto.getNgayHieuLuc() != null) {
            ngayHetHan = dto.getNgayHieuLuc().plusYears(1);
        }
        
        HopDong hopDong = HopDong.builder()
                .maHD(maHD)
                .khachHang(hoSo.getKhachHang())
                .xe(hoSo.getXe())
                .hoSoThamDinh(hoSo)
                .goiBaoHiem(hoSo.getGoiBaoHiem())
                .ngayKy(dto.getNgayKy())
                .ngayHieuLuc(dto.getNgayHieuLuc())
                .ngayHetHan(ngayHetHan)
                .tongPhiBaoHiem(phiBaoHiem)
                .tongDaThanhToan(BigDecimal.ZERO)
                .trangThai(TrangThaiHopDong.DRAFT)
                .ghiChu(dto.getGhiChu())
                .build();
        
        return hopDongRepository.save(hopDong);
    }
    
    @Override
    @Transactional
    public HopDong update(Long id, HopDongDTO dto) {
        HopDong hopDong = getById(id);
        
        if (dto.getNgayKy() != null) hopDong.setNgayKy(dto.getNgayKy());
        if (dto.getNgayHieuLuc() != null) hopDong.setNgayHieuLuc(dto.getNgayHieuLuc());
        if (dto.getNgayHetHan() != null) hopDong.setNgayHetHan(dto.getNgayHetHan());
        if (dto.getGhiChu() != null) hopDong.setGhiChu(dto.getGhiChu());
        
        return hopDongRepository.save(hopDong);
    }
    
    @Override
    @Transactional
    public HopDong renew(Long id, HopDongRenewDTO dto) {
        HopDong hopDongCu = getById(id);
        
        if (hopDongCu.getTrangThai() != TrangThaiHopDong.ACTIVE && 
            hopDongCu.getTrangThai() != TrangThaiHopDong.EXPIRED) {
            throw new RuntimeException("Chỉ có thể tái tục hợp đồng đang hiệu lực hoặc đã hết hạn");
        }
        
        // Tự động tính ngày hết hạn nếu không được cung cấp (mặc định 1 năm sau ngày hiệu lực)
        LocalDate ngayHetHan = dto.getNgayHetHan();
        if (ngayHetHan == null && dto.getNgayHieuLuc() != null) {
            ngayHetHan = dto.getNgayHieuLuc().plusYears(1);
        }
        
        // Validate renewal dates
        validateRenewalDates(hopDongCu, dto, ngayHetHan);
        
        Long nextSequence = hopDongRepository.count() + 1;
        String maHD = CodeGenerator.generateMaHD(nextSequence);
        
        HopDong hopDongMoi = HopDong.builder()
                .maHD(maHD)
                .khachHang(hopDongCu.getKhachHang())
                .xe(hopDongCu.getXe())
                .hoSoThamDinh(hopDongCu.getHoSoThamDinh())
                .goiBaoHiem(hopDongCu.getGoiBaoHiem())
                .ngayKy(dto.getNgayKy())
                .ngayHieuLuc(dto.getNgayHieuLuc())
                .ngayHetHan(ngayHetHan)
                .tongPhiBaoHiem(hopDongCu.getTongPhiBaoHiem())
                .tongDaThanhToan(BigDecimal.ZERO)
                .trangThai(TrangThaiHopDong.DRAFT)
                .ghiChu(dto.getGhiChu())
                .hopDongCu(hopDongCu)
                .loaiQuanHe(LoaiQuanHeHopDong.TAI_TUC)
                .build();
        
        hopDongCu.setTrangThai(TrangThaiHopDong.RENEWED);
        hopDongRepository.save(hopDongCu);
        
        return hopDongRepository.save(hopDongMoi);
    }
    
    @Override
    @Transactional
    public HopDong cancel(Long id, CancelDTO dto) {
        HopDong hopDong = getById(id);
        
        if (hopDong.getTrangThai() == TrangThaiHopDong.CANCELLED) {
            throw new RuntimeException("Hợp đồng đã bị hủy");
        }
        
        hopDong.setTrangThai(TrangThaiHopDong.CANCELLED);
        if (dto.getLyDo() != null) {
            hopDong.setGhiChu(hopDong.getGhiChu() + "\nLý do hủy: " + dto.getLyDo());
        }
        
        // Nếu có hoàn phí, sẽ được xử lý trong ThanhToanService
        return hopDongRepository.save(hopDong);
    }
    
    @Override
    @Transactional
    public void delete(Long id) {
        HopDong hopDong = getById(id);
        hopDongRepository.delete(hopDong);
    }
    
    private BigDecimal calculatePhiBaoHiem(HoSoThamDinh hoSo) {
        var maTran = maTranTinhPhiRepository.findByDiemRuiRo(hoSo.getRiskScore())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ma trận tính phí cho điểm rủi ro: " + hoSo.getRiskScore()));
        
        return hoSo.getGoiBaoHiem().getPhiCoBan().multiply(maTran.getHeSoPhi());
    }
    
    private void validateRenewalDates(HopDong hopDongCu, HopDongRenewDTO dto, LocalDate ngayHetHan) {
        // Validate ngayKy must be after or equal to old contract's ngayHetHan
        if (dto.getNgayKy().isBefore(hopDongCu.getNgayHetHan())) {
            throw new RuntimeException("Ngày ký hợp đồng mới phải sau hoặc bằng ngày hết hạn của hợp đồng cũ (" + 
                hopDongCu.getNgayHetHan() + ")");
        }
        
        // Validate ngayHieuLuc must be after or equal to ngayKy
        if (dto.getNgayHieuLuc().isBefore(dto.getNgayKy())) {
            throw new RuntimeException("Ngày hiệu lực phải sau hoặc bằng ngày ký");
        }
        
        // Validate ngayHetHan must be after ngayHieuLuc
        if (ngayHetHan != null && (ngayHetHan.isBefore(dto.getNgayHieuLuc()) || 
            ngayHetHan.isEqual(dto.getNgayHieuLuc()))) {
            throw new RuntimeException("Ngày hết hạn phải sau ngày hiệu lực");
        }
        
        // Validate ngayHieuLuc should be after old contract's ngayHetHan (recommended)
        if (dto.getNgayHieuLuc().isBefore(hopDongCu.getNgayHetHan())) {
            throw new RuntimeException("Ngày hiệu lực hợp đồng mới nên sau ngày hết hạn của hợp đồng cũ (" + 
                hopDongCu.getNgayHetHan() + ")");
        }
        
        // Validate contract duration (at least 1 day, recommended at least 30 days)
        if (ngayHetHan != null) {
            long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(dto.getNgayHieuLuc(), ngayHetHan);
            if (daysBetween < 30) {
                throw new RuntimeException("Thời hạn hợp đồng phải ít nhất 30 ngày (hiện tại: " + daysBetween + " ngày)");
            }
        }
    }
}
