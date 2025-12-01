package com.example.CAR_.SYSTEM.service.impl;

import com.example.CAR_.SYSTEM.dto.request.MaTranTinhPhiDTO;
import com.example.CAR_.SYSTEM.model.MaTranTinhPhi;
import com.example.CAR_.SYSTEM.repository.MaTranTinhPhiRepository;
import com.example.CAR_.SYSTEM.service.MaTranTinhPhiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaTranTinhPhiServiceImpl implements MaTranTinhPhiService {
    
    private final MaTranTinhPhiRepository maTranTinhPhiRepository;
    
    @Override
    public List<MaTranTinhPhi> getAll() {
        return maTranTinhPhiRepository.findAll();
    }
    
    @Override
    public MaTranTinhPhi getById(Long id) {
        return maTranTinhPhiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ma trận tính phí với ID: " + id));
    }
    
    @Override
    @Transactional
    public MaTranTinhPhi create(MaTranTinhPhiDTO dto) {
        // Kiểm tra overlap
        validateNoOverlap(dto.getDiemRuiRoTu(), dto.getDiemRuiRoDen(), null);
        
        MaTranTinhPhi maTran = MaTranTinhPhi.builder()
                .diemRuiRoTu(dto.getDiemRuiRoTu())
                .diemRuiRoDen(dto.getDiemRuiRoDen())
                .heSoPhi(dto.getHeSoPhi())
                .moTa(dto.getMoTa())
                .active(dto.getActive() != null ? dto.getActive() : true)
                .build();
        
        return maTranTinhPhiRepository.save(maTran);
    }
    
    @Override
    @Transactional
    public MaTranTinhPhi update(Long id, MaTranTinhPhiDTO dto) {
        MaTranTinhPhi maTran = getById(id);
        
        // Kiểm tra overlap (trừ chính nó)
        validateNoOverlap(dto.getDiemRuiRoTu(), dto.getDiemRuiRoDen(), id);
        
        maTran.setDiemRuiRoTu(dto.getDiemRuiRoTu());
        maTran.setDiemRuiRoDen(dto.getDiemRuiRoDen());
        maTran.setHeSoPhi(dto.getHeSoPhi());
        maTran.setMoTa(dto.getMoTa());
        if (dto.getActive() != null) {
            maTran.setActive(dto.getActive());
        }
        
        return maTranTinhPhiRepository.save(maTran);
    }
    
    private void validateNoOverlap(Integer diemTu, Integer diemDen, Long excludeId) {
        List<MaTranTinhPhi> existing = maTranTinhPhiRepository.findByActiveTrue();
        for (MaTranTinhPhi mt : existing) {
            if (excludeId != null && mt.getId().equals(excludeId)) {
                continue;
            }
            // Kiểm tra overlap
            if (!(diemDen < mt.getDiemRuiRoTu() || diemTu > mt.getDiemRuiRoDen())) {
                throw new RuntimeException("Khoảng điểm rủi ro bị trùng với ma trận khác: " + 
                    mt.getDiemRuiRoTu() + "-" + mt.getDiemRuiRoDen());
            }
        }
    }
    
    @Override
    @Transactional
    public void delete(Long id) {
        MaTranTinhPhi maTran = getById(id);
        maTranTinhPhiRepository.delete(maTran);
    }
}

