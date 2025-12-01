package com.example.CAR_.SYSTEM.model;

import com.example.CAR_.SYSTEM.model.enums.RiskLevel;
import com.example.CAR_.SYSTEM.model.enums.TrangThaiHoSo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ho_so_tham_dinh")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HoSoThamDinh {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String maHS;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "khach_hang_id", nullable = false)
    private KhachHang khachHang;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "xe_id", nullable = false)
    private Xe xe;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goi_bao_hiem_id", nullable = false)
    private GoiBaoHiem goiBaoHiem;
    
    @Column(nullable = false)
    private Integer riskScore;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RiskLevel riskLevel;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrangThaiHoSo trangThai;
    
    @Column(precision = 15, scale = 2)
    private BigDecimal phiBaoHiem;
    
    @Column(length = 1000)
    private String ghiChu;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_tham_dinh_id")
    private User nguoiThamDinh;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "hoSoThamDinh", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<ChiTietThamDinh> chiTietThamDinh = new ArrayList<>();
    
    @OneToMany(mappedBy = "hoSoThamDinh", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<HopDong> danhSachHopDong = new ArrayList<>();
}

