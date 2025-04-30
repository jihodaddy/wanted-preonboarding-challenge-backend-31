package com.example.backend.product.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductPriceRequest {
    @NotNull(message = "기본 가격은 필수 항목입니다.")
    @Min(value = 0, message = "기본 가격은 0보다 커야 합니다.")
    private Double basePrice;

    @NotNull(message = "판매 가격은 필수 항목입니다.")
    @Min(value = 0, message = "판매 가격은 0보다 커야 합니다.")
    private Double salePrice;

    @NotNull(message = "원가는 필수 항목입니다.")
    @Min(value = 0, message = "원가는 0보다 커야 합니다.")
    private Double costPrice;

    @NotNull(message = "통화는 필수 항목입니다.")
    private String currency;

    @NotNull(message = "세율은 필수 항목입니다.")
    @Min(value = 0, message = "세율은 0보다 커야 합니다.")
    private Double taxRate;
} 