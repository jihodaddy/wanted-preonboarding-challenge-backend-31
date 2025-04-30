package com.example.backend.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String slug;
    private String shortDescription;
    private Double basePrice;
    private Double salePrice;
    private String currency;
    private Double rating;
    private Integer reviewCount;
    private Boolean inStock;
    private String createdAt;
    private ProductImageDto primaryImage;
    private BrandDto brand;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductImageDto {
        private String url;
        private String altText;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BrandDto {
        private Long id;
        private String name;
    }
} 