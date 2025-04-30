package com.example.backend.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductImageRequest {
    @NotBlank(message = "이미지 URL은 필수 항목입니다.")
    private String url;

    @NotBlank(message = "대체 텍스트는 필수 항목입니다.")
    private String altText;

    @NotNull(message = "주요 이미지 여부는 필수 항목입니다.")
    private Boolean isPrimary;

    @NotNull(message = "표시 순서는 필수 항목입니다.")
    private Integer displayOrder;

    private Long optionId;
} 