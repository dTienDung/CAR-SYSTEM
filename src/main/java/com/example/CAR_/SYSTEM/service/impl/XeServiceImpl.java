package com.example.CAR_.SYSTEM.service.impl;

import com.example.CAR_.SYSTEM.dto.request.XeDTO;
import com.example.CAR_.SYSTEM.model.Xe;
import com.example.CAR_.SYSTEM.repository.KhachHangRepository;
import com.example.CAR_.SYSTEM.repository.XeRepository;
import com.example.CAR_.SYSTEM.service.XeService;
import com.example.CAR_.SYSTEM.util.CodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class XeServiceImpl implements XeService {
    
    private final XeRepository xeRepository;
    private final KhachHangRepository khachHangRepository;
    private final com.example.CAR_.SYSTEM.repository.HoSoThamDinhRepository hoSoThamDinhRepository;
    private final com.example.CAR_.SYSTEM.repository.HopDongRepository hopDongRepository;
    
    @Override
    public List<Xe> getAll() {
        return xeRepository.findAll();
    }
    
    @Override
    public List<Xe> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }
        return xeRepository.search(keyword.trim());
    }
    
    @Override
    public List<Xe> getByKhachHangId(Long khachHangId) {
        return xeRepository.findByKhachHangId(khachHangId);
    }
    
    @Override
    public Xe getById(Long id) {
        return xeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe với ID: " + id));
    }
    
    @Override
    @Transactional
    public Xe create(XeDTO dto) {
        if (xeRepository.existsBySoKhung(dto.getSoKhung())) {
            throw new RuntimeException("Số khung đã tồn tại");
        }
        
        var khachHang = khachHangRepository.findById(dto.getKhachHangId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + dto.getKhachHangId()));
        
        Long nextSequence = xeRepository.count() + 1;
        String maXe = CodeGenerator.generateMaXe(nextSequence);
        
        Xe xe = Xe.builder()
                .maXe(maXe)
                .bienSo(dto.getBienSo())
                .soKhung(dto.getSoKhung())
                .soMay(dto.getSoMay())
                .hangXe(dto.getHangXe())
                .dongXe(dto.getDongXe())
                .namSanXuat(dto.getNamSanXuat())
                .namDangKy(dto.getNamDangKy())
                .mauSac(dto.getMauSac())
                .mucDichSuDung(dto.getMucDichSuDung())
                .giaTriXe(dto.getGiaTriXe())
                .thongTinKyThuat(dto.getThongTinKyThuat())
                .khachHang(khachHang)
                .build();
        
        return xeRepository.save(xe);
    }
    
    @Override
    @Transactional
    public Xe update(Long id, XeDTO dto) {
        Xe xe = getById(id);
        
        if (!xe.getSoKhung().equals(dto.getSoKhung()) && xeRepository.existsBySoKhung(dto.getSoKhung())) {
            throw new RuntimeException("Số khung đã tồn tại");
        }
        
        var khachHang = khachHangRepository.findById(dto.getKhachHangId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + dto.getKhachHangId()));
        
        xe.setBienSo(dto.getBienSo());
        xe.setSoKhung(dto.getSoKhung());
        xe.setSoMay(dto.getSoMay());
        xe.setHangXe(dto.getHangXe());
        xe.setDongXe(dto.getDongXe());
        xe.setNamSanXuat(dto.getNamSanXuat());
        xe.setNamDangKy(dto.getNamDangKy());
        xe.setMauSac(dto.getMauSac());
        xe.setMucDichSuDung(dto.getMucDichSuDung());
        xe.setGiaTriXe(dto.getGiaTriXe());
        xe.setThongTinKyThuat(dto.getThongTinKyThuat());
        xe.setKhachHang(khachHang);
        
        return xeRepository.save(xe);
    }
    
    @Override
    @Transactional
    public void delete(Long id) {
        Xe xe = getById(id);
        
        // Validate: Kiểm tra xem xe có hồ sơ thẩm định không
        long soHoSo = hoSoThamDinhRepository.countByXeId(id);
        if (soHoSo > 0) {
            throw new RuntimeException("Không thể xóa xe vì đang có " + soHoSo + " hồ sơ thẩm định!");
        }
        
        // Validate: Kiểm tra xem xe có hợp đồng không
        long soHopDong = hopDongRepository.countByXeId(id);
        if (soHopDong > 0) {
            throw new RuntimeException("Không thể xóa xe vì đang có " + soHopDong + " hợp đồng!");
        }
        
        xeRepository.delete(xe);
    }
}
