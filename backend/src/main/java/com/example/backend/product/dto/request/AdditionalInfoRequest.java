package com.example.backend.product.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdditionalInfoRequest {
    private Boolean assemblyRequired;
    private String assemblyTime;
} 