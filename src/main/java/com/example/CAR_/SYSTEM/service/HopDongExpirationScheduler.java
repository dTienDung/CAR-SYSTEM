package com.example.CAR_.SYSTEM.service;

import com.example.CAR_.SYSTEM.model.HopDong;
import com.example.CAR_.SYSTEM.model.enums.TrangThaiHopDong;
import com.example.CAR_.SYSTEM.repository.HopDongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Service tự động kiểm tra và cập nhật trạng thái hợp đồng hết hạn
 * Chạy mỗi ngày lúc 00:00 (nửa đêm)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class HopDongExpirationScheduler {
    
    private final HopDongRepository hopDongRepository;
    
    /**
     * Tự động cập nhật trạng thái hợp đồng hết hạn
     * Chạy mỗi ngày lúc 00:00
     */
    @Scheduled(cron = "0 0 0 * * *") // Chạy lúc 00:00:00 mỗi ngày
    @Transactional
    public void checkAndUpdateExpiredContracts() {
        log.info("=== BẮT ĐẦU KIỂM TRA HỢP ĐỒNG HẾT HẠN ===");
        
        LocalDate today = LocalDate.now();
        
        // Tìm tất cả hợp đồng đang ACTIVE và đã quá ngày hết hạn
        List<HopDong> expiredContracts = hopDongRepository.findAll().stream()
            .filter(hd -> hd.getTrangThai() == TrangThaiHopDong.ACTIVE)
            .filter(hd -> hd.getNgayHetHan().isBefore(today))
            .toList();
        
        if (expiredContracts.isEmpty()) {
            log.info("✓ Không có hợp đồng nào hết hạn hôm nay");
        } else {
            log.info("Tìm thấy {} hợp đồng hết hạn, đang cập nhật...", expiredContracts.size());
            
            for (HopDong hopDong : expiredContracts) {
                hopDong.setTrangThai(TrangThaiHopDong.EXPIRED);
                hopDongRepository.save(hopDong);
                
                log.info("  ✓ Cập nhật hợp đồng {} → EXPIRED (Hết hạn: {})", 
                    hopDong.getMaHD(), 
                    hopDong.getNgayHetHan());
            }
            
            log.info("✓ Đã cập nhật {} hợp đồng sang trạng thái EXPIRED", expiredContracts.size());
        }
        
        log.info("=== HOÀN TẤT KIỂM TRA HỢP ĐỒNG HẾT HẠN ===");
    }
    
    /**
     * Kiểm tra hợp đồng sắp hết hạn (trong 7 ngày)
     * Chạy mỗi ngày lúc 08:00
     */
    @Scheduled(cron = "0 0 8 * * *") // Chạy lúc 08:00:00 mỗi ngày
    @Transactional(readOnly = true)
    public void checkContractsExpiringSoon() {
        log.info("=== KIỂM TRA HỢP ĐỒNG SẮP HẾT HẠN ===");
        
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysLater = today.plusDays(7);
        
        // Tìm hợp đồng ACTIVE và sắp hết hạn trong 7 ngày
        List<HopDong> expiringSoon = hopDongRepository.findAll().stream()
            .filter(hd -> hd.getTrangThai() == TrangThaiHopDong.ACTIVE)
            .filter(hd -> !hd.getNgayHetHan().isBefore(today)) // Chưa hết hạn
            .filter(hd -> hd.getNgayHetHan().isBefore(sevenDaysLater)) // Trong 7 ngày tới
            .toList();
        
        if (expiringSoon.isEmpty()) {
            log.info("✓ Không có hợp đồng nào sắp hết hạn trong 7 ngày tới");
        } else {
            log.warn("⚠️ Có {} hợp đồng sắp hết hạn trong 7 ngày tới:", expiringSoon.size());
            
            for (HopDong hopDong : expiringSoon) {
                long daysUntilExpiry = java.time.temporal.ChronoUnit.DAYS.between(today, hopDong.getNgayHetHan());
                log.warn("  - {} sẽ hết hạn trong {} ngày ({})", 
                    hopDong.getMaHD(), 
                    daysUntilExpiry,
                    hopDong.getNgayHetHan());
            }
            
            // TODO: Có thể gửi email thông báo cho khách hàng ở đây
        }
        
        log.info("=== HOÀN TẤT KIỂM TRA HỢP ĐỒNG SẮP HẾT HẠN ===");
    }
}
