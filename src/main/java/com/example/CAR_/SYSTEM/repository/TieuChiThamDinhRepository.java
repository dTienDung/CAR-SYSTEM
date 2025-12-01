package com.example.CAR_.SYSTEM.repository;

import com.example.CAR_.SYSTEM.model.TieuChiThamDinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TieuChiThamDinhRepository extends JpaRepository<TieuChiThamDinh, Long> {
    Optional<TieuChiThamDinh> findByMaTieuChi(String maTieuChi);
    List<TieuChiThamDinh> findByActiveTrueOrderByThuTu();
}

