package com.huyhaf.shopapp.repositories;

import com.huyhaf.shopapp.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
