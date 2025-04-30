package com.example.backend.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaginationResponse {
    private Integer totalItems;
    private Integer totalPages;
    private Long currentPage;
    private Integer perPage;
} 