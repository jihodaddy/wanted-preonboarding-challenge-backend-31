package com.preonboarding.challenge.controller;

import com.preonboarding.challenge.service.dto.PaginationDto;
import com.preonboarding.challenge.service.dto.Utils;
import com.preonboarding.challenge.service.CategoryService;
import com.preonboarding.challenge.service.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getAllCategories(@RequestParam(required = false) Integer level) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        categoryService.getAllCategories(level),
                        "카테고리 목록을 성공적으로 조회했습니다."
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        CategoryDto.CategoryResponse category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(
                ApiResponse.success(
                        category,
                        "카테고리 정보를 성공적으로 조회했습니다."
                )
        );
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<?> getCategoryProducts(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "created_at:desc") String sort,
            @RequestParam(defaultValue = "true") Boolean includeSubcategories) {

        var paginationRequest = PaginationDto.PaginationRequest.builder()
                .page(page)
                .size(perPage)
                .sort(sort)
                .build();

        // 서비스 호출
        CategoryDto.CategoryProductsResponse response =
                categoryService.getCategoryProducts(id, includeSubcategories, paginationRequest);

        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "카테고리 상품 목록을 성공적으로 조회했습니다."
                )
        );
    }
}