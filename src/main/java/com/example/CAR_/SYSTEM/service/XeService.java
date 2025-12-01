package com.example.CAR_.SYSTEM.service;

import com.example.CAR_.SYSTEM.dto.request.XeDTO;
import com.example.CAR_.SYSTEM.model.Xe;

import java.util.List;

public interface XeService {
    List<Xe> getAll();
    List<Xe> search(String keyword);
    List<Xe> getByKhachHangId(Long khachHangId);
    Xe getById(Long id);
    Xe create(XeDTO dto);
    Xe update(Long id, XeDTO dto);
    void delete(Long id);
}

