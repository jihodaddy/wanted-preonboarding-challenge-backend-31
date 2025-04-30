package com.example.backend.product.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCategoryResponse {
    private Long id;
    private String name;
    private String slug;
    private Boolean isPrimary;
    private CategoryParentResponse parent;
} 