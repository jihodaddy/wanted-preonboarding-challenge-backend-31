package com.example.backend.common.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backend.common.entity.Product;
import com.example.backend.enums.ProductStatus;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
        Optional<Product> findById(Long id);

        Page<Product> findByProductCategoriesId(Long categoryId, Pageable pageable);

        Page<Product> findByProductCategoriesIdIn(List<Long> categoryIds, Pageable pageable);

        Page<Product> findByNameContainingAndProductCategoriesIdAndBrandIdAndSellerIdAndStatus(
                        String search, Long productCategoryId, Long brandId, Long sellerId, ProductStatus status,
                        Pageable pageable);

        Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

        @Query(value = """
                        SELECT
                            FLOOR(base_price/10000)*10000 as min_price,
                            FLOOR(base_price/10000)*10000 + 9999 as max_price,
                            COUNT(*) as count
                        FROM products
                        WHERE LOWER(name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                        GROUP BY FLOOR(base_price/10000)
                        ORDER BY min_price
                        """, nativeQuery = true)
        List<Object[]> findPriceRangesWithCount(@Param("keyword") String keyword);

        @Query("SELECT p FROM Product p " +
                        "LEFT JOIN p.productPrice pp " +
                        "LEFT JOIN p.optionGroups og " +
                        "LEFT JOIN og.options o " +
                        "LEFT JOIN p.reviews r " +
                        "WHERE (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(p.shortDescription) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
                        "AND (:categoryIds IS NULL OR p.id IN " +
                        "(SELECT pc.product.id FROM ProductCategory pc WHERE pc.category.id IN :categoryIds)) " +
                        "AND (:minPrice IS NULL OR pp.basePrice >= :minPrice) " +
                        "AND (:maxPrice IS NULL OR pp.basePrice <= :maxPrice) " +
                        "AND (:brandIds IS NULL OR p.brand.id IN :brandIds) " +
                        "AND (:sellerIds IS NULL OR p.seller.id IN :sellerIds) " +
                        "AND (:inStock IS NULL OR " +
                        "CASE WHEN p.optionGroups IS EMPTY THEN true " +
                        "ELSE EXISTS (SELECT 1 FROM p.optionGroups og2 JOIN og2.options o2 WHERE o2.stock > 0) END = :inStock) "
                        +
                        "AND (:rating IS NULL OR " +
                        "COALESCE((SELECT AVG(r2.rating) FROM p.reviews r2), 0) >= :rating) " +
                        "AND p.status = 'ACTIVE' " +
                        "GROUP BY p.id")
        Page<Product> searchProducts(
                        @Param("keyword") String keyword,
                        @Param("categoryIds") List<Long> categoryIds,
                        @Param("minPrice") Integer minPrice,
                        @Param("maxPrice") Integer maxPrice,
                        @Param("brandIds") List<Long> brandIds,
                        @Param("sellerIds") List<Long> sellerIds,
                        @Param("inStock") Boolean inStock,
                        @Param("rating") Double rating,
                        Pageable pageable);

        @Query("SELECT c.id, c.name, COUNT(pc.product.id) " +
                        "FROM Category c " +
                        "LEFT JOIN ProductCategory pc ON c.id = pc.category.id " +
                        "WHERE pc.product.id IN " +
                        "(SELECT p.id FROM Product p " +
                        "LEFT JOIN p.productPrice pp " +
                        "LEFT JOIN p.optionGroups og " +
                        "LEFT JOIN og.options o " +
                        "LEFT JOIN p.reviews r " +
                        "WHERE (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(p.shortDescription) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
                        "AND (:minPrice IS NULL OR pp.basePrice >= :minPrice) " +
                        "AND (:maxPrice IS NULL OR pp.basePrice <= :maxPrice) " +
                        "AND (:brandIds IS NULL OR p.brand.id IN :brandIds) " +
                        "AND (:sellerIds IS NULL OR p.seller.id IN :sellerIds) " +
                        "AND (:inStock IS NULL OR " +
                        "CASE WHEN p.optionGroups IS EMPTY THEN true " +
                        "ELSE EXISTS (SELECT 1 FROM p.optionGroups og2 JOIN og2.options o2 WHERE o2.stock > 0) END = :inStock) "
                        +
                        "AND (:rating IS NULL OR " +
                        "COALESCE((SELECT AVG(r2.rating) FROM p.reviews r2), 0) >= :rating) " +
                        "AND p.status = 'ACTIVE') " +
                        "GROUP BY c.id, c.name")
        List<Object[]> getCategoryFilters(
                        @Param("keyword") String keyword,
                        @Param("minPrice") Integer minPrice,
                        @Param("maxPrice") Integer maxPrice,
                        @Param("brandIds") List<Long> brandIds,
                        @Param("sellerIds") List<Long> sellerIds,
                        @Param("inStock") Boolean inStock,
                        @Param("rating") Double rating);

        @Query("SELECT b.id, b.name, COUNT(p.id) " +
                        "FROM Brand b " +
                        "LEFT JOIN Product p ON b.id = p.brand.id " +
                        "LEFT JOIN p.productPrice pp " +
                        "LEFT JOIN p.optionGroups og " +
                        "LEFT JOIN og.options o " +
                        "LEFT JOIN p.reviews r " +
                        "WHERE (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(p.shortDescription) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
                        "AND (:categoryIds IS NULL OR p.id IN " +
                        "(SELECT pc.product.id FROM ProductCategory pc WHERE pc.category.id IN :categoryIds)) " +
                        "AND (:minPrice IS NULL OR pp.basePrice >= :minPrice) " +
                        "AND (:maxPrice IS NULL OR pp.basePrice <= :maxPrice) " +
                        "AND (:sellerIds IS NULL OR p.seller.id IN :sellerIds) " +
                        "AND (:inStock IS NULL OR " +
                        "CASE WHEN p.optionGroups IS EMPTY THEN true " +
                        "ELSE EXISTS (SELECT 1 FROM p.optionGroups og2 JOIN og2.options o2 WHERE o2.stock > 0) END = :inStock) "
                        +
                        "AND (:rating IS NULL OR " +
                        "COALESCE((SELECT AVG(r2.rating) FROM p.reviews r2), 0) >= :rating) " +
                        "AND p.status = 'ACTIVE' " +
                        "GROUP BY b.id, b.name")
        List<Object[]> getBrandFilters(
                        @Param("keyword") String keyword,
                        @Param("categoryIds") List<Long> categoryIds,
                        @Param("minPrice") Integer minPrice,
                        @Param("maxPrice") Integer maxPrice,
                        @Param("sellerIds") List<Long> sellerIds,
                        @Param("inStock") Boolean inStock,
                        @Param("rating") Double rating);

        @Query("SELECT " +
                        "CASE " +
                        "WHEN pp.basePrice < 300000 THEN 0 " +
                        "WHEN pp.basePrice < 600000 THEN 300000 " +
                        "ELSE 600000 " +
                        "END as minPrice, " +
                        "CASE " +
                        "WHEN pp.basePrice < 300000 THEN 300000 " +
                        "WHEN pp.basePrice < 600000 THEN 600000 " +
                        "ELSE NULL " +
                        "END as maxPrice, " +
                        "COUNT(p.id) " +
                        "FROM Product p " +
                        "LEFT JOIN p.productPrice pp " +
                        "LEFT JOIN p.optionGroups og " +
                        "LEFT JOIN og.options o " +
                        "LEFT JOIN p.reviews r " +
                        "WHERE (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(p.shortDescription) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
                        "AND (:categoryIds IS NULL OR p.id IN " +
                        "(SELECT pc.product.id FROM ProductCategory pc WHERE pc.category.id IN :categoryIds)) " +
                        "AND (:brandIds IS NULL OR p.brand.id IN :brandIds) " +
                        "AND (:sellerIds IS NULL OR p.seller.id IN :sellerIds) " +
                        "AND (:inStock IS NULL OR " +
                        "CASE WHEN p.optionGroups IS EMPTY THEN true " +
                        "ELSE EXISTS (SELECT 1 FROM p.optionGroups og2 JOIN og2.options o2 WHERE o2.stock > 0) END = :inStock) "
                        +
                        "AND (:rating IS NULL OR " +
                        "COALESCE((SELECT AVG(r2.rating) FROM p.reviews r2), 0) >= :rating) " +
                        "AND p.status = 'ACTIVE' " +
                        "GROUP BY " +
                        "CASE " +
                        "WHEN pp.basePrice < 300000 THEN 0 " +
                        "WHEN pp.basePrice < 600000 THEN 300000 " +
                        "ELSE 600000 " +
                        "END, " +
                        "CASE " +
                        "WHEN pp.basePrice < 300000 THEN 300000 " +
                        "WHEN pp.basePrice < 600000 THEN 600000 " +
                        "ELSE NULL " +
                        "END")
        List<Object[]> getPriceRangeFilters(
                        @Param("keyword") String keyword,
                        @Param("categoryIds") List<Long> categoryIds,
                        @Param("brandIds") List<Long> brandIds,
                        @Param("sellerIds") List<Long> sellerIds,
                        @Param("inStock") Boolean inStock,
                        @Param("rating") Double rating);
}