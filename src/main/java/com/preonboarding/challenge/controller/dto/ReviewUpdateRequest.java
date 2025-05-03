package com.preonboarding.challenge.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewUpdateRequest {
    private Integer rating;
    private String title;
    private String content;
}