package com.example.CAR_.SYSTEM.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelDTO {
    private String lyDo;
    private Boolean hoanPhi = true; // Có hoàn phí hay không
}

