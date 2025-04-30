package com.example.backend.product.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductOptionGroupResponse {
    private Long id;
    private String name;
    private Integer displayOrder;
    private List<ProductOptionResponse> options;
} 