package com.example.backend.product.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetailInfoResponse {
    private Double weight;
    private DimensionsResponse dimensions;
    private String materials;
    private String countryOfOrigin;
    private String warrantyInfo;
    private String careInstructions;
    private AdditionalInfoResponse additionalInfo;
} 