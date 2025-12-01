package com.example.CAR_.SYSTEM.repository;

import com.example.CAR_.SYSTEM.model.ThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThanhToanRepository extends JpaRepository<ThanhToan, Long> {
    Optional<ThanhToan> findByMaTT(String maTT);

    @Query("SELECT DISTINCT t FROM ThanhToan t " +
           "LEFT JOIN FETCH t.hopDong hd " +
           "LEFT JOIN FETCH hd.khachHang " +
           "LEFT JOIN FETCH hd.xe " +
           "LEFT JOIN FETCH hd.hoSoThamDinh " +
           "LEFT JOIN FETCH hd.goiBaoHiem")
    List<ThanhToan> findAllWithDetails();

    @Query("SELECT DISTINCT t FROM ThanhToan t " +
           "LEFT JOIN FETCH t.hopDong hd " +
           "LEFT JOIN FETCH hd.khachHang " +
           "LEFT JOIN FETCH hd.xe " +
           "LEFT JOIN FETCH hd.hoSoThamDinh " +
           "LEFT JOIN FETCH hd.goiBaoHiem " +
           "WHERE hd.id = :hopDongId")
    List<ThanhToan> findByHopDongId(@Param("hopDongId") Long hopDongId);
    
    @Query("SELECT COALESCE(SUM(t.soTien), 0) FROM ThanhToan t WHERE t.hopDong.id = :hopDongId")
    java.math.BigDecimal tongThanhToanByHopDong(@Param("hopDongId") Long hopDongId);
    
    // Dashboard charts queries
    @Query("SELECT DATE(t.createdAt), SUM(t.soTien) FROM ThanhToan t " +
           "WHERE t.isHoanPhi = false " +
           "AND t.createdAt >= :fromDate " +
           "GROUP BY DATE(t.createdAt) " +
           "ORDER BY DATE(t.createdAt)")
    List<Object[]> sumByDateRange(@Param("fromDate") java.time.LocalDateTime fromDate);
}
