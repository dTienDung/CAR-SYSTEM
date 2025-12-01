package com.example.CAR_.SYSTEM.service;

import com.example.CAR_.SYSTEM.dto.request.HoSoThamDinhDTO;
import com.example.CAR_.SYSTEM.dto.response.RiskScoreDTO;
import com.example.CAR_.SYSTEM.model.HoSoThamDinh;
import com.example.CAR_.SYSTEM.model.enums.RiskLevel;
import com.example.CAR_.SYSTEM.model.enums.TrangThaiHoSo;

import java.util.List;

public interface HoSoThamDinhService {
    List<HoSoThamDinh> getAllWithDetails();
    List<HoSoThamDinh> getAll();
    List<HoSoThamDinh> filter(TrangThaiHoSo trangThai, RiskLevel riskLevel);
    HoSoThamDinh getById(Long id);
    HoSoThamDinh create(HoSoThamDinhDTO dto);
    HoSoThamDinh update(Long id, HoSoThamDinhDTO dto);
    RiskScoreDTO calculateRiskScore(Long id);
    void delete(Long id);
}

