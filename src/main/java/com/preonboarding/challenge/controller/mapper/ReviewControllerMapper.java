package com.preonboarding.challenge.controller.mapper;

import com.preonboarding.challenge.controller.dto.ReviewCreateRequest;
import com.preonboarding.challenge.controller.dto.ReviewUpdateRequest;
import com.preonboarding.challenge.service.dto.ReviewDto;
import org.springframework.stereotype.Component;

@Component
public class ReviewControllerMapper {

    public ReviewDto.CreateRequest toReviewDtoCreateRequest(ReviewCreateRequest request) {
        return ReviewDto.CreateRequest.builder()
                .rating(request.getRating())
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }

    public ReviewDto.UpdateRequest toReviewDtoUpdateRequest(ReviewUpdateRequest request) {
        return ReviewDto.UpdateRequest.builder()
                .rating(request.getRating())
                .content(request.getContent())
                .build();
    }

}