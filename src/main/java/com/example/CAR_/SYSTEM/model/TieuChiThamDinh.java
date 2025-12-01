package com.example.CAR_.SYSTEM.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tieu_chi_tham_dinh")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TieuChiThamDinh {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String maTieuChi;
    
    @Column(nullable = false, length = 200)
    private String tenTieuChi;
    
    @Column(length = 1000)
    private String moTa;
    
    @Column(nullable = false)
    private Integer diemToiDa;
    
    @Column(nullable = false)
    private Integer thuTu;
    
    @Column(nullable = false)
    private Boolean active = true;
    
    @Column(columnDefinition = "TEXT")
    private String dieuKien;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "tieuChi", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<ChiTietThamDinh> chiTietThamDinh = new ArrayList<>();
}

