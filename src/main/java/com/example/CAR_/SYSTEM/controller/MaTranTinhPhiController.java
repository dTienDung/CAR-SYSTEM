package com.example.CAR_.SYSTEM.controller;

import com.example.CAR_.SYSTEM.dto.response.ApiResponse;
import com.example.CAR_.SYSTEM.model.MaTranTinhPhi;
import com.example.CAR_.SYSTEM.service.MaTranTinhPhiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ma-tran-tinh-phi")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MaTranTinhPhiController {
    
    private final MaTranTinhPhiService maTranTinhPhiService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<MaTranTinhPhi>>> getAllMaTran() {
        List<MaTranTinhPhi> maTranList = maTranTinhPhiService.getAll();
        return ResponseEntity.ok(ApiResponse.success(maTranList));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MaTranTinhPhi>> getMaTranById(@PathVariable Long id) {
        MaTranTinhPhi maTran = maTranTinhPhiService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(maTran));
    }
}
