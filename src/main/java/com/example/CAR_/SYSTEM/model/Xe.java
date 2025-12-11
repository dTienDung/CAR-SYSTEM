package com.example.CAR_.SYSTEM.model;

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
@Table(name = "xe")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Xe {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 20)
    private String maXe; // XE0001, XE0002...
    
    @Column(nullable = false, length = 20)
    private String bienSo; // Biển số xe
    
    @Column(unique = true, nullable = false, length = 50)
    private String soKhung; // Unique - Số khung
    
    @Column(length = 50)
    private String soMay;
    
    @Column(nullable = false, length = 100)
    private String hangXe; // Hãng xe (Toyota, Honda...)
    
    @Column(nullable = false, length = 100)
    private String dongXe; // Dòng xe (Camry, Civic...)
    
    @Column(nullable = false)
    private Integer namSanXuat;
    
    @Column(nullable = false)
    private Integer namDangKy;
    
    @Column(nullable = false, length = 20)
    private String mauSac;
    
    @Column(nullable = false, length = 50)
    private String mucDichSuDung;
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal giaTriXe;
    
    @Column(length = 500)
    private String thongTinKyThuat;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "khach_hang_id", nullable = false)
    private KhachHang khachHang;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "xe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<LichSuTaiNan> lichSuTaiNan = new ArrayList<>();
    @OneToMany(mappedBy = "xe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<HoSoThamDinh> danhSachHoSo = new ArrayList<>();
}
