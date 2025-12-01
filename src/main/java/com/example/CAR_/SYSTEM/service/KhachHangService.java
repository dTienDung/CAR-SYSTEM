package com.example.CAR_.SYSTEM.service;

import com.example.CAR_.SYSTEM.dto.request.KhachHangDTO;
import com.example.CAR_.SYSTEM.model.KhachHang;

import java.util.List;

public interface KhachHangService {
    List<KhachHang> getAll();
    List<KhachHang> search(String keyword);
    KhachHang getById(Long id);
    KhachHang create(KhachHangDTO dto);
    KhachHang update(Long id, KhachHangDTO dto);
    void delete(Long id);
}

