package com.preonboarding.challenge.service;

import com.preonboarding.challenge.service.dto.PaginationDto;
import com.preonboarding.challenge.service.dto.ReviewDto;

public interface ReviewService {

    // 리뷰 관리
    ReviewDto.ReviewPageResponse getProductReviews(
            Long productId,
            Integer rating,
            PaginationDto.PaginationRequest paginationRequest
    );

    ReviewDto.ReviewResponse createReview(Long productId, Long userId, ReviewDto.CreateRequest request);

    ReviewDto.ReviewResponse updateReview(Long reviewId, Long userId, ReviewDto.UpdateRequest request);

    void deleteReview(Long reviewId, Long userId);
}