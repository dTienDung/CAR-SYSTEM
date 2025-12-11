package com.example.CAR_.SYSTEM.service.impl;

import com.example.CAR_.SYSTEM.constants.MaTranTinhPhiConstants;
import com.example.CAR_.SYSTEM.constants.TieuChiThamDinhConstants;
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
            int diem = (autoDiem >= 0) ? autoDiem : oldScores.getOrDefault(tieuChi.getId(), 0);

            ChiTietThamDinh chiTiet = ChiTietThamDinh.builder()
                    .hoSoThamDinh(hoSo)
                    .tieuChi(tieuChi)
                    .diem(diem)
                    .ghiChu(autoDiem >= 0 ? "Tự động" : null)
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
     * Xác định mức độ rủi ro dựa trên ĐIỂM TUYỆT ĐỐI
     * Khớp với ma trận tính phí: 0-2 (Thấp), 3-5 (TB), ≥6 (Cao)
     */
    private RiskLevel determineRiskLevel(int totalScore) {
        // Dựa trên điểm tuyệt đối
        if (totalScore <= 2) {
            return RiskLevel.CHAP_NHAN;        // Rủi ro thấp
        } else if (totalScore <= 5) {
            return RiskLevel.XEM_XET;          // Rủi ro trung bình
        } else {
            return RiskLevel.TU_CHOI;          // Rủi ro cao (≥6 điểm)
        }
    }
    
    /**
     * Tính tổng điểm tối đa có thể đạt được từ constants
     */
    private int calculateMaxPossibleScore() {
        return TieuChiThamDinhConstants.TONG_DIEM_TOI_DA;
    }

    /**
     * Tính phí bảo hiểm dựa trên ĐIỂM TUYỆT ĐỐI
     * Sử dụng constants để tìm hệ số phí
     */
    private void calculatePhiBaoHiem(HoSoThamDinh hoSo) {
        try {
            // Lấy hệ số phí từ constants theo điểm rủi ro
            BigDecimal heSo = MaTranTinhPhiConstants.getHeSoPhi(hoSo.getRiskScore());
            
            // Tính phí = phí cơ bản × hệ số
            BigDecimal phiBaoHiem = hoSo.getGoiBaoHiem().getPhiCoBan().multiply(heSo);
            hoSo.setPhiBaoHiem(phiBaoHiem);
        } catch (Exception e) {
            // Nếu có lỗi, để null
            hoSo.setPhiBaoHiem(null);
        }
    }

    /**
     * Tính điểm tự động cho từng tiêu chí dựa trên mã tiêu chí
     * Hỗ trợ 4 tiêu chí: CT01 (Tuổi xe), CT02 (Mục đích), CT03 (Tuổi tài xế), CT04 (Giá trị xe)
     */
    private int calculateDiemForTieuChi(TieuChiThamDinh tieuChi, HoSoThamDinh hoSo) {
        String maTieuChi = tieuChi.getMaTieuChi().toUpperCase();
        int diemToiDa = tieuChi.getDiemToiDa();
        
        try {
            // CT01: TUỔI XE
            if (maTieuChi.contains("CT01") || maTieuChi.contains("TUOI_XE") || 
                maTieuChi.contains("TUOI XE")) {
                return calculateDiemTuoiXe(hoSo, diemToiDa);
            }
            
            // CT02: MỤC ĐÍCH SỬ DỤNG
            if (maTieuChi.contains("CT02") || maTieuChi.contains("MUC_DICH") || 
                maTieuChi.contains("MUC DICH")) {
                return calculateDiemMucDichSuDung(hoSo, diemToiDa);
            }
            
            // CT03: TUỔI TÀI XẾ
            if (maTieuChi.contains("CT03") || maTieuChi.contains("TUOI_TAI") || 
                maTieuChi.contains("TUOI TAI")) {
                return calculateDiemTuoiTaiXe(hoSo, diemToiDa);
            }
            
            // CT04: GIÁ TRỊ XE
            if (maTieuChi.contains("CT04") || maTieuChi.contains("GIA_TRI") || 
                maTieuChi.contains("GIA TRI")) {
                return calculateDiemGiaTriXe(hoSo, diemToiDa);
            }
            
        } catch (Exception e) {
            // Nếu có lỗi, trả về 0
            System.err.println("Lỗi tính điểm tiêu chí " + maTieuChi + ": " + e.getMessage());
        }
        
        // Mặc định trả về 0 điểm (chưa nhận diện được tiêu chí)
        return 0;
    }
    
    // ========== CÁC HÀM TÍNH ĐIỂM CHO 4 TIÊU CHÍ MỚI ==========
    
    /**
     * CT01: Tính điểm tuổi xe
     * <5 năm: 0đ; 5-10 năm: 1đ; >10 năm: 2đ
     */
    private int calculateDiemTuoiXe(HoSoThamDinh hoSo, int diemToiDa) {
        int namSanXuat = hoSo.getXe().getNamSanXuat();
        int namHienTai = LocalDate.now().getYear();
        int tuoiXe = namHienTai - namSanXuat;
        
        if (tuoiXe < 5) return 0;
        if (tuoiXe <= 10) return 1;
        return 2;  // >10 năm
    }
    
    /**
     * CT02: Tính điểm mục đích sử dụng
     * Cá nhân: 0đ; Kinh doanh: 2đ
     */
    private int calculateDiemMucDichSuDung(HoSoThamDinh hoSo, int diemToiDa) {
        String mucDich = hoSo.getXe().getMucDichSuDung();
        if (mucDich == null) return 0;
        
        mucDich = mucDich.toLowerCase();
        
        // Kinh doanh/Taxi/Vận tải = 2 điểm
        if (mucDich.contains("kinh doanh") || mucDich.contains("taxi") || 
            mucDich.contains("uber") || mucDich.contains("grab") ||
            mucDich.contains("vận tải") || mucDich.contains("van tai")) {
            return 2;
        }
        
        // Cá nhân = 0 điểm
        return 0;
    }
    
    /**
     * CT03: Tính điểm tuổi tài xế
     * 26-55 tuổi: 0đ; 18-25 tuổi: 2đ; >55 tuổi: 1đ
     */
    private int calculateDiemTuoiTaiXe(HoSoThamDinh hoSo, int diemToiDa) {
        if (hoSo.getKhachHang().getNgaySinh() == null) return 0;
        
        int namSinh = hoSo.getKhachHang().getNgaySinh().getYear();
        int namHienTai = LocalDate.now().getYear();
        int tuoi = namHienTai - namSinh;
        
        // 18-25 tuổi = 2 điểm (thiếu kinh nghiệm)
        if (tuoi >= 18 && tuoi <= 25) return 2;
        
        // 26-55 tuổi = 0 điểm (độ tuổi an toàn)
        if (tuoi >= 26 && tuoi <= 55) return 0;
        
        // >55 tuổi = 1 điểm (phản xạ kém hơn)
        if (tuoi > 55) return 1;
        
        // <18 tuổi hoặc edge cases
        return 2;
    }
    
    /**
     * CT04: Tính điểm giá trị xe
     * >500tr: 0đ; 200-500tr: 1đ; <200tr: 2đ
     */
    private int calculateDiemGiaTriXe(HoSoThamDinh hoSo, int diemToiDa) {
        BigDecimal giaTriXe = hoSo.getXe().getGiaTriXe();
        if (giaTriXe == null) return 0;
        
        long giaTri = giaTriXe.longValue();
        
        // >500 triệu = 0 điểm (xe an toàn, hiện đại)
        if (giaTri > 500_000_000) return 0;
        
        // 200-500 triệu = 1 điểm (trung bình)
        if (giaTri >= 200_000_000) return 1;
        
        // <200 triệu = 2 điểm (xe cũ, kém an toàn)
        return 2;
    }

    private String getRiskLevelDescription(RiskLevel riskLevel) {
        return switch (riskLevel) {
            case CHAP_NHAN -> "Chấp nhận - Rủi ro thấp (0-2 điểm)";
            case XEM_XET -> "Xem xét - Rủi ro trung bình (3-5 điểm)";
            case TU_CHOI -> "Từ chối - Rủi ro cao (≥6 điểm)";
        };
    }

    @Override
    @Transactional
    public void delete(Long id) {
        HoSoThamDinh hoSo = getById(id);
        hoSoThamDinhRepository.delete(hoSo);
    }
}
