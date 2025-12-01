package com.example.CAR_.SYSTEM.repository;

import com.example.CAR_.SYSTEM.model.LichSuTaiNan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LichSuTaiNanRepository extends JpaRepository<LichSuTaiNan, Long> {
    List<LichSuTaiNan> findByXeId(Long xeId);
}

