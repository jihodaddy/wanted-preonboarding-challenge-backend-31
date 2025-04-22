package com.preonboarding.challenge.service.mapper;

import com.preonboarding.challenge.entity.Review;
import com.preonboarding.challenge.entity.User;
import com.preonboarding.challenge.service.dto.ReviewDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ReviewMapper {

    @Mapping(target = "user", source = "user")
    ReviewDto.ReviewResponse toReviewResponse(Review review);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "avatarUrl", source = "avatarUrl")
    ReviewDto.UserDto toUserDto(User user);

    default ReviewDto.ReviewSummary toReviewSummary(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return ReviewDto.ReviewSummary.builder()
                    .averageRating(0.0)
                    .totalCount(0)
                    .distribution(Map.of())
                    .build();
        }

        // 평균 평점 계산
        double averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        // 평점별 분포 계산
        Map<Integer, Integer> distribution = reviews.stream()
                .collect(Collectors.groupingBy(
                        Review::getRating,
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));

        // 1-5 평점 모두 포함하도록 보정
        for (int i = 1; i <= 5; i++) {
            distribution.putIfAbsent(i, 0);
        }

        return ReviewDto.ReviewSummary.builder()
                .averageRating(averageRating)
                .totalCount(reviews.size())
                .distribution(distribution)
                .build();
    }

    default <T, R> ReviewDto.Page<R> toPageDto(org.springframework.data.domain.Page<T> page, List<R> content) {
        ReviewDto.PaginationInfo paginationInfo = ReviewDto.PaginationInfo.builder()
                .totalItems((int) page.getTotalElements())
                .totalPages(page.getTotalPages())
                .currentPage(page.getNumber() + 1) // 0-based to 1-based
                .perPage(page.getSize())
                .build();

        return ReviewDto.Page.<R>builder()
                .items(content)
                .pagination(paginationInfo)
                .build();
    }
}