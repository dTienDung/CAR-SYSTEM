package com.example.CAR_.SYSTEM.service;

import com.example.CAR_.SYSTEM.dto.request.HopDongDTO;
import com.example.CAR_.SYSTEM.dto.request.HopDongRenewDTO;
import com.example.CAR_.SYSTEM.dto.request.CancelDTO;
import com.example.CAR_.SYSTEM.model.HopDong;
import com.example.CAR_.SYSTEM.model.enums.TrangThaiHopDong;

import java.time.LocalDate;
import java.util.List;

public interface HopDongService {
    List<HopDong> getAll();
    List<HopDong> filter(TrangThaiHopDong trangThai, Long khachHangId, LocalDate fromDate, LocalDate toDate);
    HopDong getById(Long id);
    HopDong create(HopDongDTO dto);
    HopDong update(Long id, HopDongDTO dto);
    HopDong renew(Long id, HopDongRenewDTO dto);
    HopDong cancel(Long id, CancelDTO dto);
    void delete(Long id);
}

