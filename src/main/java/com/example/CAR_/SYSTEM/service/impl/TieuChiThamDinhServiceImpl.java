package com.example.CAR_.SYSTEM.service.impl;

import com.example.CAR_.SYSTEM.dto.request.TieuChiThamDinhDTO;
import com.example.CAR_.SYSTEM.model.TieuChiThamDinh;
import com.example.CAR_.SYSTEM.repository.TieuChiThamDinhRepository;
import com.example.CAR_.SYSTEM.service.TieuChiThamDinhService;
import com.example.CAR_.SYSTEM.util.CodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TieuChiThamDinhServiceImpl implements TieuChiThamDinhService {
    
    private final TieuChiThamDinhRepository tieuChiThamDinhRepository;
    
    @Override
    public List<TieuChiThamDinh> getAll() {
        return tieuChiThamDinhRepository.findAll();
    }
    
    @Override
    public TieuChiThamDinh getById(Long id) {
        return tieuChiThamDinhRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tiêu chí thẩm định với ID: " + id));
    }
    
    @Override
    @Transactional
    public TieuChiThamDinh create(TieuChiThamDinhDTO dto) {
        Long nextSequence = tieuChiThamDinhRepository.count() + 1;
        String maTieuChi = CodeGenerator.generateMaTieuChi(nextSequence);
        
        TieuChiThamDinh tieuChi = TieuChiThamDinh.builder()
                .maTieuChi(maTieuChi)
                .tenTieuChi(dto.getTenTieuChi())
                .moTa(dto.getMoTa())
                .diemToiDa(dto.getDiemToiDa())
                .thuTu(dto.getThuTu())
                .active(dto.getActive() != null ? dto.getActive() : true)
                .dieuKien(dto.getDieuKien())
                .build();
        
        return tieuChiThamDinhRepository.save(tieuChi);
    }
    
    @Override
    @Transactional
    public TieuChiThamDinh update(Long id, TieuChiThamDinhDTO dto) {
        TieuChiThamDinh tieuChi = getById(id);
        
        tieuChi.setTenTieuChi(dto.getTenTieuChi());
        tieuChi.setMoTa(dto.getMoTa());
        tieuChi.setDiemToiDa(dto.getDiemToiDa());
        tieuChi.setThuTu(dto.getThuTu());
        if (dto.getActive() != null) {
            tieuChi.setActive(dto.getActive());
        }
        tieuChi.setDieuKien(dto.getDieuKien());
        
        return tieuChiThamDinhRepository.save(tieuChi);
    }
    
    @Override
    @Transactional
    public void delete(Long id) {
        TieuChiThamDinh tieuChi = getById(id);
        tieuChiThamDinhRepository.delete(tieuChi);
    }
}

