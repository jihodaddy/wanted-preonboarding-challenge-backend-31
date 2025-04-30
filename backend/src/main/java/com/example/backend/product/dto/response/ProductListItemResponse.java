package com.example.backend.product.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductListItemResponse {
    private Long id;
    private String name;
    private String slug;
    private String shortDescription;
    private Double basePrice;
    private Double salePrice;
    private String currency;
    private String status;
    private LocalDateTime createdAt;
    private ProductImageResponse primaryImage;
    private BrandResponse brand;
    private SellerResponse seller;
    private Double rating;
    private int reviewCount;
    private boolean inStock;
} 