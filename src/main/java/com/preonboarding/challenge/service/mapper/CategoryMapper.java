package com.preonboarding.challenge.service.mapper;

import com.preonboarding.challenge.entity.Category;
import com.preonboarding.challenge.service.dto.CategoryDto;
import com.preonboarding.challenge.service.dto.PaginationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {

    // Category → CategoryResponse (계층 구조 포함)
    @Mapping(target = "children", ignore = true)
    CategoryDto.CategoryResponse toCategoryResponse(Category category);

    // Category → CategoryDetail (부모 정보 포함)
    @Mapping(target = "parent", source = "parent")
    CategoryDto.CategoryDetail toCategoryDetail(Category category);

    // 페이지네이션 정보 매핑
    default PaginationDto.PaginationInfo toPaginationInfo(Page<?> page) {
        return PaginationDto.PaginationInfo.builder()
                .totalItems((int) page.getTotalElements())
                .totalPages(page.getTotalPages())
                .currentPage(page.getNumber() + 1) // 0-based to 1-based
                .perPage(page.getSize())
                .build();
    }
}