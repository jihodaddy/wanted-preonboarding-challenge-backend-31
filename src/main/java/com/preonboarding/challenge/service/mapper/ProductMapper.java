package com.preonboarding.challenge.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.preonboarding.challenge.entity.*;
import com.preonboarding.challenge.service.dto.ProductDto;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ProductMapper {

    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Mapping(target = "seller", source = "seller")
    @Mapping(target = "brand", source = "brand")
    @Mapping(target = "detail", source = "detail")
    @Mapping(target = "price", expression = "java(mapPriceInfo(product.getPrice()))")
    @Mapping(target = "categories", expression = "java(mapCategories(product))")
    @Mapping(target = "optionGroups", expression = "java(mapOptionGroups(product.getOptionGroups()))")
    @Mapping(target = "images", expression = "java(mapImages(product.getImages()))")
    @Mapping(target = "tags", expression = "java(mapTags(product.getTags()))")
    @Mapping(target = "rating", expression = "java(mapRatingSummary(product.getReviews()))")
    @Mapping(target = "relatedProducts", ignore = true) // 필요시 별도 매핑
    public abstract ProductDto.ProductDetail toProductDetail(Product product);

    // Product → ProductSummary
    @Mapping(target = "primaryImage", expression = "java(mapPrimaryImage(product))")
    @Mapping(target = "brand", expression = "java(mapBrand(product.getBrand()))")
    @Mapping(target = "seller", expression = "java(mapSeller(product.getSeller()))")
    @Mapping(target = "rating", expression = "java(calculateAverageRating(product.getReviews()))")
    @Mapping(target = "reviewCount", expression = "java(product.getReviews() != null ? product.getReviews().size() : 0)")
    @Mapping(target = "inStock", expression = "java(checkInStock(product))")
    @Mapping(target = "basePrice", expression = "java(product.getPrice() != null ? product.getPrice().getBasePrice() : null)")
    @Mapping(target = "salePrice", expression = "java(product.getPrice() != null ? product.getPrice().getSalePrice() : null)")
    @Mapping(target = "currency", expression = "java(product.getPrice() != null ? product.getPrice().getCurrency() : null)")
    public abstract ProductDto.ProductSummary toProductSummary(Product product);

    // Product 리스트를 ProductSummary 리스트로 변환
    public abstract List<ProductDto.ProductSummary> toProductSummaryList(List<Product> products);


    @Mapping(target = "id", source = "id")
    @Mapping(target = "url", source = "url")
    @Mapping(target = "altText", source = "altText")
    @Mapping(target = "isPrimary", source = "primary")
    @Mapping(target = "displayOrder", source = "displayOrder")
    @Mapping(target = "optionId", expression = "java(image.getOption() != null ? image.getOption().getId() : null)")
    public abstract ProductDto.ImageResponse toImageResponse(ProductImage image);

    // DTO to Entity mappings (new entities)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "detail", ignore = true)
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "optionGroups", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "status", expression = "java(mapProductStatus(request.getStatus()))")
    public abstract Product toProductEntity(ProductDto.CreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", source = "product")
    @Mapping(target = "dimensions", expression = "java(convertMapToJsonString(detail.getDimensions()))")
    @Mapping(target = "additionalInfo", expression = "java(convertMapToJsonString(detail.getAdditionalInfo()))")
    public abstract ProductDetail toProductDetailEntity(ProductDto.ProductDetailDto detail, Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", source = "product")
    public abstract ProductPrice toProductPriceEntity(ProductDto.ProductPriceDto price, Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", source = "product")
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "options", ignore = true)
    public abstract ProductOptionGroup toOptionGroupEntity(ProductDto.OptionGroupDto dto, Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "optionGroup", source = "optionGroup")
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "displayOrder", source = "dto.displayOrder")
    @Mapping(target = "images", ignore = true)
    public abstract ProductOption toOptionEntity(ProductDto.OptionDto dto, ProductOptionGroup optionGroup);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", source = "product")
    @Mapping(target = "displayOrder", source = "dto.displayOrder")
    @Mapping(target = "option", source = "option")
    public abstract ProductImage toImageEntity(ProductDto.ImageRequest dto, Product product, ProductOption option);

    // Update entity mappings
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "detail", ignore = true)
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "optionGroups", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "status", expression = "java(request.getStatus() != null ? mapProductStatus(request.getStatus()) : product.getStatus())")
    public abstract void updateProductEntity(ProductDto.UpdateRequest request, @MappingTarget Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "dimensions", expression = "java(detail.getDimensions() != null ? convertMapToJsonString(detail.getDimensions()) : productDetail.getDimensions())")
    @Mapping(target = "additionalInfo", expression = "java(detail.getAdditionalInfo() != null ? convertMapToJsonString(detail.getAdditionalInfo()) : productDetail.getAdditionalInfo())")
    public abstract void updateProductDetailEntity(ProductDto.ProductDetailDto detail, @MappingTarget ProductDetail productDetail);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    public abstract void updateProductPriceEntity(ProductDto.ProductPriceDto price, @MappingTarget ProductPrice productPrice);

    // Response mappings
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "slug", source = "slug")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    public abstract ProductDto.CreateResponse toCreateResponse(Product product);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "slug", source = "slug")
    @Mapping(target = "updatedAt", source = "updatedAt")
    public abstract ProductDto.UpdateResponse toUpdateResponse(Product product);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "optionGroupId", source = "optionGroup.id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "additionalPrice", source = "additionalPrice")
    @Mapping(target = "sku", source = "sku")
    @Mapping(target = "stock", source = "stock")
    @Mapping(target = "displayOrder", source = "displayOrder")
    public abstract ProductDto.OptionResponse toOptionResponse(ProductOption option);

    // Helper methods that need to be implemented
    protected ProductDto.PriceInfoDto mapPriceInfo(ProductPrice price) {
        if (price == null) {
            return null;
        }

        return ProductDto.PriceInfoDto.builder()
                .basePrice(price.getBasePrice())
                .salePrice(price.getSalePrice())
                .currency(price.getCurrency())
                .taxRate(price.getTaxRate())
                .discountPercentage(price.getDiscountPercentage())
                .build();
    }

    protected List<ProductDto.CategoryDto> mapCategories(Product product) {
        if (product.getCategories() == null) {
            return List.of();
        }

        return product.getCategories().stream()
                .map(category -> {
                    ProductDto.CategoryDto dto = new ProductDto.CategoryDto();
                    dto.setId(category.getId());
                    dto.setName(category.getName());
                    dto.setSlug(category.getSlug());

                    if (category.getParent() != null) {
                        ProductDto.ParentCategoryDto parentDto = new ProductDto.ParentCategoryDto();
                        parentDto.setId(category.getParent().getId());
                        parentDto.setName(category.getParent().getName());
                        parentDto.setSlug(category.getParent().getSlug());
                        dto.setParent(parentDto);
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    protected List<ProductDto.OptionGroupResponseDto> mapOptionGroups(List<ProductOptionGroup> groups) {
        if (groups == null) {
            return List.of();
        }

        return groups.stream()
                .map(group -> {
                    ProductDto.OptionGroupResponseDto dto = new ProductDto.OptionGroupResponseDto();
                    dto.setId(group.getId());
                    dto.setName(group.getName());
                    dto.setDisplayOrder(group.getDisplayOrder());

                    List<ProductDto.OptionDto> options = group.getOptions().stream()
                            .map(option -> {
                                ProductDto.OptionDto optionDto = new ProductDto.OptionDto();
                                optionDto.setId(option.getId());
                                optionDto.setOptionGroupId(group.getId());
                                optionDto.setName(option.getName());
                                optionDto.setAdditionalPrice(option.getAdditionalPrice());
                                optionDto.setSku(option.getSku());
                                optionDto.setStock(option.getStock());
                                optionDto.setDisplayOrder(option.getDisplayOrder());
                                return optionDto;
                            })
                            .collect(Collectors.toList());

                    dto.setOptions(options);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    protected List<ProductDto.ImageResponse> mapImages(List<ProductImage> images) {
        if (images == null) {
            return List.of();
        }

        return images.stream()
                .map(this::toImageResponse)
                .collect(Collectors.toList());
    }

    protected List<ProductDto.TagDto> mapTags(List<Tag> tags) {
        if (tags == null) {
            return List.of();
        }

        return tags.stream()
                .map(tag -> {
                    ProductDto.TagDto dto = new ProductDto.TagDto();
                    dto.setId(tag.getId());
                    dto.setName(tag.getName());
                    dto.setSlug(tag.getSlug());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    protected ProductDto.RatingSummaryDto mapRatingSummary(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return null;
        }

        double average = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        Map<Integer, Integer> distribution = reviews.stream()
                .collect(Collectors.groupingBy(
                        Review::getRating,
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));

        return ProductDto.RatingSummaryDto.builder()
                .average(average)
                .count(reviews.size())
                .distribution(distribution)
                .build();
    }

    protected ProductStatus mapProductStatus(String status) {
        if (status == null) {
            return ProductStatus.ACTIVE;
        }

        return switch (status.toUpperCase()) {
            case "OUT_OF_STOCK" -> ProductStatus.OUT_OF_STOCK;
            case "DELETED" -> ProductStatus.DELETED;
            default -> ProductStatus.ACTIVE;
        };
    }

    // 헬퍼 메서드: 대표 이미지 매핑
    protected ProductDto.ImageDto mapPrimaryImage(Product product) {
        if (product.getImages() == null || product.getImages().isEmpty()) {
            return null;
        }

        // isPrimary가 true인 이미지를 찾음
        return product.getImages().stream()
                .filter(ProductImage::isPrimary)
                .findFirst()
                .map(image -> ProductDto.ImageDto.builder()
                        .url(image.getUrl())
                        .altText(image.getAltText())
                        .build())
                .orElse(null);
    }

    // 헬퍼 메서드: 브랜드 매핑
    protected ProductDto.BrandDto mapBrand(Brand brand) {
        if (brand == null) {
            return null;
        }

        return ProductDto.BrandDto.builder()
                .id(brand.getId())
                .name(brand.getName())
                .build();
    }

    // 헬퍼 메서드: 판매자 매핑
    protected ProductDto.SellerDto mapSeller(Seller seller) {
        if (seller == null) {
            return null;
        }

        return ProductDto.SellerDto.builder()
                .id(seller.getId())
                .name(seller.getName())
                .build();
    }

    // 헬퍼 메서드: 평균 평점 계산
    protected Double calculateAverageRating(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }

        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }

    // 헬퍼 메서드: 재고 여부 확인
    protected boolean checkInStock(Product product) {
        if (product.getStatus() == ProductStatus.OUT_OF_STOCK) {
            return false;
        }

        // 모든 옵션의 재고 확인
        if (product.getOptionGroups() != null && !product.getOptionGroups().isEmpty()) {
            boolean hasStock = product.getOptionGroups().stream()
                    .flatMap(group -> group.getOptions().stream())
                    .anyMatch(option -> option.getStock() > 0);

            return hasStock;
        }

        // 옵션이 없는 경우 기본적으로 재고 있음으로 간주
        return true;
    }

    protected String convertMapToJsonString(Map<String, Object> map) {
        if (map == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting Map to JSON string", e);
        }
    }

    @SuppressWarnings("unchecked")
    protected Map<String, Object> convertJsonStringToMap(String json) {
        if (json == null || json.isEmpty()) {
            return new HashMap<>();
        }

        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON string to Map", e);
        }
    }

    // 추가: ProductDetail 직접 매핑을 위한 메서드
    @AfterMapping
    protected void afterMappingProductDetail(Product product, @MappingTarget ProductDto.ProductDetail target) {
        if (product.getDetail() != null) {
            // dimensions와 additionalInfo 필드가 별도 처리되도록 이미 설정됨
            target.getDetail().setWeight(product.getDetail().getWeight());
            target.getDetail().setMaterials(product.getDetail().getMaterials());
            target.getDetail().setCountryOfOrigin(product.getDetail().getCountryOfOrigin());
            target.getDetail().setWarrantyInfo(product.getDetail().getWarrantyInfo());
            target.getDetail().setCareInstructions(product.getDetail().getCareInstructions());
        }
    }
}