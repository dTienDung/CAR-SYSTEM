package com.example.CAR_.SYSTEM.repository;

import com.example.CAR_.SYSTEM.model.Xe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XeRepository extends JpaRepository<Xe, Long> {
    Optional<Xe> findByMaXe(String maXe);
    Optional<Xe> findBySoKhung(String soKhung);
    List<Xe> findByKhachHangId(Long khachHangId);
    
    boolean existsBySoKhung(String soKhung);
    
    @Query("SELECT x FROM Xe x WHERE " +
           "LOWER(x.bienSo) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(x.khachHang.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Xe> search(@Param("keyword") String keyword);
}

