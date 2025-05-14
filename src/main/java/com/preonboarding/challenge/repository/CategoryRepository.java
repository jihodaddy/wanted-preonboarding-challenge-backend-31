package com.preonboarding.challenge.repository;

import com.preonboarding.challenge.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // 레벨별 카테고리 조회
    List<Category> findByLevel(int level);
}