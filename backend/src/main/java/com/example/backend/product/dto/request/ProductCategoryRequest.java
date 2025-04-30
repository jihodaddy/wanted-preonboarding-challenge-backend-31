package com.example.backend.product.dto.request;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class ProductCategoryRequest {
    @NotNull(message = "카테고리 ID는 필수 항목입니다.")
    private Long categoryId;

    @NotNull(message = "주요 카테고리 여부는 필수 항목입니다.")
    private Boolean isPrimary;
} 