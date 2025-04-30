package com.example.backend.product.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductPriceResponse {
    private Double basePrice;
    private Double salePrice;
    private String currency;
    private Double taxRate;
    private Integer discountPercentage;
} 