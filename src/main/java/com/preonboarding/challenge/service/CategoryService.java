package com.preonboarding.challenge.service;

import com.preonboarding.challenge.service.dto.CategoryDto;
import com.preonboarding.challenge.service.dto.PaginationDto;

import java.util.List;

public interface CategoryService {

    // 카테고리 목록 조회 (계층 구조 포함)
    List<CategoryDto.CategoryResponse> getAllCategories(Integer level);

    // 특정 카테고리의 상품 목록 조회
    CategoryDto.CategoryProductsResponse getCategoryProducts(
            Long categoryId,
            Boolean includeSubcategories,
            PaginationDto.PaginationRequest paginationRequest
    );

    // 특정 카테고리 조회 (하위 카테고리 포함)
    CategoryDto.CategoryResponse getCategoryById(Long categoryId);
}