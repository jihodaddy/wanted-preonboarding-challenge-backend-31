package com.preonboarding.challenge.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductImageRequest {
    private String url;
    private String altText;
    private boolean isPrimary;
    private Integer displayOrder;
    private Long optionId;
}