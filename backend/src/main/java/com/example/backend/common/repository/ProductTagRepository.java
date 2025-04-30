package com.example.backend.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.common.entity.ProductTag;

@Repository
public interface ProductTagRepository extends JpaRepository<ProductTag, Long> {
} 