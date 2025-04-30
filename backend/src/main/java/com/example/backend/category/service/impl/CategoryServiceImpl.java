package com.example.backend.category.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.category.dto.response.CategoryParentResponse;
import com.example.backend.category.dto.response.CategoryProductsResponse;
import com.example.backend.category.dto.response.CategoryResponse;
import com.example.backend.category.service.CategoryService;
import com.example.backend.common.dto.PaginationResponse;
import com.example.backend.common.entity.Category;
import com.example.backend.common.entity.Product;
import com.example.backend.common.repository.CategoryRepository;
import com.example.backend.common.repository.ProductRepository;
import com.example.backend.product.dto.response.BrandResponse;
import com.example.backend.product.dto.response.ProductListItemResponse;
import com.example.backend.product.dto.response.SellerResponse;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public List<CategoryResponse> getCategories(Integer level) {
        List<Category> categories;
        if (level != null) {
            categories = categoryRepository.findByLevel(level);
        } else {
            categories = categoryRepository.findAll();
        }

        return categories.stream()
                .map(this::convertToCategoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryProductsResponse getCategoryProducts(Long categoryId, Pageable pageable, boolean includeSubcategories) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        CategoryResponse categoryResponse = convertToCategoryResponse(category);
        CategoryProductsResponse response = new CategoryProductsResponse();
        response.setCategory(categoryResponse);

        Page<Product> products;
        if (includeSubcategories) {
            // Get all subcategory IDs including the current category
            List<Long> categoryIds = getAllSubcategoryIds(category);
            products = productRepository.findByProductCategoriesIdIn(categoryIds, pageable);
        } else {
            products = productRepository.findByProductCategoriesId(categoryId, pageable);
        }

        response.setItems(products.getContent().stream()
                .map(this::convertToProductListItemResponse)
                .collect(Collectors.toList()));

        response.setPagination(new PaginationResponse(
                products.getNumber(),
                products.getSize(),
                products.getTotalElements(),
                products.getTotalPages()
        ));

        return response;
    }

    private List<Long> getAllSubcategoryIds(Category category) {
        List<Long> categoryIds = List.of(category.getId());
        List<Category> subcategories = categoryRepository.findByParentId(category.getId());
        if (!subcategories.isEmpty()) {
            categoryIds.addAll(subcategories.stream()
                    .map(Category::getId)
                    .collect(Collectors.toList()));
        }
        return categoryIds;
    }

    private CategoryResponse convertToCategoryResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setSlug(category.getSlug());
        response.setDescription(category.getDescription());
        response.setLevel(category.getLevel());
        response.setImageUrl(category.getImageUrl());

        if (category.getParent() != null) {
            CategoryParentResponse parentResponse = new CategoryParentResponse();
            parentResponse.setId(category.getParent().getId());
            parentResponse.setName(category.getParent().getName());
            parentResponse.setSlug(category.getParent().getSlug());
            response.setParent(parentResponse);
        }

        if (!category.getProductCategories().isEmpty()) {
            response.setChildren(category.getProductCategories().stream()
                    .map(pc -> {
                        CategoryResponse childResponse = new CategoryResponse();
                        childResponse.setId(pc.getCategory().getId());
                        childResponse.setName(pc.getCategory().getName());
                        childResponse.setSlug(pc.getCategory().getSlug());
                        return childResponse;
                    })
                    .collect(Collectors.toList()));
        }

        return response;
    }

    private ProductListItemResponse convertToProductListItemResponse(Product product) {
        return ProductListItemResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .shortDescription(product.getShortDescription())
                .basePrice(product.getProductPrice().getBasePrice())
                .salePrice(product.getProductPrice().getSalePrice())
                .currency(product.getProductPrice().getCurrency())
                .status(product.getStatus().name())
                .brand(BrandResponse.builder()
                        .id(product.getBrand().getId())
                        .name(product.getBrand().getName())
                        .description(product.getBrand().getDescription())
                        .logoUrl(product.getBrand().getLogoUrl())
                        .build())
                .seller(SellerResponse.builder()
                        .id(product.getSeller().getId())
                        .name(product.getSeller().getName())
                        .contactEmail(product.getSeller().getContactEmail())
                        .contactPhone(product.getSeller().getContactPhone())
                        .build())
                .createdAt(product.getCreatedAt())
                .build();
    }
} 