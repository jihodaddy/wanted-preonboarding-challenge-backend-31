package com.example.backend.product.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductOptionResponse {
    private Long id;
    private String name;
    private Double additionalPrice;
    private String sku;
    private Integer stock;
    private Integer displayOrder;
} 