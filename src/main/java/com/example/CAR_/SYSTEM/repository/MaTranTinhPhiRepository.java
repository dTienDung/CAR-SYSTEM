package com.example.CAR_.SYSTEM.repository;

import com.example.CAR_.SYSTEM.model.MaTranTinhPhi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaTranTinhPhiRepository extends JpaRepository<MaTranTinhPhi, Long> {
    @Query("SELECT m FROM MaTranTinhPhi m WHERE " +
           "m.active = true AND " +
           ":diemRuiRo >= m.diemRuiRoTu AND :diemRuiRo <= m.diemRuiRoDen " +
           "ORDER BY m.diemRuiRoTu")
    Optional<MaTranTinhPhi> findByDiemRuiRo(@Param("diemRuiRo") Integer diemRuiRo);
    
    List<MaTranTinhPhi> findByActiveTrue();
}

