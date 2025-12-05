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

    private int calculateDiemForTieuChi(TieuChiThamDinh tieuChi, HoSoThamDinh hoSo) {
        // Logic tính điểm đơn giản - có thể mở rộng
        // Ví dụ: kiểm tra lịch sử tai nạn
        if (tieuChi.getMaTieuChi().contains("TAI_NAN")) {
            long soLanTaiNan = lichSuTaiNanRepository.findByXeId(hoSo.getXe().getId()).size();
            return (int) Math.min(soLanTaiNan * 5, tieuChi.getDiemToiDa());
        }

        // Mặc định trả về 0 điểm
        return 0;
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
