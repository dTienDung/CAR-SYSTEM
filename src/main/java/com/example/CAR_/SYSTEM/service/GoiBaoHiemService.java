package com.example.CAR_.SYSTEM.service;

import com.example.CAR_.SYSTEM.dto.request.GoiBaoHiemDTO;
import com.example.CAR_.SYSTEM.model.GoiBaoHiem;

import java.util.List;

public interface GoiBaoHiemService {
    List<GoiBaoHiem> getAll();
    GoiBaoHiem getById(Long id);
    GoiBaoHiem create(GoiBaoHiemDTO dto);
    GoiBaoHiem update(Long id, GoiBaoHiemDTO dto);
    void delete(Long id);
}

