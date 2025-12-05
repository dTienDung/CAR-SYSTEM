package com.example.CAR_.SYSTEM.service.impl;

import com.example.CAR_.SYSTEM.dto.request.KhachHangDTO;
import com.example.CAR_.SYSTEM.model.KhachHang;
import com.example.CAR_.SYSTEM.repository.KhachHangRepository;
import com.example.CAR_.SYSTEM.service.KhachHangService;
import com.example.CAR_.SYSTEM.util.CodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KhachHangServiceImpl implements KhachHangService {
    
    private final KhachHangRepository khachHangRepository;
    
    @Override
    public List<KhachHang> getAll() {
        return khachHangRepository.findAll();
    }
    
    @Override
    public List<KhachHang> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }
        return khachHangRepository.search(keyword.trim());
    }
    
    @Override
    public KhachHang getById(Long id) {
        return khachHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + id));
    }
    
    @Override
    @Transactional
    public KhachHang create(KhachHangDTO dto) {
        if (khachHangRepository.existsByCccd(dto.getCccd())) {
            throw new RuntimeException("CCCD đã tồn tại");
        }
        if (khachHangRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }
        if (khachHangRepository.existsBySoDienThoai(dto.getSoDienThoai())) {
            throw new RuntimeException("Số điện thoại đã tồn tại");
        }
        
        Long nextSequence = khachHangRepository.count() + 1;
        String maKH = CodeGenerator.generateMaKH(nextSequence);
        
        KhachHang khachHang = KhachHang.builder()
                .maKH(maKH)
                .hoTen(dto.getHoTen())
                .cccd(dto.getCccd())
                .ngaySinh(dto.getNgaySinh())
                .gioiTinh(dto.getGioiTinh())
                .soDienThoai(dto.getSoDienThoai())
                .email(dto.getEmail())
                .diaChi(dto.getDiaChi())
                .ngheNghiep(dto.getNgheNghiep())
                .build();
        
        return khachHangRepository.save(khachHang);
    }
    
    @Override
    @Transactional
    public KhachHang update(Long id, KhachHangDTO dto) {
        KhachHang khachHang = getById(id);
        
        if (!khachHang.getCccd().equals(dto.getCccd()) && khachHangRepository.existsByCccd(dto.getCccd())) {
            throw new RuntimeException("CCCD đã tồn tại");
        }
        if (!khachHang.getEmail().equals(dto.getEmail()) && khachHangRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }
        if (!khachHang.getSoDienThoai().equals(dto.getSoDienThoai()) && khachHangRepository.existsBySoDienThoai(dto.getSoDienThoai())) {
            throw new RuntimeException("Số điện thoại đã tồn tại");
        }
        
        khachHang.setHoTen(dto.getHoTen());
        khachHang.setCccd(dto.getCccd());
        khachHang.setNgaySinh(dto.getNgaySinh());
        khachHang.setGioiTinh(dto.getGioiTinh());
        khachHang.setSoDienThoai(dto.getSoDienThoai());
        khachHang.setEmail(dto.getEmail());
        khachHang.setDiaChi(dto.getDiaChi());
        khachHang.setNgheNghiep(dto.getNgheNghiep());
        
        return khachHangRepository.save(khachHang);
    }
    
    @Override
    @Transactional
    public void delete(Long id) {
        KhachHang khachHang = getById(id);
        
        // Validate: Kiểm tra xem khách hàng có xe không
        if (khachHang.getDanhSachXe() != null && !khachHang.getDanhSachXe().isEmpty()) {
            throw new RuntimeException("Không thể xóa khách hàng vì đang có " + 
                khachHang.getDanhSachXe().size() + " xe. Vui lòng xóa xe trước!");
        }
        
        // Validate: Kiểm tra xem khách hàng có hợp đồng không
        if (khachHang.getDanhSachHopDong() != null && !khachHang.getDanhSachHopDong().isEmpty()) {
            throw new RuntimeException("Không thể xóa khách hàng vì đang có " + 
                khachHang.getDanhSachHopDong().size() + " hợp đồng!");
        }
        
        khachHangRepository.delete(khachHang);
    }
}
