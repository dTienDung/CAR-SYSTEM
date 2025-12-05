package com.example.CAR_.SYSTEM.service.impl;

import com.example.CAR_.SYSTEM.dto.request.HoSoThamDinhDTO;
import com.example.CAR_.SYSTEM.dto.response.RiskScoreDTO;
import com.example.CAR_.SYSTEM.model.*;
import com.example.CAR_.SYSTEM.model.enums.RiskLevel;
import com.example.CAR_.SYSTEM.model.enums.TrangThaiHoSo;
import com.example.CAR_.SYSTEM.repository.*;
import com.example.CAR_.SYSTEM.service.HoSoThamDinhService;
import com.example.CAR_.SYSTEM.util.CodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HoSoThamDinhServiceImpl implements HoSoThamDinhService {

    private final HoSoThamDinhRepository hoSoThamDinhRepository;
    private final KhachHangRepository khachHangRepository;
    private final XeRepository xeRepository;
    private final GoiBaoHiemRepository goiBaoHiemRepository;
    private final TieuChiThamDinhRepository tieuChiThamDinhRepository;
    private final ChiTietThamDinhRepository chiTietThamDinhRepository;
    private final LichSuTaiNanRepository lichSuTaiNanRepository;
    private final MaTranTinhPhiRepository maTranTinhPhiRepository;


    @Override
    public List<HoSoThamDinh> getAllWithDetails() {
        return hoSoThamDinhRepository.findAllWithDetails();
    }
    @Override
    @Transactional(readOnly = true)
    public List<HoSoThamDinh> getAll() {
        return hoSoThamDinhRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<HoSoThamDinh> filter(TrangThaiHoSo trangThai, RiskLevel riskLevel) {
        return hoSoThamDinhRepository.filter(trangThai, riskLevel);
    }

    @Override
    @Transactional(readOnly = true)
    public HoSoThamDinh getById(Long id) {
        return hoSoThamDinhRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ thẩm định với ID: " + id));
    }

    @Override
    @Transactional
    public HoSoThamDinh create(HoSoThamDinhDTO dto) {
        var khachHang = khachHangRepository.findById(dto.getKhachHangId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        var xe = xeRepository.findById(dto.getXeId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe"));
        var goiBaoHiem = goiBaoHiemRepository.findById(dto.getGoiBaoHiemId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy gói bảo hiểm"));

        Long nextSequence = hoSoThamDinhRepository.count() + 1;
        String maHS = CodeGenerator.generateMaHS(nextSequence);

        HoSoThamDinh hoSo = HoSoThamDinh.builder()
                .maHS(maHS)
                .khachHang(khachHang)
                .xe(xe)
                .goiBaoHiem(goiBaoHiem)
                .trangThai(TrangThaiHoSo.MOI_TAO)
                .riskScore(0)
                .riskLevel(RiskLevel.XEM_XET)
                .ghiChu(dto.getGhiChu())
                .build();

        if (dto.getNguoiThamDinhId() != null) {
            // Set nguoiThamDinh if provided
        }

        hoSo = hoSoThamDinhRepository.save(hoSo);

        // Tính điểm rủi ro từ danh sách điểm tiêu chí (nếu có) hoặc tự động tính
        if (dto.getChiTietThamDinh() != null && !dto.getChiTietThamDinh().isEmpty()) {
            calculateAndSaveRiskScoreFromDTO(hoSo, dto.getChiTietThamDinh());
        } else {
            calculateAndSaveRiskScore(hoSo);
        }

        return hoSoThamDinhRepository.findById(hoSo.getId()).orElse(hoSo);
    }

    @Override
    @Transactional
    public HoSoThamDinh update(Long id, HoSoThamDinhDTO dto) {
        HoSoThamDinh hoSo = getById(id);

        if (dto.getKhachHangId() != null) {
            var khachHang = khachHangRepository.findById(dto.getKhachHangId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
            hoSo.setKhachHang(khachHang);
        }
        if (dto.getXeId() != null) {
            var xe = xeRepository.findById(dto.getXeId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy xe"));
            hoSo.setXe(xe);
        }
        if (dto.getGoiBaoHiemId() != null) {
            var goiBaoHiem = goiBaoHiemRepository.findById(dto.getGoiBaoHiemId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy gói bảo hiểm"));
            hoSo.setGoiBaoHiem(goiBaoHiem);
        }
        if (dto.getGhiChu() != null) {
            hoSo.setGhiChu(dto.getGhiChu());
        }

        // Tính lại điểm rủi ro nếu có thay đổi
        if (dto.getChiTietThamDinh() != null && !dto.getChiTietThamDinh().isEmpty()) {
            calculateAndSaveRiskScoreFromDTO(hoSo, dto.getChiTietThamDinh());
        } else {
            calculateAndSaveRiskScore(hoSo);
        }

        return hoSoThamDinhRepository.save(hoSo);
    }

    @Override
    @Transactional
    public RiskScoreDTO calculateRiskScore(Long id) {
        HoSoThamDinh hoSo = getById(id);
        calculateAndSaveRiskScore(hoSo);

        hoSo = hoSoThamDinhRepository.findById(id).orElse(hoSo);

        return RiskScoreDTO.builder()
                .riskScore(hoSo.getRiskScore())
                .riskLevel(hoSo.getRiskLevel())
                .moTa(getRiskLevelDescription(hoSo.getRiskLevel()))
                .build();
    }

    private void calculateAndSaveRiskScoreFromDTO(HoSoThamDinh hoSo,
                                                  List<com.example.CAR_.SYSTEM.dto.request.ChiTietThamDinhRequestDTO> chiTietDTOs) {
        // Xóa chi tiết cũ
        chiTietThamDinhRepository.deleteByHoSoThamDinhId(hoSo.getId());

        int totalScore = 0;

        // Lưu điểm từ DTO
        for (com.example.CAR_.SYSTEM.dto.request.ChiTietThamDinhRequestDTO dto : chiTietDTOs) {
            TieuChiThamDinh tieuChi = tieuChiThamDinhRepository.findById(dto.getTieuChiId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy tiêu chí với ID: " + dto.getTieuChiId()));

            // Validate điểm không vượt quá điểm tối đa
            int diem = Math.min(dto.getDiem(), tieuChi.getDiemToiDa());

            ChiTietThamDinh chiTiet = ChiTietThamDinh.builder()
                    .hoSoThamDinh(hoSo)
                    .tieuChi(tieuChi)
                    .diem(diem)
                    .ghiChu(dto.getGhiChu())
                    .build();

            chiTietThamDinhRepository.save(chiTiet);
            totalScore += diem;
        }

        // Xác định RiskLevel theo business rules
        RiskLevel riskLevel = determineRiskLevel(totalScore);

        hoSo.setRiskScore(totalScore);
        hoSo.setRiskLevel(riskLevel);

        // Tính phí bảo hiểm
        calculatePhiBaoHiem(hoSo);

        // Cập nhật trạng thái
        if (hoSo.getTrangThai() == TrangThaiHoSo.MOI_TAO) {
            hoSo.setTrangThai(TrangThaiHoSo.DANG_THAM_DINH);
        }

        hoSoThamDinhRepository.save(hoSo);
    }

    private void calculateAndSaveRiskScore(HoSoThamDinh hoSo) {
        // HOTFIX: Lưu điểm manual cũ TRƯỚC KHI xóa
        List<ChiTietThamDinh> oldDetails = chiTietThamDinhRepository.findByHoSoThamDinhId(hoSo.getId());
        java.util.Map<Long, Integer> oldScores = new java.util.HashMap<>();
        for (ChiTietThamDinh detail : oldDetails) {
            oldScores.put(detail.getTieuChi().getId(), detail.getDiem());
        }

        // Xóa chi tiết cũ
        chiTietThamDinhRepository.deleteByHoSoThamDinhId(hoSo.getId());

        // Lấy danh sách tiêu chí
        List<TieuChiThamDinh> tieuChis = tieuChiThamDinhRepository.findByActiveTrueOrderByThuTu();

        int totalScore = 0;

        // Tính điểm cho từng tiêu chí
        for (TieuChiThamDinh tieuChi : tieuChis) {
            // Thử tính tự động
            int autoDiem = calculateDiemForTieuChi(tieuChi, hoSo);
            
            // HOTFIX: Nếu auto = 0 (chưa có logic), giữ điểm manual cũ
            int diem = (autoDiem > 0) ? autoDiem : oldScores.getOrDefault(tieuChi.getId(), 0);

            ChiTietThamDinh chiTiet = ChiTietThamDinh.builder()
                    .hoSoThamDinh(hoSo)
                    .tieuChi(tieuChi)
                    .diem(diem)
                    .ghiChu(autoDiem > 0 ? "Tự động" : null)
                    .build();

            chiTietThamDinhRepository.save(chiTiet);
            totalScore += diem;
        }

        // Xác định RiskLevel
        RiskLevel riskLevel = determineRiskLevel(totalScore);

        hoSo.setRiskScore(totalScore);
        hoSo.setRiskLevel(riskLevel);

        // Tính phí bảo hiểm
        calculatePhiBaoHiem(hoSo);

        // Cập nhật trạng thái
        if (hoSo.getTrangThai() == TrangThaiHoSo.MOI_TAO) {
            hoSo.setTrangThai(TrangThaiHoSo.DANG_THAM_DINH);
        }

        hoSoThamDinhRepository.save(hoSo);
    }

    /**
     * Xác định mức độ rủi ro dựa trên TỶ LỆ PHẦN TRĂM
     * → Linh hoạt khi thêm/bớt tiêu chí
     */
    private RiskLevel determineRiskLevel(int totalScore) {
        // Tính tổng điểm tối đa từ tất cả tiêu chí active
        int maxPossibleScore = calculateMaxPossibleScore();
        
        // Tránh chia cho 0
        if (maxPossibleScore == 0) {
            return RiskLevel.XEM_XET; // Mặc định nếu chưa có tiêu chí
        }
        
        // Tính tỷ lệ phần trăm
        double scorePercentage = (double) totalScore / maxPossibleScore * 100;
        
        // Quyết định dựa trên tỷ lệ %
        // CHẤP NHẬN: ≤25% | XEM XÉT: 26-50% | TỪ CHỐI: >50%
        if (scorePercentage <= 25) {
            return RiskLevel.CHAP_NHAN;        // Rủi ro thấp
        } else if (scorePercentage <= 50) {
            return RiskLevel.XEM_XET;          // Rủi ro trung bình
        } else {
            return RiskLevel.TU_CHOI;          // Rủi ro cao
        }
    }
    
    /**
     * Tính tổng điểm tối đa có thể đạt được từ tất cả tiêu chí active
     */
    private int calculateMaxPossibleScore() {
        List<TieuChiThamDinh> tieuChis = tieuChiThamDinhRepository.findByActiveTrueOrderByThuTu();
        return tieuChis.stream()
                .mapToInt(TieuChiThamDinh::getDiemToiDa)
                .sum();
    }

    /**
     * Tính phí bảo hiểm dựa trên TỶ LỆ % điểm rủi ro
     * → Linh hoạt với mọi tổng điểm
     */
    private void calculatePhiBaoHiem(HoSoThamDinh hoSo) {
        try {
            int maxScore = calculateMaxPossibleScore();
            if (maxScore == 0) {
                hoSo.setPhiBaoHiem(null);
                return;
            }
            
            // Tính % điểm rủi ro
            double scorePercentage = (double) hoSo.getRiskScore() / maxScore * 100;
            
            // Tìm ma trận theo % (sử dụng điểm rủi ro như % để tìm)
            // Ví dụ: scorePercentage = 30% → tìm ma trận có khoảng chứa 30
            int percentAsInt = (int) Math.round(scorePercentage);
            var maTran = maTranTinhPhiRepository.findByDiemRuiRo(percentAsInt);
            
            if (maTran.isPresent()) {
                BigDecimal phiBaoHiem = hoSo.getGoiBaoHiem().getPhiCoBan()
                        .multiply(maTran.get().getHeSoPhi());
                hoSo.setPhiBaoHiem(phiBaoHiem);
            } else {
                // Fallback: Tính hệ số dựa trên %
                BigDecimal heSo = calculateHeSoPhiByPercentage(scorePercentage);
                BigDecimal phiBaoHiem = hoSo.getGoiBaoHiem().getPhiCoBan().multiply(heSo);
                hoSo.setPhiBaoHiem(phiBaoHiem);
            }
        } catch (Exception e) {
            // Nếu có lỗi, để null
            hoSo.setPhiBaoHiem(null);
        }
    }
    
    /**
     * Tính hệ số phí dựa trên % điểm rủi ro (fallback logic)
     * Không cần ma trận trong DB, tính trực tiếp
     */
    private BigDecimal calculateHeSoPhiByPercentage(double scorePercentage) {
        // Logic tính hệ số dựa trên %
        if (scorePercentage <= 10) {
            return BigDecimal.valueOf(0.8);  // Giảm 20%
        } else if (scorePercentage <= 25) {
            return BigDecimal.valueOf(1.0);  // Phí chuẩn
        } else if (scorePercentage <= 40) {
            return BigDecimal.valueOf(1.2);  // Tăng 20%
        } else if (scorePercentage <= 50) {
            return BigDecimal.valueOf(1.5);  // Tăng 50%
        } else if (scorePercentage <= 65) {
            return BigDecimal.valueOf(1.8);  // Tăng 80%
        } else if (scorePercentage <= 80) {
            return BigDecimal.valueOf(2.2);  // Tăng 120%
        } else {
            return BigDecimal.valueOf(2.5);  // Tăng 150%
        }
    }

    /**
     * Tính điểm tự động cho từng tiêu chí dựa trên dữ liệu thực tế
     * Hỗ trợ nhiều tiêu chí: tuổi xe, lịch sử tai nạn, tuổi lái xe, v.v.
     */
    private int calculateDiemForTieuChi(TieuChiThamDinh tieuChi, HoSoThamDinh hoSo) {
        String maTieuChi = tieuChi.getMaTieuChi().toUpperCase();
        int diemToiDa = tieuChi.getDiemToiDa();
        
        try {
            // 1. LỊCH SỬ TAI NẠN
            if (maTieuChi.contains("TAI_NAN") || maTieuChi.contains("ACCIDENT")) {
                return calculateDiemTaiNan(hoSo, diemToiDa);
            }
            
            // 2. TUỔI XE (Nam sản xuất)
            if (maTieuChi.contains("TUOI_XE") || maTieuChi.contains("NAM_SAN_XUAT") || 
                maTieuChi.contains("AGE_VEHICLE")) {
                return calculateDiemTuoiXe(hoSo, diemToiDa);
            }
            
            // 3. TUỔI LÁI XE (Tuổi khách hàng)
            if (maTieuChi.contains("TUOI_LAI") || maTieuChi.contains("TUOI_KH") || 
                maTieuChi.contains("AGE_DRIVER")) {
                return calculateDiemTuoiLaiXe(hoSo, diemToiDa);
            }
            
            // 4. GIÁ TRỊ XE
            if (maTieuChi.contains("GIA_TRI") || maTieuChi.contains("VALUE")) {
                return calculateDiemGiaTriXe(hoSo, diemToiDa);
            }
            
            // 5. MỤC ĐÍCH SỬ DỤNG
            if (maTieuChi.contains("MUC_DICH") || maTieuChi.contains("PURPOSE")) {
                return calculateDiemMucDichSuDung(hoSo, diemToiDa);
            }
            
            // 6. NGHỀ NGHIỆP
            if (maTieuChi.contains("NGHE_NGHIEP") || maTieuChi.contains("OCCUPATION")) {
                return calculateDiemNgheNghiep(hoSo, diemToiDa);
            }
            
            // 7. GIỚI TÍNH
            if (maTieuChi.contains("GIOI_TINH") || maTieuChi.contains("GENDER")) {
                return calculateDiemGioiTinh(hoSo, diemToiDa);
            }
            
            // 8. LOẠI XE (Hãng xe)
            if (maTieuChi.contains("LOAI_XE") || maTieuChi.contains("HANG_XE") || 
                maTieuChi.contains("BRAND")) {
                return calculateDiemLoaiXe(hoSo, diemToiDa);
            }
            
        } catch (Exception e) {
            // Nếu có lỗi, trả về 0
            System.err.println("Lỗi tính điểm tiêu chí " + maTieuChi + ": " + e.getMessage());
        }
        
        // Mặc định trả về 0 điểm (chưa có logic tự động)
        return 0;
    }
    
    // ========== CÁC HÀM TÍNH ĐIỂM CHO TỪNG TIÊU CHÍ ==========
    
    /**
     * 1. Tính điểm lịch sử tai nạn
     * Càng nhiều tai nạn → điểm càng cao
     */
    private int calculateDiemTaiNan(HoSoThamDinh hoSo, int diemToiDa) {
        long soLanTaiNan = lichSuTaiNanRepository.findByXeId(hoSo.getXe().getId()).size();
        
        // 0 tai nạn = 0 điểm
        // 1 tai nạn = 30% điểm tối đa
        // 2 tai nạn = 60% điểm tối đa
        // 3+ tai nạn = 100% điểm tối đa
        if (soLanTaiNan == 0) return 0;
        if (soLanTaiNan == 1) return (int) (diemToiDa * 0.3);
        if (soLanTaiNan == 2) return (int) (diemToiDa * 0.6);
        return diemToiDa; // 3+ tai nạn
    }
    
    /**
     * 2. Tính điểm tuổi xe
     * Xe càng cũ → rủi ro càng cao
     */
    private int calculateDiemTuoiXe(HoSoThamDinh hoSo, int diemToiDa) {
        int namSanXuat = hoSo.getXe().getNamSanXuat();
        int namHienTai = LocalDate.now().getYear();
        int tuoiXe = namHienTai - namSanXuat;
        
        // 0-3 năm = 0 điểm (xe mới)
        // 4-7 năm = 20% điểm
        // 8-12 năm = 50% điểm
        // 13-20 năm = 80% điểm
        // >20 năm = 100% điểm (xe quá cũ)
        if (tuoiXe <= 3) return 0;
        if (tuoiXe <= 7) return (int) (diemToiDa * 0.2);
        if (tuoiXe <= 12) return (int) (diemToiDa * 0.5);
        if (tuoiXe <= 20) return (int) (diemToiDa * 0.8);
        return diemToiDa;
    }
    
    /**
     * 3. Tính điểm tuổi lái xe (tuổi khách hàng)
     * Quá trẻ hoặc quá già → rủi ro cao
     */
    private int calculateDiemTuoiLaiXe(HoSoThamDinh hoSo, int diemToiDa) {
        if (hoSo.getKhachHang().getNgaySinh() == null) return 0;
        
        int namSinh = hoSo.getKhachHang().getNgaySinh().getYear();
        int namHienTai = LocalDate.now().getYear();
        int tuoi = namHienTai - namSinh;
        
        // 18-25 tuổi = 60% điểm (thiếu kinh nghiệm)
        // 26-50 tuổi = 0 điểm (độ tuổi an toàn)
        // 51-65 tuổi = 30% điểm
        // >65 tuổi = 70% điểm (phản xạ kém)
        if (tuoi < 18) return diemToiDa; // Chưa đủ tuổi lái xe
        if (tuoi <= 25) return (int) (diemToiDa * 0.6);
        if (tuoi <= 50) return 0;
        if (tuoi <= 65) return (int) (diemToiDa * 0.3);
        return (int) (diemToiDa * 0.7);
    }
    
    /**
     * 4. Tính điểm giá trị xe
     * Xe càng đắt → rủi ro bồi thường càng cao
     */
    private int calculateDiemGiaTriXe(HoSoThamDinh hoSo, int diemToiDa) {
        BigDecimal giaTriXe = hoSo.getXe().getGiaTriXe();
        if (giaTriXe == null) return 0;
        
        long giaTri = giaTriXe.longValue();
        
        // <300 triệu = 0 điểm
        // 300-500 triệu = 20% điểm
        // 500-800 triệu = 40% điểm
        // 800-1.5 tỷ = 60% điểm
        // >1.5 tỷ = 100% điểm
        if (giaTri < 300_000_000) return 0;
        if (giaTri < 500_000_000) return (int) (diemToiDa * 0.2);
        if (giaTri < 800_000_000) return (int) (diemToiDa * 0.4);
        if (giaTri < 1_500_000_000) return (int) (diemToiDa * 0.6);
        return diemToiDa;
    }
    
    /**
     * 5. Tính điểm mục đích sử dụng
     * Kinh doanh/taxi → rủi ro cao hơn cá nhân
     */
    private int calculateDiemMucDichSuDung(HoSoThamDinh hoSo, int diemToiDa) {
        String mucDich = hoSo.getXe().getMucDichSuDung();
        if (mucDich == null) return 0;
        
        mucDich = mucDich.toLowerCase();
        
        // Cá nhân = 0 điểm
        // Kinh doanh/Taxi/Uber = 80% điểm
        // Vận tải = 100% điểm
        if (mucDich.contains("cá nhân") || mucDich.contains("ca nhan")) return 0;
        if (mucDich.contains("taxi") || mucDich.contains("uber") || 
            mucDich.contains("grab") || mucDich.contains("kinh doanh")) {
            return (int) (diemToiDa * 0.8);
        }
        if (mucDich.contains("vận tải") || mucDich.contains("van tai")) {
            return diemToiDa;
        }
        
        return (int) (diemToiDa * 0.3); // Mặc định
    }
    
    /**
     * 6. Tính điểm nghề nghiệp
     * Nghề nguy hiểm → rủi ro cao
     */
    private int calculateDiemNgheNghiep(HoSoThamDinh hoSo, int diemToiDa) {
        String ngheNghiep = hoSo.getKhachHang().getNgheNghiep();
        if (ngheNghiep == null) return 0;
        
        ngheNghiep = ngheNghiep.toLowerCase();
        
        // Văn phòng/Giáo viên = 0 điểm
        // Tài xế/Shipper = 70% điểm
        // Công nhân/Xây dựng = 50% điểm
        if (ngheNghiep.contains("văn phòng") || ngheNghiep.contains("giáo viên") ||
            ngheNghiep.contains("nhân viên") || ngheNghiep.contains("kế toán")) {
            return 0;
        }
        if (ngheNghiep.contains("tài xế") || ngheNghiep.contains("lái xe") ||
            ngheNghiep.contains("shipper") || ngheNghiep.contains("giao hàng")) {
            return (int) (diemToiDa * 0.7);
        }
        if (ngheNghiep.contains("công nhân") || ngheNghiep.contains("xây dựng")) {
            return (int) (diemToiDa * 0.5);
        }
        
        return (int) (diemToiDa * 0.2); // Mặc định
    }
    
    /**
     * 7. Tính điểm giới tính
     * Thống kê: Nam có tỷ lệ tai nạn cao hơn Nữ
     */
    private int calculateDiemGioiTinh(HoSoThamDinh hoSo, int diemToiDa) {
        String gioiTinh = hoSo.getKhachHang().getGioiTinh();
        if (gioiTinh == null) return 0;
        
        // Nam = 40% điểm
        // Nữ = 0 điểm
        if (gioiTinh.equalsIgnoreCase("Nam")) {
            return (int) (diemToiDa * 0.4);
        }
        return 0;
    }
    
    /**
     * 8. Tính điểm loại xe (hãng xe)
     * Xe sang → chi phí sửa chữa cao → rủi ro cao
     */
    private int calculateDiemLoaiXe(HoSoThamDinh hoSo, int diemToiDa) {
        String hangXe = hoSo.getXe().getHangXe();
        if (hangXe == null) return 0;
        
        hangXe = hangXe.toLowerCase();
        
        // Xe sang (Mercedes, BMW, Audi, Lexus) = 70% điểm
        // Xe phổ thông (Toyota, Honda, Mazda) = 0 điểm
        // Xe Trung Quốc = 40% điểm
        if (hangXe.contains("mercedes") || hangXe.contains("bmw") || 
            hangXe.contains("audi") || hangXe.contains("lexus") ||
            hangXe.contains("porsche") || hangXe.contains("bentley")) {
            return (int) (diemToiDa * 0.7);
        }
        if (hangXe.contains("toyota") || hangXe.contains("honda") || 
            hangXe.contains("mazda") || hangXe.contains("hyundai") ||
            hangXe.contains("kia") || hangXe.contains("ford")) {
            return 0;
        }
        if (hangXe.contains("vinfast") || hangXe.contains("byd") ||
            hangXe.contains("mg") || hangXe.contains("haval")) {
            return (int) (diemToiDa * 0.4);
        }
        
        return (int) (diemToiDa * 0.2); // Mặc định
    }

    private String getRiskLevelDescription(RiskLevel riskLevel) {
        int maxScore = calculateMaxPossibleScore();
        
        return switch (riskLevel) {
            case CHAP_NHAN -> String.format(
                "Chấp nhận - Rủi ro thấp (≤25%% tổng điểm, tối đa %d điểm)", 
                (int)(maxScore * 0.25)
            );
            case XEM_XET -> String.format(
                "Xem xét - Rủi ro trung bình (26-50%% tổng điểm, %d-%d điểm)", 
                (int)(maxScore * 0.26), 
                (int)(maxScore * 0.50)
            );
            case TU_CHOI -> String.format(
                "Từ chối - Rủi ro cao (>50%% tổng điểm, >%d điểm)", 
                (int)(maxScore * 0.50)
            );
        };
    }

    @Override
    @Transactional
    public void delete(Long id) {
        HoSoThamDinh hoSo = getById(id);
        hoSoThamDinhRepository.delete(hoSo);
    }
}
