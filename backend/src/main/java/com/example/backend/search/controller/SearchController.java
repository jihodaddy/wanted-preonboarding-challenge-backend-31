package com.example.backend.search.controller;

import com.example.backend.common.exception.BusinessException;
import com.example.backend.common.exception.ErrorCode;
import com.example.backend.search.dto.SearchRequestDto;
import com.example.backend.search.dto.SearchResponseDto;
import com.example.backend.search.service.SearchService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public ResponseEntity<SearchResponseDto> searchProducts(@ModelAttribute @Valid SearchRequestDto request) {
        if (request.getQ() == null || request.getQ().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "검색어는 필수 항목입니다.");
        }
        SearchResponseDto response = searchService.searchProducts(request);
        return ResponseEntity.ok(response);
    }
} 