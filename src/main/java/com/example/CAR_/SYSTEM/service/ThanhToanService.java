package com.example.CAR_.SYSTEM.service;

import com.example.CAR_.SYSTEM.dto.request.GiaoDichHoanPhiDTO;
import com.example.CAR_.SYSTEM.dto.request.ThanhToanDTO;
import com.example.CAR_.SYSTEM.model.ThanhToan;

import java.util.List;

public interface ThanhToanService {
    List<ThanhToan> getAll();
    List<ThanhToan> getByHopDongId(Long hopDongId);
    ThanhToan getById(Long id);
    ThanhToan create(ThanhToanDTO dto);
    ThanhToan createHoanPhi(GiaoDichHoanPhiDTO dto);
    void delete(Long id);
}

