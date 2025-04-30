package com.example.backend.product.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductOptionGroupRequest {
    @NotBlank(message = "옵션 그룹명은 필수 항목입니다.")
    private String name;

    @NotNull(message = "표시 순서는 필수 항목입니다.")
    private Integer displayOrder;

    @Valid
    @NotNull(message = "옵션 목록은 필수 항목입니다.")
    private List<ProductOptionRequest> options;
} 