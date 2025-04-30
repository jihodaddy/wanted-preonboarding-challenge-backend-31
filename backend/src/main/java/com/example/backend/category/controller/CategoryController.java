package com.example.backend.category.controller;

import com.example.backend.category.dto.response.CategoryProductsResponse;
import com.example.backend.category.dto.response.CategoryResponse;
import com.example.backend.category.service.CategoryService;
import com.example.backend.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategories(
            @RequestParam(required = false) Integer level) {
        List<CategoryResponse> categories = categoryService.getCategories(level);
        return ResponseEntity.ok(ApiResponse.success(categories, "카테고리 목록을 성공적으로 조회했습니다."));
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<ApiResponse<CategoryProductsResponse>> getCategoryProducts(
            @PathVariable Long id,
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(defaultValue = "true") boolean includeSubcategories) {
            CategoryProductsResponse category = categoryService.getCategoryProducts(id, pageable, includeSubcategories);
        return ResponseEntity.ok(ApiResponse.success(category, "카테고리 상품 목록을 성공적으로 조회했습니다."));
    }
} 