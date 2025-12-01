package com.example.CAR_.SYSTEM.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "chi_tiet_tham_dinh")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChiTietThamDinh {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ho_so_tham_dinh_id", nullable = false)
    private HoSoThamDinh hoSoThamDinh;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tieu_chi_id", nullable = false)
    private TieuChiThamDinh tieuChi;
    
    @Column(nullable = false)
    private Integer diem;
    
    @Column(length = 500)
    private String ghiChu;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}

