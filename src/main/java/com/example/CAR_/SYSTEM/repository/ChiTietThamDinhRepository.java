package com.example.CAR_.SYSTEM.repository;

import com.example.CAR_.SYSTEM.model.ChiTietThamDinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietThamDinhRepository extends JpaRepository<ChiTietThamDinh, Long> {
    List<ChiTietThamDinh> findByHoSoThamDinhId(Long hoSoThamDinhId);
    void deleteByHoSoThamDinhId(Long hoSoThamDinhId);
}

