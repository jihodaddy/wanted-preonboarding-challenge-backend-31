package com.preonboarding.challenge.controller.mapper;

import com.preonboarding.challenge.controller.dto.ReviewCreateRequest;
import com.preonboarding.challenge.controller.dto.ReviewUpdateRequest;
import com.preonboarding.challenge.service.dto.ReviewDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ReviewControllerMapper {

    ReviewDto.CreateRequest toServiceDto(ReviewCreateRequest request);

    ReviewDto.UpdateRequest toServiceUpdateDto(ReviewUpdateRequest request);
}