package com.example.CAR_.SYSTEM.model;

import com.example.CAR_.SYSTEM.model.enums.LoaiQuanHeHopDong;
import com.example.CAR_.SYSTEM.model.enums.TrangThaiHopDong;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hop_dong")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HopDong {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String maHD; //  mã hợp  doodndfh bằng buốc nhưu nay HD-YYYYMMDD-XXXX
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "khach_hang_id", nullable = false)
    private KhachHang khachHang;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "xe_id", nullable = false)
    private Xe xe;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ho_so_tham_dinh_id", nullable = false)
    private HoSoThamDinh hoSoThamDinh;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goi_bao_hiem_id", nullable = false)
    private GoiBaoHiem goiBaoHiem;
    
    @Column(nullable = false)
    private LocalDate ngayKy;
    
    @Column(nullable = false)
    private LocalDate ngayHieuLuc;
    
    @Column(nullable = false)
    private LocalDate ngayHetHan;
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal tongPhiBaoHiem;
    
    @Column(nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal tongDaThanhToan = BigDecimal.ZERO;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrangThaiHopDong trangThai;
    
    @Column(length = 1000)
    private String ghiChu;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_tao_id")
    private User nguoiTao;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "hopDong", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<ThanhToan> danhSachThanhToan = new ArrayList<>();
    

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "hop_dong_cu_id")
    private HopDong hopDongCu; // Hợp đồng cũ (nếu là tái tục)
    
    @Enumerated(EnumType.STRING)
    @Column(name = "loai_quan_he")
    private LoaiQuanHeHopDong loaiQuanHe;
    
    @OneToMany(mappedBy = "hopDongCu", cascade = CascadeType.ALL)
    @JsonIgnore
    @Builder.Default
    private List<HopDong> hopDongMoi = new ArrayList<>();
}

