package com.example.CAR_.SYSTEM.service;

import com.example.CAR_.SYSTEM.dto.request.TieuChiThamDinhDTO;
import com.example.CAR_.SYSTEM.model.TieuChiThamDinh;

import java.util.List;

public interface TieuChiThamDinhService {
    List<TieuChiThamDinh> getAll();
    TieuChiThamDinh getById(Long id);
    TieuChiThamDinh create(TieuChiThamDinhDTO dto);
    TieuChiThamDinh update(Long id, TieuChiThamDinhDTO dto);
    void delete(Long id);
}

