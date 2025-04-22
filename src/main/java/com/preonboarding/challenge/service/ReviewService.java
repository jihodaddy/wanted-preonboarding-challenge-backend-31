package com.preonboarding.challenge.service;

import com.preonboarding.challenge.service.dto.ReviewDto;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

    // 리뷰 관리
    ReviewDto.ReviewPageResponse getProductReviews(Long productId, Integer rating, Pageable pageable);

    ReviewDto.ReviewResponse createReview(Long productId, Long userId, ReviewDto.CreateRequest request);

    ReviewDto.ReviewResponse updateReview(Long reviewId, Long userId, ReviewDto.UpdateRequest request);

    void deleteReview(Long reviewId, Long userId);
}