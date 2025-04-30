package com.example.backend.product.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductOptionRequest {
    @NotBlank(message = "옵션명은 필수 항목입니다.")
    private String name;

    @NotNull(message = "추가 가격은 필수 항목입니다.")
    @Min(value = 0, message = "추가 가격은 0보다 커야 합니다.")
    private Double additionalPrice;

    @NotBlank(message = "SKU는 필수 항목입니다.")
    private String sku;

    @NotNull(message = "재고는 필수 항목입니다.")
    @Min(value = 0, message = "재고는 0보다 커야 합니다.")
    private Integer stock;

    @NotNull(message = "표시 순서는 필수 항목입니다.")
    private Integer displayOrder;
} 