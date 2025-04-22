package com.preonboarding.challenge.controller;

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

        // 정렬 처리
        Sort sortReq = Utils.createBasicSortBySortParams(sort);

        // 페이지네이션 (0-based를 1-based로 변환)
        Pageable pageable = PageRequest.of(page - 1, perPage, sortReq);

        // 서비스 호출
        CategoryDto.CategoryProductsResponse response =
                categoryService.getCategoryProducts(id, includeSubcategories, pageable);

        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "카테고리 상품 목록을 성공적으로 조회했습니다."
                )
        );
    }
}