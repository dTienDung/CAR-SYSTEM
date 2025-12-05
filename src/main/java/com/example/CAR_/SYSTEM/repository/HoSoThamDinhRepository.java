package com.example.CAR_.SYSTEM.repository;

import com.example.CAR_.SYSTEM.model.HoSoThamDinh;
import com.example.CAR_.SYSTEM.model.enums.RiskLevel;
import com.example.CAR_.SYSTEM.model.enums.TrangThaiHoSo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HoSoThamDinhRepository extends JpaRepository<HoSoThamDinh, Long> {
    
    @Query("SELECT DISTINCT h FROM HoSoThamDinh h " +
           "LEFT JOIN FETCH h.khachHang " +
           "LEFT JOIN FETCH h.xe " +
           "LEFT JOIN FETCH h.goiBaoHiem")
    List<HoSoThamDinh> findAll();
    @Query("SELECT h FROM HoSoThamDinh h " +
            "JOIN FETCH h.khachHang " +
            "JOIN FETCH h.goiBaoHiem")

    List<HoSoThamDinh> findAllWithDetails();
    @Query("SELECT DISTINCT h FROM HoSoThamDinh h " +
           "LEFT JOIN FETCH h.khachHang " +
           "LEFT JOIN FETCH h.xe " +
           "LEFT JOIN FETCH h.goiBaoHiem " +
           "WHERE h.maHS = :maHS")
    Optional<HoSoThamDinh> findByMaHS(@Param("maHS") String maHS);
    
    @Query("SELECT DISTINCT h FROM HoSoThamDinh h " +
           "LEFT JOIN FETCH h.khachHang " +
           "LEFT JOIN FETCH h.xe " +
           "LEFT JOIN FETCH h.goiBaoHiem " +
           "WHERE h.trangThai = :trangThai")
    List<HoSoThamDinh> findByTrangThai(@Param("trangThai") TrangThaiHoSo trangThai);
    
    @Query("SELECT DISTINCT h FROM HoSoThamDinh h " +
           "LEFT JOIN FETCH h.khachHang " +
           "LEFT JOIN FETCH h.xe " +
           "LEFT JOIN FETCH h.goiBaoHiem " +
           "WHERE h.riskLevel = :riskLevel")
    List<HoSoThamDinh> findByRiskLevel(@Param("riskLevel") RiskLevel riskLevel);
    
    @Query("SELECT DISTINCT h FROM HoSoThamDinh h " +
           "LEFT JOIN FETCH h.khachHang " +
           "LEFT JOIN FETCH h.xe " +
           "LEFT JOIN FETCH h.goiBaoHiem " +
           "WHERE (:trangThai IS NULL OR h.trangThai = :trangThai) AND " +
           "(:riskLevel IS NULL OR h.riskLevel = :riskLevel)")
    List<HoSoThamDinh> filter(@Param("trangThai") TrangThaiHoSo trangThai, 
                               @Param("riskLevel") RiskLevel riskLevel);
    
    @Query("SELECT DISTINCT h FROM HoSoThamDinh h " +
           "LEFT JOIN FETCH h.khachHang " +
           "LEFT JOIN FETCH h.xe " +
           "LEFT JOIN FETCH h.goiBaoHiem " +
           "WHERE h.id = :id")
    Optional<HoSoThamDinh> findById(@Param("id") Long id);
    
    // Dashboard charts queries
    @Query("SELECT h.riskLevel, COUNT(h) FROM HoSoThamDinh h GROUP BY h.riskLevel")
    List<Object[]> countByRiskLevel();
    
    @Query("SELECT h FROM HoSoThamDinh h " +
           "LEFT JOIN FETCH h.xe x " +
           "LEFT JOIN FETCH x.khachHang " +
           "LEFT JOIN FETCH h.goiBaoHiem " +
           "WHERE h.riskLevel = com.example.CAR_.SYSTEM.model.enums.RiskLevel.TU_CHOI " +
           "ORDER BY h.riskScore DESC")
    List<HoSoThamDinh> findTopRiskVehicles();
    
    // Validation queries
    long countByXeId(Long xeId);
    long countByGoiBaoHiemId(Long goiBaoHiemId);
}
