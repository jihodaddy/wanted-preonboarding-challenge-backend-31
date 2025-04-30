package com.example.backend.product.dto.response;

import java.util.List;

import com.example.backend.common.dto.PaginationResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductListResponse {
    private List<ProductListItemResponse> items;
    private PaginationResponse pagination;
} 