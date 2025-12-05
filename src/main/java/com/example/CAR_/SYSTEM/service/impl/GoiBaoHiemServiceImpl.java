package com.example.CAR_.SYSTEM.service.impl;

import com.example.CAR_.SYSTEM.dto.request.GoiBaoHiemDTO;
import com.example.CAR_.SYSTEM.model.GoiBaoHiem;
import com.example.CAR_.SYSTEM.repository.GoiBaoHiemRepository;
import com.example.CAR_.SYSTEM.service.GoiBaoHiemService;
import com.example.CAR_.SYSTEM.util.CodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoiBaoHiemServiceImpl implements GoiBaoHiemService {
    
    private final GoiBaoHiemRepository goiBaoHiemRepository;
    private final com.example.CAR_.SYSTEM.repository.HoSoThamDinhRepository hoSoThamDinhRepository;
    private final com.example.CAR_.SYSTEM.repository.HopDongRepository hopDongRepository;
    
    @Override
    public List<GoiBaoHiem> getAll() {
        return goiBaoHiemRepository.findAll();
    }
    
    @Override
    public GoiBaoHiem getById(Long id) {
        return goiBaoHiemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy gói bảo hiểm với ID: " + id));
    }
    
    @Override
    @Transactional
    public GoiBaoHiem create(GoiBaoHiemDTO dto) {
        Long nextSequence = goiBaoHiemRepository.count() + 1;
        String maGoi = CodeGenerator.generateMaGoi(nextSequence);
        
        GoiBaoHiem goiBaoHiem = GoiBaoHiem.builder()
                .maGoi(maGoi)
                .tenGoi(dto.getTenGoi())
                .moTa(dto.getMoTa())
                .phiCoBan(dto.getPhiCoBan())
                .quyenLoi(dto.getQuyenLoi())
                .active(dto.getActive() != null ? dto.getActive() : true)
                .build();
        
        return goiBaoHiemRepository.save(goiBaoHiem);
    }
    
    @Override
    @Transactional
    public GoiBaoHiem update(Long id, GoiBaoHiemDTO dto) {
        GoiBaoHiem goiBaoHiem = getById(id);
        
        goiBaoHiem.setTenGoi(dto.getTenGoi());
        goiBaoHiem.setMoTa(dto.getMoTa());
        goiBaoHiem.setPhiCoBan(dto.getPhiCoBan());
        goiBaoHiem.setQuyenLoi(dto.getQuyenLoi());
        if (dto.getActive() != null) {
            goiBaoHiem.setActive(dto.getActive());
        }
        
        return goiBaoHiemRepository.save(goiBaoHiem);
    }
    
    @Override
    @Transactional
    public void delete(Long id) {
        GoiBaoHiem goiBaoHiem = getById(id);
        
        // Validate: Kiểm tra xem gói bảo hiểm có hồ sơ thẩm định không
        long soHoSo = hoSoThamDinhRepository.countByGoiBaoHiemId(id);
        if (soHoSo > 0) {
            throw new RuntimeException("Không thể xóa gói bảo hiểm vì đang có " + soHoSo + " hồ sơ thẩm định!");
        }
        
        // Validate: Kiểm tra xem gói bảo hiểm có hợp đồng không
        long soHopDong = hopDongRepository.countByGoiBaoHiemId(id);
        if (soHopDong > 0) {
            throw new RuntimeException("Không thể xóa gói bảo hiểm vì đang có " + soHopDong + " hợp đồng!");
        }
        
        goiBaoHiemRepository.delete(goiBaoHiem);
    }
}
