package com.example.backend.product.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCreateRequest {
    @NotBlank(message = "상품명은 필수 항목입니다.")
    private String name;

    @NotBlank(message = "슬러그는 필수 항목입니다.")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "슬러그는 소문자, 숫자, 하이픈만 사용할 수 있습니다.")
    private String slug;

    @NotBlank(message = "간단 설명은 필수 항목입니다.")
    private String shortDescription;

    @NotBlank(message = "상세 설명은 필수 항목입니다.")
    private String fullDescription;

    @NotNull(message = "판매자 ID는 필수 항목입니다.")
    private Long sellerId;

    @NotNull(message = "브랜드 ID는 필수 항목입니다.")
    private Long brandId;

    @NotBlank(message = "상품 상태는 필수 항목입니다.")
    private String status;

    @Valid
    @NotNull(message = "상품 상세 정보는 필수 항목입니다.")
    private ProductDetailRequest detail;

    @Valid
    @NotNull(message = "가격 정보는 필수 항목입니다.")
    private ProductPriceRequest price;

    @NotEmpty(message = "최소 하나의 카테고리를 지정해야 합니다.")
    private List<@Valid ProductCategoryRequest> categories;

    private List<@Valid ProductOptionGroupRequest> optionGroups;

    private List<@Valid ProductImageRequest> images;

    private List<Long> tags;
} 