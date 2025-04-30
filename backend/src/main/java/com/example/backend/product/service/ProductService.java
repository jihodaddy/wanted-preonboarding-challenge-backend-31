package com.example.backend.product.service;

import com.example.backend.product.dto.request.ProductCreateRequest;
import com.example.backend.product.dto.request.ProductUpdateRequest;
import com.example.backend.product.dto.response.ProductDetailResponse;
import com.example.backend.product.dto.response.ProductListResponse;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductDetailResponse createProduct(ProductCreateRequest request);
    ProductDetailResponse getProduct(Long id);
    ProductListResponse getProducts(String search, Long categoryId, Long brandId, Long sellerId, String status, Pageable pageable);
    ProductDetailResponse updateProduct(Long id, ProductUpdateRequest request);
    void deleteProduct(Long id);
    void updateProductStatus(Long id, String status);
    void updateProductStock(Long id, Long optionId, Integer quantity);
    void updateProductPrice(Long id, Long optionId, Double price);
} 