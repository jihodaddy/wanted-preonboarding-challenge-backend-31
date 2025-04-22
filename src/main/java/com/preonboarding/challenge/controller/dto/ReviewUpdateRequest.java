package com.preonboarding.challenge.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewUpdateRequest {
    private Integer rating;
    private String title;
    private String content;
}