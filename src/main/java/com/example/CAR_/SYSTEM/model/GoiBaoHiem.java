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
@Table(name = "goi_bao_hiem")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoiBaoHiem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String maGoi;
    
    @Column(nullable = false, length = 200)
    private String tenGoi;
    
    @Column(length = 1000)
    private String moTa;
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal phiCoBan;
    
    @Column(columnDefinition = "TEXT")
    private String quyenLoi;
    
    @Column(nullable = false)
    private Boolean active = true;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "goiBaoHiem", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<HoSoThamDinh> danhSachHoSo = new ArrayList<>();
}

