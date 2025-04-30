package com.example.backend.product.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ProductRatingResponse {
    private Double average;
    private Integer count;
    private Map<Integer, Integer> distribution;
} 