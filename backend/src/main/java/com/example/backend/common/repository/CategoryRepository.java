package com.example.backend.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backend.common.entity.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByLevel(Integer level);
    List<Category> findByParentId(Long parentId);

    @Query(value = """
            SELECT c.*, COUNT(p.id) as count
            FROM categories c
            JOIN product_categories pc ON pc.category_id = c.id
            JOIN products p ON p.id = pc.product_id
            WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            GROUP BY c.id
            ORDER BY count DESC
            """, nativeQuery = true)
    List<Object[]> findCategoriesWithProductCount(@Param("keyword") String keyword);
} 