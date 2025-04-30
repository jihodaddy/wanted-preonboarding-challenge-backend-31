package com.example.backend.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ProductUpdateRequest {
    @NotBlank(message = "상품명은 필수입니다.")
    private String name;

    @NotNull(message = "카테고리 ID는 필수입니다.")
    @Positive(message = "카테고리 ID는 양수여야 합니다.")
    private Long categoryId;

    @NotNull(message = "브랜드 ID는 필수입니다.")
    @Positive(message = "브랜드 ID는 양수여야 합니다.")
    private Long brandId;

    @NotNull(message = "판매자 ID는 필수입니다.")
    @Positive(message = "판매자 ID는 양수여야 합니다.")
    private Long sellerId;

    @NotBlank(message = "상품 설명은 필수입니다.")
    private String description;

    private List<ProductOptionUpdateRequest> options;

    @Getter
    @NoArgsConstructor
    public static class ProductOptionUpdateRequest {
        @NotNull(message = "옵션 ID는 필수입니다.")
        @Positive(message = "옵션 ID는 양수여야 합니다.")
        private Long id;

        @NotBlank(message = "옵션명은 필수입니다.")
        private String name;

        @NotNull(message = "재고는 필수입니다.")
        @Positive(message = "재고는 양수여야 합니다.")
        private Integer stock;

        @NotNull(message = "가격은 필수입니다.")
        @Positive(message = "가격은 양수여야 합니다.")
        private Double price;
    }
}
