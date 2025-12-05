package com.example.CAR_.SYSTEM.repository;

import com.example.CAR_.SYSTEM.model.HopDong;
import com.example.CAR_.SYSTEM.model.enums.TrangThaiHopDong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HopDongRepository extends JpaRepository<HopDong, Long> {
    
    @Query("SELECT DISTINCT h FROM HopDong h " +
           "LEFT JOIN FETCH h.khachHang " +
           "LEFT JOIN FETCH h.xe " +
           "LEFT JOIN FETCH h.hoSoThamDinh " +
           "LEFT JOIN FETCH h.goiBaoHiem")
    List<HopDong> findAll();
    
    @Query("SELECT DISTINCT h FROM HopDong h " +
           "LEFT JOIN FETCH h.khachHang " +
           "LEFT JOIN FETCH h.xe " +
           "LEFT JOIN FETCH h.hoSoThamDinh " +
           "LEFT JOIN FETCH h.goiBaoHiem " +
           "WHERE h.maHD = :maHD")
    Optional<HopDong> findByMaHD(@Param("maHD") String maHD);
    
    @Query("SELECT DISTINCT h FROM HopDong h " +
           "LEFT JOIN FETCH h.khachHang " +
           "LEFT JOIN FETCH h.xe " +
           "LEFT JOIN FETCH h.hoSoThamDinh " +
           "LEFT JOIN FETCH h.goiBaoHiem " +
           "WHERE h.trangThai = :trangThai")
    List<HopDong> findByTrangThai(@Param("trangThai") TrangThaiHopDong trangThai);
    
    @Query("SELECT DISTINCT h FROM HopDong h " +
           "LEFT JOIN FETCH h.khachHang " +
           "LEFT JOIN FETCH h.xe " +
           "LEFT JOIN FETCH h.hoSoThamDinh " +
           "LEFT JOIN FETCH h.goiBaoHiem " +
           "WHERE h.khachHang.id = :khachHangId")
    List<HopDong> findByKhachHangId(@Param("khachHangId") Long khachHangId);
    
    @Query("SELECT DISTINCT h FROM HopDong h " +
           "LEFT JOIN FETCH h.khachHang " +
           "LEFT JOIN FETCH h.xe " +
           "LEFT JOIN FETCH h.hoSoThamDinh " +
           "LEFT JOIN FETCH h.goiBaoHiem " +
           "WHERE (:trangThai IS NULL OR h.trangThai = :trangThai) AND " +
           "(:khachHangId IS NULL OR h.khachHang.id = :khachHangId) AND " +
           "(:fromDate IS NULL OR h.ngayKy >= :fromDate) AND " +
           "(:toDate IS NULL OR h.ngayKy <= :toDate)")
    List<HopDong> filter(@Param("trangThai") TrangThaiHopDong trangThai,
                         @Param("khachHangId") Long khachHangId,
                         @Param("fromDate") LocalDate fromDate,
                         @Param("toDate") LocalDate toDate);
    
    @Query("SELECT DISTINCT h FROM HopDong h " +
           "LEFT JOIN FETCH h.khachHang " +
           "LEFT JOIN FETCH h.xe " +
           "LEFT JOIN FETCH h.hoSoThamDinh " +
           "LEFT JOIN FETCH h.goiBaoHiem " +
           "WHERE h.id = :id")
    Optional<HopDong> findById(@Param("id") Long id);
    
    // Dashboard charts queries
    @Query("SELECT h.trangThai, COUNT(h) FROM HopDong h GROUP BY h.trangThai")
    List<Object[]> countByTrangThai();
    
    @Query("SELECT FUNCTION('MONTH', h.ngayHetHan) as month, " +
           "h.trangThai, COUNT(h) FROM HopDong h " +
           "WHERE h.ngayHetHan >= :fromDate " +
           "GROUP BY FUNCTION('MONTH', h.ngayHetHan), h.trangThai " +
           "ORDER BY month")
    List<Object[]> countRenewalByMonth(@Param("fromDate") LocalDate fromDate);
    
    // Validation queries
    long countByXeId(Long xeId);
    long countByGoiBaoHiemId(Long goiBaoHiemId);
    long countByKhachHangId(Long khachHangId);
}
