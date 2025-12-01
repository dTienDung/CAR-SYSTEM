package com.example.CAR_.SYSTEM.service;

import com.example.CAR_.SYSTEM.dto.request.MaTranTinhPhiDTO;
import com.example.CAR_.SYSTEM.model.MaTranTinhPhi;

import java.util.List;

public interface MaTranTinhPhiService {
    List<MaTranTinhPhi> getAll();
    MaTranTinhPhi getById(Long id);
    MaTranTinhPhi create(MaTranTinhPhiDTO dto);
    MaTranTinhPhi update(Long id, MaTranTinhPhiDTO dto);
    void delete(Long id);
}

