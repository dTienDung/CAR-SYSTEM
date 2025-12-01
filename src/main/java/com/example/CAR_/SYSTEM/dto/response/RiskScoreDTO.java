package com.example.CAR_.SYSTEM.dto.response;

import com.example.CAR_.SYSTEM.model.enums.RiskLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskScoreDTO {
    private Integer riskScore;
    private RiskLevel riskLevel;
    private String moTa;
}

