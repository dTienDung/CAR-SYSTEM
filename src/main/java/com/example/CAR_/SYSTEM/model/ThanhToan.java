package com.example.CAR_.SYSTEM.model;

import com.example.CAR_.SYSTEM.model.enums.PhuongThucThanhToan;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "thanh_toan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThanhToan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String maTT; // TT-YYYYMMDD-XXXX
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hop_dong_id", nullable = false)
    private HopDong hopDong;
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal soTien; // Số tiền (có thể âm nếu là hoàn phí)
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PhuongThucThanhToan phuongThuc;
    
    @Column(length = 200)
    private String soTaiKhoan; // Số tài khoản (nếu chuyển khoản)
    
    @Column(length = 200)
    private String soThe; // Số thẻ (nếu POS/thẻ)
    
    @Column(length = 500)
    private String ghiChu;
    
    @Column(nullable = false)
    private Boolean isHoanPhi = false; // Đánh dấu là giao dịch hoàn phí
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_thu_id")
    private User nguoiThu; // Nhân viên thu tiền
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}

