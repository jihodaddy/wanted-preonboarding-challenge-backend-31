package com.preonboarding.challenge.repository;

import com.preonboarding.challenge.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    // 카테고리 ID로 상품 조회
    Page<Product> findByCategoriesId(Long categoryId, Pageable pageable);

    // 여러 카테고리 ID로 상품 조회 (하위 카테고리 포함)
    Page<Product> findByCategoriesIdIn(List<Long> categoryIds, Pageable pageable);

    // 모든 필터링 조건을 specification으로 대체할 수 있음
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);
}