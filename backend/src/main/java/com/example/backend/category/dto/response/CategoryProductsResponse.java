package com.example.backend.category.dto.response;

import java.util.List;

import com.example.backend.common.dto.PaginationResponse;
import com.example.backend.product.dto.response.ProductListItemResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryProductsResponse {
    private CategoryResponse category;
    private List<ProductListItemResponse> items;
    private PaginationResponse pagination;
} 