package com.example.CAR_.SYSTEM.repository;

import com.example.CAR_.SYSTEM.model.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, Long> {
    Optional<KhachHang> findByMaKH(String maKH);
    Optional<KhachHang> findByCccd(String cccd);
    Optional<KhachHang> findByEmail(String email);
    Optional<KhachHang> findBySoDienThoai(String soDienThoai);
    
    boolean existsByCccd(String cccd);
    boolean existsByEmail(String email);
    boolean existsBySoDienThoai(String soDienThoai);
    
    @Query("SELECT k FROM KhachHang k WHERE " +
           "LOWER(k.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "k.cccd LIKE CONCAT('%', :keyword, '%') OR " +
           "k.soDienThoai LIKE CONCAT('%', :keyword, '%')")
    List<KhachHang> search(@Param("keyword") String keyword);
}

