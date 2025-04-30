package com.example.backend.search.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequestDto {

    private String q; // 검색어
    private Integer page = 1;
    private Integer perPage = 10;
    private String sort = "createdAt:desc";
    private List<Long> category;
    private Integer minPrice;
    private Integer maxPrice;
    private List<Long> brand;
    private List<Long> seller;
    private Boolean inStock;
    private Double rating;
} 