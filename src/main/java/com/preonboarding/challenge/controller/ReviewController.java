package com.preonboarding.challenge.controller;

import com.preonboarding.challenge.controller.dto.ReviewCreateRequest;
import com.preonboarding.challenge.controller.dto.ReviewUpdateRequest;
import com.preonboarding.challenge.controller.mapper.ReviewControllerMapper;
import com.preonboarding.challenge.service.ReviewService;
import com.preonboarding.challenge.service.dto.ReviewDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewControllerMapper mapper;

    public ReviewController(
            ReviewService reviewService,
            @Qualifier("reviewControllerMapperImpl") ReviewControllerMapper mapper
    ) {
        this.reviewService = reviewService;
        this.mapper = mapper;
    }

    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<?> getProductReviews(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "created_at:desc") String sort,
            @RequestParam(required = false) Integer rating) {

        // 정렬 처리
        String[] sortParams = sort.split(":");
        String sortField = convertToCamelCase(sortParams[0]); // snake_case -> camelCase 변환
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        // 페이지네이션 (0-based)
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, sortField));

        // 서비스 호출
        ReviewDto.ReviewPageResponse reviewsPage = reviewService.getProductReviews(productId, rating, pageable);

        return ResponseEntity.ok(ApiResponse.success(reviewsPage, "상품 리뷰를 성공적으로 조회했습니다."));
    }

    // snake_case -> camelCase 변환 메소드
    private String convertToCamelCase(String snakeCase) {
        if (snakeCase.contains("_")) {
            StringBuilder result = new StringBuilder();
            boolean capitalize = false;

            for (char c : snakeCase.toCharArray()) {
                if (c == '_') {
                    capitalize = true;
                } else if (capitalize) {
                    result.append(Character.toUpperCase(c));
                    capitalize = false;
                } else {
                    result.append(c);
                }
            }

            return result.toString();
        }

        return snakeCase; // 이미 camelCase인 경우 그대로 반환
    }

    @PostMapping("/products/{productId}/reviews")
    public ResponseEntity<?> createReview(
            @PathVariable Long productId,
            @RequestBody ReviewCreateRequest request) {

        // 인증 관련 로직 (실제로는 Spring Security 등을 통해 구현)
        Long userId = 1L; // 임시로 고정된 사용자 ID 사용

        // DTO 변환
        ReviewDto.CreateRequest serviceDto = mapper.toServiceDto(request);

        // 서비스 호출
        ReviewDto.ReviewResponse response = reviewService.createReview(productId, userId, serviceDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "리뷰가 성공적으로 등록되었습니다."));
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<?> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewUpdateRequest request) {

        // 인증 관련 로직 (실제로는 Spring Security 등을 통해 구현)
        Long userId = 1L; // 임시로 고정된 사용자 ID 사용

        // DTO 변환
        ReviewDto.UpdateRequest serviceDto = mapper.toServiceUpdateDto(request);

        // 서비스 호출
        ReviewDto.ReviewResponse response = reviewService.updateReview(reviewId, userId, serviceDto);

        return ResponseEntity.ok(ApiResponse.success(response, "리뷰가 성공적으로 수정되었습니다."));
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId) {

        // 인증 관련 로직 (실제로는 Spring Security 등을 통해 구현)
        Long userId = 1L; // 임시로 고정된 사용자 ID 사용

        // 서비스 호출
        reviewService.deleteReview(reviewId, userId);

        return ResponseEntity.ok(ApiResponse.success(null, "리뷰가 성공적으로 삭제되었습니다."));
    }
}