package com.example.backend.product.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetailRequest {
    @NotNull(message = "무게는 필수 항목입니다.")
    private Double weight;

    @Valid
    @NotNull(message = "치수 정보는 필수 항목입니다.")
    private DimensionsRequest dimensions;

    private String materials;
    private String countryOfOrigin;
    private String warrantyInfo;
    private String careInstructions;

    @Valid
    private AdditionalInfoRequest additionalInfo;
} 