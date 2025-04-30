package com.example.backend.category.service;

import com.example.backend.category.dto.response.CategoryResponse;
import com.example.backend.category.dto.response.CategoryProductsResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getCategories(Integer level);
    CategoryProductsResponse getCategoryProducts(Long categoryId, Pageable pageable, boolean includeSubcategories);
} 