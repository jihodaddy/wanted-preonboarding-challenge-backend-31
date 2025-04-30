package com.example.backend.product.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetailResponse {
    private Long id;
    private String name;
    private String slug;
    private String shortDescription;
    private String fullDescription;
    private SellerResponse seller;
    private BrandResponse brand;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ProductDetailInfoResponse detail;
    private ProductPriceResponse price;
    private List<ProductCategoryResponse> categories;
    private List<ProductOptionGroupResponse> optionGroups;
    private List<ProductImageResponse> images;
    private List<TagResponse> tags;
    private ProductRatingResponse rating;
    private List<RelatedProductResponse> relatedProducts;
} 