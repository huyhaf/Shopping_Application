package com.huyhaf.shopapp.repositories;

import com.huyhaf.shopapp.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsByName(String name);

    Page<Product> findAll(Pageable pageable);

    @Query("SELECT p FROM Product p " +
            "WHERE (:category is null or :categoryId = 0 OR p.category.id = :categoryId) " +
            "AND (:keyword is null or :keyword = '' OR p.name LIKE %:keyword%' or p.description LIKE %:keyword%)")
    Page<Product> searchProducts(@Param("categoryId") Long categoryId,
                                 @Param("keyword") String keyword,
                                 Pageable pageable);
}
