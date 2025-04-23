package com.preonboarding.challenge.controller.mapper;

import com.preonboarding.challenge.controller.dto.ProductCreateRequest;
import com.preonboarding.challenge.controller.dto.ProductImageRequest;
import com.preonboarding.challenge.controller.dto.ProductOptionRequest;
import com.preonboarding.challenge.controller.dto.ProductUpdateRequest;
import com.preonboarding.challenge.service.dto.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductControllerMapper {

    // Controller DTO → Service DTO 매핑
    @Mapping(target = "detail", source = "detail")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "optionGroups", source = "optionGroups")
    @Mapping(target = "images", source = "images")
    @Mapping(target = "categories", source = "categories")
    ProductDto.CreateRequest toServiceDto(ProductCreateRequest request);

    @Mapping(target = "detail", source = "detail")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "categories", source = "categories")
    ProductDto.UpdateRequest toServiceUpdateDto(ProductUpdateRequest request);

    ProductDto.OptionRequest toServiceOptionRequest(ProductOptionRequest request);

    ProductDto.ImageRequest toServiceImageRequest(ProductImageRequest request);

    @Mapping(target = "pagination.page", source = "page")
    @Mapping(target = "pagination.size", source = "perPage")
    @Mapping(target = "pagination.sort", source = "sort")
    ProductDto.ProductListRequest toServiceListRequest(com.preonboarding.challenge.controller.dto.ProductListRequest request);
}