package com.huyhaf.shopapp.sevices;

import com.huyhaf.shopapp.dtos.CategoryDTO;
import com.huyhaf.shopapp.models.Category;
import com.huyhaf.shopapp.repositories.CategoryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    private final CategoryRepository categoryRepository;
    @Override
    @CacheEvict(value = "categories", allEntries = true) // Xóa toàn bộ cache liên quan đến categories khi tạo mới
    @Transactional
    public Category createCategory(CategoryDTO categoryDTO) {
        Category category = Category.builder().name(categoryDTO.getName()).build();
        return categoryRepository.save(category);
    }

    @Override
    @Cacheable(value = "categories", key = "#id") // Lưu cache cho từng category theo id
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    @Cacheable(value = "categories") // Lưu cache cho danh sách tất cả categories
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    @CachePut(value = "categories", key = "#categoryId")
    public Category updateCategory(long categoryId, CategoryDTO category) {
        Category existingCategory = getCategoryById(categoryId);
        existingCategory.setName(category.getName());
        categoryRepository.save(existingCategory);
        return existingCategory;
    }

    @Override
    @Transactional
    @CacheEvict(value = "categories", key = "#categoryId")
    public void deleteCategory(long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
