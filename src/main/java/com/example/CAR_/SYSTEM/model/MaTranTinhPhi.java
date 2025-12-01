package com.example.CAR_.SYSTEM.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ma_tran_tinh_phi")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaTranTinhPhi {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Integer diemRuiRoTu;
    
    @Column(nullable = false)
    private Integer diemRuiRoDen;
    
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal heSoPhi;
    
    @Column(length = 500)
    private String moTa;
    
    @Column(nullable = false)
    private Boolean active = true;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

