package com.example.backend.product.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelatedProductResponse {
    private Long id;
    private String name;
    private String slug;
    private String shortDescription;
    private ProductImageResponse primaryImage;
    private Integer basePrice;
    private Integer salePrice;
    private String currency;
} 