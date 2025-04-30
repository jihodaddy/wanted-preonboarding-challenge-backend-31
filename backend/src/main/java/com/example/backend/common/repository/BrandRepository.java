package com.example.backend.common.repository;

import com.example.backend.common.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    @Query(value = """
            SELECT b.*, COUNT(p.id) as count
            FROM brands b
            JOIN products p ON p.brand_id = b.id
            WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            GROUP BY b.id
            ORDER BY count DESC
            """, nativeQuery = true)
    List<Object[]> findBrandsWithProductCount(@Param("keyword") String keyword);
} 