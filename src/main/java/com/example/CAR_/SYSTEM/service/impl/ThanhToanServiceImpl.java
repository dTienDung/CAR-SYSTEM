package com.example.CAR_.SYSTEM.service.impl;

import com.example.CAR_.SYSTEM.dto.request.GiaoDichHoanPhiDTO;
import com.example.CAR_.SYSTEM.dto.request.ThanhToanDTO;
import com.example.CAR_.SYSTEM.model.*;
import com.example.CAR_.SYSTEM.model.enums.TrangThaiHopDong;
import com.example.CAR_.SYSTEM.model.enums.TrangThaiThanhToan;
import com.example.CAR_.SYSTEM.repository.HopDongRepository;
import com.example.CAR_.SYSTEM.repository.ThanhToanRepository;
import com.example.CAR_.SYSTEM.service.ThanhToanService;
import com.example.CAR_.SYSTEM.util.CodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ThanhToanServiceImpl implements ThanhToanService {
    
    private final ThanhToanRepository thanhToanRepository;
    private final HopDongRepository hopDongRepository;
    
    @Override
    public List<ThanhToan> getAll() {
        return thanhToanRepository.findAllWithDetails();
    }
    
    @Override
    public List<ThanhToan> getByHopDongId(Long hopDongId) {
        return thanhToanRepository.findByHopDongId(hopDongId);
    }
    
    @Override
    public ThanhToan getById(Long id) {
        return thanhToanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thanh toán với ID: " + id));
    }
    
    @Override
    @Transactional
    public ThanhToan create(ThanhToanDTO dto) {
        HopDong hopDong = hopDongRepository.findById(dto.getHopDongId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hợp đồng"));
        
        Long nextSequence = thanhToanRepository.count() + 1;
        String maTT = CodeGenerator.generateMaTT(nextSequence);
        
        ThanhToan thanhToan = ThanhToan.builder()
                .maTT(maTT)
                .hopDong(hopDong)
                .soTien(dto.getSoTien())
                .phuongThuc(dto.getPhuongThuc())
                .soTaiKhoan(dto.getSoTaiKhoan())
                .soThe(dto.getSoThe())
                .ghiChu(dto.getGhiChu())
                .isHoanPhi(false)
                .build();
        
        thanhToan = thanhToanRepository.save(thanhToan);
        
        // Cập nhật tổng thanh toán và trạng thái hợp đồng
        updateHopDongStatus(hopDong);
        
        return thanhToan;
    }
    
    @Override
    @Transactional
    public ThanhToan createHoanPhi(GiaoDichHoanPhiDTO dto) {
        HopDong hopDong = hopDongRepository.findById(dto.getHopDongId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hợp đồng"));
        
        if (hopDong.getTrangThai() != TrangThaiHopDong.CANCELLED) {
            throw new RuntimeException("Chỉ có thể hoàn phí cho hợp đồng đã hủy");
        }
        
        Long nextSequence = thanhToanRepository.count() + 1;
        String maTT = CodeGenerator.generateMaTT(nextSequence);
        
        // Số tiền hoàn là số âm
        BigDecimal soTienHoan = dto.getSoTienHoan().negate();
        
        ThanhToan thanhToan = ThanhToan.builder()
                .maTT(maTT)
                .hopDong(hopDong)
                .soTien(soTienHoan)
                .phuongThuc(dto.getPhuongThuc())
                .soTaiKhoan(dto.getSoTaiKhoan())
                .ghiChu("Hoàn phí: " + (dto.getGhiChu() != null ? dto.getGhiChu() : ""))
                .isHoanPhi(true)
                .build();
        
        return thanhToanRepository.save(thanhToan);
    }
    
    @Override
    @Transactional
    public void delete(Long id) {
        ThanhToan thanhToan = getById(id);
        HopDong hopDong = thanhToan.getHopDong();
        
        thanhToanRepository.delete(thanhToan);
        
        // Cập nhật lại trạng thái hợp đồng
        updateHopDongStatus(hopDong);
    }
    
    private void updateHopDongStatus(HopDong hopDong) {
        BigDecimal tongThanhToan = thanhToanRepository.tongThanhToanByHopDong(hopDong.getId());
        hopDong.setTongDaThanhToan(tongThanhToan);
        
        // Xác định trạng thái thanh toán
        TrangThaiThanhToan trangThaiTT;
        if (tongThanhToan.compareTo(BigDecimal.ZERO) == 0) {
            trangThaiTT = TrangThaiThanhToan.CHUA_THANH_TOAN;
        } else if (tongThanhToan.compareTo(hopDong.getTongPhiBaoHiem()) < 0) {
            trangThaiTT = TrangThaiThanhToan.THANH_TOAN_MOT_PHAN;
        } else {
            trangThaiTT = TrangThaiThanhToan.DA_THANH_TOAN_DU;
        }
        
        // Cập nhật trạng thái hợp đồng
        if (hopDong.getTrangThai() == TrangThaiHopDong.DRAFT || 
            hopDong.getTrangThai() == TrangThaiHopDong.PENDING_PAYMENT) {
            if (trangThaiTT == TrangThaiThanhToan.DA_THANH_TOAN_DU) {
                hopDong.setTrangThai(TrangThaiHopDong.ACTIVE);
            } else if (trangThaiTT == TrangThaiThanhToan.THANH_TOAN_MOT_PHAN) {
                hopDong.setTrangThai(TrangThaiHopDong.PENDING_PAYMENT);
            }
        }
        
        hopDongRepository.save(hopDong);
    }
}

