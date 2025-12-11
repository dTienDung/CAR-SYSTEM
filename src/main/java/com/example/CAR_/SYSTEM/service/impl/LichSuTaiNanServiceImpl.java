package com.example.CAR_.SYSTEM.service.impl;

import com.example.CAR_.SYSTEM.model.LichSuTaiNan;
import com.example.CAR_.SYSTEM.repository.LichSuTaiNanRepository;
import com.example.CAR_.SYSTEM.repository.XeRepository;
import com.example.CAR_.SYSTEM.service.LichSuTaiNanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LichSuTaiNanServiceImpl implements LichSuTaiNanService {

    private final LichSuTaiNanRepository lichSuTaiNanRepository;
    private final XeRepository xeRepository;

    @Override
    public List<LichSuTaiNan> getAll() {
        return lichSuTaiNanRepository.findAll();
    }

    @Override
    public List<LichSuTaiNan> getByXeId(Long xeId) {
        return lichSuTaiNanRepository.findByXeId(xeId);
    }

    @Override
    public LichSuTaiNan getById(Long id) {
        return lichSuTaiNanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch sử tai nạn với ID: " + id));
    }

    @Override
    @Transactional
    public LichSuTaiNan create(LichSuTaiNan lichSuTaiNan) {
        if (lichSuTaiNan.getXe() == null || lichSuTaiNan.getXe().getId() == null) {
            throw new RuntimeException("Thiếu thông tin xe");
        }
        var xe = xeRepository.findById(lichSuTaiNan.getXe().getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe với ID: " + lichSuTaiNan.getXe().getId()));
        lichSuTaiNan.setXe(xe);
        return lichSuTaiNanRepository.save(lichSuTaiNan);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        LichSuTaiNan lichSuTaiNan = getById(id);
        lichSuTaiNanRepository.delete(lichSuTaiNan);
    }
}


