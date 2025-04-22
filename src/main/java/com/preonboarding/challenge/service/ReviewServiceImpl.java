package com.preonboarding.challenge.service;

import com.preonboarding.challenge.entity.Product;
import com.preonboarding.challenge.entity.Review;
import com.preonboarding.challenge.entity.User;
import com.preonboarding.challenge.exception.AccessDeniedException;
import com.preonboarding.challenge.exception.ResourceNotFoundException;
import com.preonboarding.challenge.repository.ProductRepository;
import com.preonboarding.challenge.repository.ReviewRepository;
import com.preonboarding.challenge.repository.UserRepository;
import com.preonboarding.challenge.service.dto.ReviewDto;
import com.preonboarding.challenge.service.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewDto.ReviewResponse> getProductReviews(Long productId, Integer rating, Pageable pageable) {
        // 상품 존재 확인
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        // 평점 필터 적용
        Page<Review> reviewPage;
        if (rating != null) {
            reviewPage = reviewRepository.findByProductIdAndRating(productId, rating, pageable);
        } else {
            reviewPage = reviewRepository.findByProductId(productId, pageable);
        }

        // DTO 변환
        return reviewPage.map(reviewMapper::toReviewResponse);
    }

    @Override
    @Transactional
    public ReviewDto.ReviewResponse createReview(Long productId, Long userId, ReviewDto.CreateRequest request) {
        // 상품 존재 확인
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        // 사용자 존재 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        // 리뷰 생성
        Review review = Review.builder()
                .product(product)
                .user(user)
                .rating(request.getRating())
                .title(request.getTitle())
                .content(request.getContent())
                .verifiedPurchase(true) // 실제로는 구매 여부 확인 로직이 필요
                .helpfulVotes(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // 저장
        review = reviewRepository.save(review);

        // 응답 변환
        return reviewMapper.toReviewResponse(review);
    }

    @Override
    @Transactional
    public ReviewDto.ReviewResponse updateReview(Long reviewId, Long userId, ReviewDto.UpdateRequest request) {
        // 리뷰 존재 확인
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", reviewId));

        // 권한 확인 (리뷰 작성자만 수정 가능)
        if (!review.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("다른 사용자의 리뷰를 수정할 권한이 없습니다.");
        }

        // 리뷰 수정
        if (request.getRating() != null) {
            review.setRating(request.getRating());
        }

        if (request.getTitle() != null) {
            review.setTitle(request.getTitle());
        }

        if (request.getContent() != null) {
            review.setContent(request.getContent());
        }

        review.setUpdatedAt(LocalDateTime.now());

        // 저장
        review = reviewRepository.save(review);

        // 응답 변환
        return reviewMapper.toReviewResponse(review);
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        // 리뷰 존재 확인
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", reviewId));

        // 권한 확인 (리뷰 작성자만 삭제 가능)
        if (!review.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("다른 사용자의 리뷰를 삭제할 권한이 없습니다.");
        }

        // 리뷰 삭제
        reviewRepository.delete(review);
    }
}