package com.example.CAR_.SYSTEM.config;

import com.example.CAR_.SYSTEM.constants.MaTranTinhPhiConstants;
import com.example.CAR_.SYSTEM.constants.TieuChiThamDinhConstants;
import com.example.CAR_.SYSTEM.model.MaTranTinhPhi;
import com.example.CAR_.SYSTEM.model.TieuChiThamDinh;
import com.example.CAR_.SYSTEM.repository.MaTranTinhPhiRepository;
import com.example.CAR_.SYSTEM.repository.TieuChiThamDinhRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    
    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        log.info("=== BẮT ĐẦU KHỞI TẠO DỮ LIỆU TỪ CONSTANTS ===");
        
        // Seed Tiêu chí thẩm định
        seedTieuChiThamDinh();
        
        // Seed Ma trận tính phí
        seedMaTranTinhPhi();
        
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
}
