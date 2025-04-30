package com.example.backend.search.dto;

import java.util.List;

import com.example.backend.common.dto.PageInfo;
import com.example.backend.product.dto.ProductDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponseDto {
    private String keyword;
    private Long totalCount;
    private List<ProductDto> items;
    private PageInfo pagination;
    private SearchFiltersDto filters;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchFiltersDto {
        private List<CategoryFilterDto> categories;
        private List<BrandFilterDto> brands;
        private List<PriceRangeFilterDto> priceRanges;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryFilterDto {
        private Long id;
        private String name;
        private Long count;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BrandFilterDto {
        private Long id;
        private String name;
        private Long count;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceRangeFilterDto {
        private Integer min;
        private Integer max;
        private Long count;
    }
} 