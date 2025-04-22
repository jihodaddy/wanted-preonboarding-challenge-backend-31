package com.preonboarding.challenge.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class CategoryDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CategoryResponse {
        private Long id;
        private String name;
        private String slug;
        private String description;
        private Integer level;
        private String imageUrl;
        @Builder.Default
        private List<CategoryResponse> children = new ArrayList<>();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CategoryDetail {
        private Long id;
        private String name;
        private String slug;
        private String description;
        private Integer level;
        private String imageUrl;
        private ParentCategoryDto parent;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ParentCategoryDto {
        private Long id;
        private String name;
        private String slug;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CategoryProductsResponse {
        private CategoryDetail category;
        private List<ProductDto.ProductSummary> items;
        private PaginationInfo pagination;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PaginationInfo {
        private Integer totalItems;
        private Integer totalPages;
        private Integer currentPage;
        private Integer perPage;
    }
}