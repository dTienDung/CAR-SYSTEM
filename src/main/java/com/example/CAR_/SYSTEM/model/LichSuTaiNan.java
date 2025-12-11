package com.example.CAR_.SYSTEM.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lich_su_tai_nan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichSuTaiNan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "xe_id", nullable = false)
    private Xe xe;
    
    @Column(nullable = false)
    private LocalDate ngayXayRa;
    
    @Column(length = 500)
    private String moTa;
    
    @Column(precision = 15, scale = 2)
    private java.math.BigDecimal thietHai;
    
    @Column(length = 200)
    private String diaDiem;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}

