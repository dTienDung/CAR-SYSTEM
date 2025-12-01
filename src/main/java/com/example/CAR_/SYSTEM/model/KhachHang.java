package com.example.CAR_.SYSTEM.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "khach_hang")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhachHang {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 20)
    private String maKH;
    
    @Column(nullable = false, length = 100)
    private String hoTen;
    
    @Column(unique = true, nullable = false, length = 12)
    private String cccd;
    
    @Column(nullable = false)
    private LocalDate ngaySinh;
    
    @Column(nullable = false, length = 10)
    private String gioiTinh;
    
    @Column(unique = true, nullable = false, length = 20)
    private String soDienThoai;
    
    @Column(unique = true, nullable = false, length = 100)
    private String email;
    
    @Column(length = 500)
    private String diaChi;
    
    @Column(length = 100)
    private String ngheNghiep;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "khachHang", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<Xe> danhSachXe = new ArrayList<>();
    
    @OneToMany(mappedBy = "khachHang", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<HopDong> danhSachHopDong = new ArrayList<>();
}

