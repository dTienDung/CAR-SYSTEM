package com.example.CAR_.SYSTEM.repository;

import com.example.CAR_.SYSTEM.model.GoiBaoHiem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GoiBaoHiemRepository extends JpaRepository<GoiBaoHiem, Long> {
    Optional<GoiBaoHiem> findByMaGoi(String maGoi);
    List<GoiBaoHiem> findByActiveTrue();
}

