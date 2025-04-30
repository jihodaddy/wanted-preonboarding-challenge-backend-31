package com.example.backend.product.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DimensionsRequest {
    private Double width;
    private Double height;
    private Double depth;
} 