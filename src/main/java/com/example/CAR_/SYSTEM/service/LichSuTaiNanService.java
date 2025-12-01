package com.example.CAR_.SYSTEM.service;

import com.example.CAR_.SYSTEM.model.LichSuTaiNan;

import java.util.List;

public interface LichSuTaiNanService {
    List<LichSuTaiNan> getAll();
    List<LichSuTaiNan> getByXeId(Long xeId);
    LichSuTaiNan getById(Long id);
    LichSuTaiNan create(LichSuTaiNan lichSuTaiNan);
    void delete(Long id);
}


