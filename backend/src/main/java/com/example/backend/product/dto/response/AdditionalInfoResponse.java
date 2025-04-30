package com.example.backend.product.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdditionalInfoResponse {
    private Boolean assemblyRequired;
    private String assemblyTime;
} 