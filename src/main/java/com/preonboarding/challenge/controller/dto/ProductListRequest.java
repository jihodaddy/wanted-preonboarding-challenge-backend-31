package com.preonboarding.challenge.controller.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class ProductListRequest {
    private Integer page = 1;
    private Integer perPage = 10;
    private String sort = "created_at:desc";
    private String status;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<Long> category;
    private Long seller;
    private Long brand;
    private Boolean inStock;
    private List<Long> tag;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdTo;
}