package com.example.backend.search.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.common.dto.PageInfo;
import com.example.backend.common.entity.Product;
import com.example.backend.common.entity.Review;
import com.example.backend.common.repository.BrandRepository;
import com.example.backend.common.repository.CategoryRepository;
import com.example.backend.common.repository.ProductRepository;
import com.example.backend.product.dto.ProductDto;
import com.example.backend.search.dto.SearchRequestDto;
import com.example.backend.search.dto.SearchResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    public SearchResponseDto searchProducts(SearchRequestDto request) {
        // 정렬 설정
        Sort sort = getSort(request.getSort());
        PageRequest pageRequest = PageRequest.of(request.getPage() - 1, request.getPerPage(), sort);

        // 상품 검색
        Page<Product> productPage = productRepository.searchProducts(
                request.getQ(),
                request.getCategory(),
                request.getMinPrice(),
                request.getMaxPrice(),
                request.getBrand(),
                request.getSeller(),
                request.getInStock(),
                request.getRating(),
                pageRequest
        );

        // 필터 정보 조회
        List<Object[]> categoryFilters = productRepository.getCategoryFilters(
                request.getQ(),
                request.getMinPrice(),
                request.getMaxPrice(),
                request.getBrand(),
                request.getSeller(),
                request.getInStock(),
                request.getRating()
        );

        List<Object[]> brandFilters = productRepository.getBrandFilters(
                request.getQ(),
                request.getCategory(),
                request.getMinPrice(),
                request.getMaxPrice(),
                request.getSeller(),
                request.getInStock(),
                request.getRating()
        );

        List<Object[]> priceRangeFilters = productRepository.getPriceRangeFilters(
                request.getQ(),
                request.getCategory(),
                request.getBrand(),
                request.getSeller(),
                request.getInStock(),
                request.getRating()
        );

        // 응답 생성
        SearchResponseDto response = new SearchResponseDto();
        response.setKeyword(request.getQ());
        response.setTotalCount(productPage.getTotalElements());
        response.setItems(productPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()));

        // 페이지네이션 정보 설정
        PageInfo pageInfo = new PageInfo();
        pageInfo.setTotalElements(productPage.getTotalElements());
        pageInfo.setTotalPages(productPage.getTotalPages());
        pageInfo.setPage(request.getPage());
        pageInfo.setSize(request.getPerPage());
        response.setPagination(pageInfo);

        // 필터 정보 설정
        SearchResponseDto.SearchFiltersDto filters = new SearchResponseDto.SearchFiltersDto();
        filters.setCategories(convertToCategoryFilters(categoryFilters));
        filters.setBrands(convertToBrandFilters(brandFilters));
        filters.setPriceRanges(convertToPriceRangeFilters(priceRangeFilters));
        response.setFilters(filters);

        return response;
    }

    private Sort getSort(String sortParam) {
        if (sortParam == null || sortParam.isEmpty()) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }

        String[] parts = sortParam.split(":");
        String field = parts[0];
        Sort.Direction direction = parts.length > 1 && "desc".equalsIgnoreCase(parts[1])
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return Sort.by(direction, field);
    }

    private ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setSlug(product.getSlug());
        dto.setShortDescription(product.getShortDescription());
        dto.setBasePrice(product.getProductPrice().getBasePrice());
        dto.setSalePrice(product.getProductPrice().getSalePrice());
        dto.setCurrency(product.getProductPrice().getCurrency());

        dto.setRating(calculateAverageRating(product.getReviews()));
        dto.setReviewCount(product.getReviews().size());

        boolean inStock = product.getOptionGroups().stream()
        .flatMap(group -> group.getOptions().stream())
        .anyMatch(option -> option.getStock() > 0);

        dto.setInStock(inStock);
        dto.setCreatedAt(product.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME));

        // 기본 이미지 설정
        product.getImages().stream()
                .filter(image -> image.getIsPrimary())
                .findFirst()
                .ifPresent(image -> {
                    ProductDto.ProductImageDto imageDto = new ProductDto.ProductImageDto();
                    imageDto.setUrl(image.getUrl());
                    imageDto.setAltText(image.getAltText());
                    dto.setPrimaryImage(imageDto);
                });

        // 브랜드 정보 설정
        if (product.getBrand() != null) {
            ProductDto.BrandDto brandDto = new ProductDto.BrandDto();
            brandDto.setId(product.getBrand().getId());
            brandDto.setName(product.getBrand().getName());
            dto.setBrand(brandDto);
        }

        return dto;
    }

    private List<SearchResponseDto.CategoryFilterDto> convertToCategoryFilters(List<Object[]> results) {
        return results.stream()
                .map(result -> {
                    SearchResponseDto.CategoryFilterDto dto = new SearchResponseDto.CategoryFilterDto();
                    dto.setId((Long) result[0]);
                    dto.setName((String) result[1]);
                    dto.setCount((Long) result[2]);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private List<SearchResponseDto.BrandFilterDto> convertToBrandFilters(List<Object[]> results) {
        return results.stream()
                .map(result -> {
                    SearchResponseDto.BrandFilterDto dto = new SearchResponseDto.BrandFilterDto();
                    dto.setId((Long) result[0]);
                    dto.setName((String) result[1]);
                    dto.setCount((Long) result[2]);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private List<SearchResponseDto.PriceRangeFilterDto> convertToPriceRangeFilters(List<Object[]> results) {
        return results.stream()
                .map(result -> {
                    SearchResponseDto.PriceRangeFilterDto dto = new SearchResponseDto.PriceRangeFilterDto();
                    dto.setMin((Integer) result[0]);
                    dto.setMax((Integer) result[1]);
                    dto.setCount((Long) result[2]);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private Double calculateAverageRating(List<Review> reviews) {
        if (reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }
} 