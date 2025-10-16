package com.huyhaf.shopapp.controllers;

import com.huyhaf.shopapp.components.LocalizationUtils;
import com.huyhaf.shopapp.dtos.CategoryDTO;
import com.huyhaf.shopapp.models.Category;
import com.huyhaf.shopapp.responses.UpdateCategoryResponse;
import com.huyhaf.shopapp.sevices.CategoryService;
import com.huyhaf.shopapp.utils.MessageKeys;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/categories")
// @Validated
@RequiredArgsConstructor
public class CategoryController {
	private final CategoryService categoryService;
	private final LocalizationUtils localizationUtils;

	@PostMapping("")
	public ResponseEntity<?> createCategories(
			@Valid @RequestBody CategoryDTO categoryDTO,
			BindingResult result) {
		if (result.hasErrors()) {
			List<String> errorMessages = result.getFieldErrors()
					.stream()
					.map(FieldError::getDefaultMessage)
					.toList();
			return ResponseEntity.badRequest().body(errorMessages);
		}
		categoryService.createCategory(categoryDTO);
		return ResponseEntity.ok("Insert successfully" + categoryDTO);
	}

	// Showing all categories
	@GetMapping("") // http://localhost:8088/api/v1/categories?page=1&limit=10
	public ResponseEntity<List<Category>> getAllCategories(
			@RequestParam("page") int page,
			@RequestParam("limit") int limit) {
		List<Category> categories = categoryService.getAllCategories();
		return ResponseEntity.ok(categories);
	}

	@PutMapping("/{id}")
	public ResponseEntity<UpdateCategoryResponse> updateCategories(
			@PathVariable Long id,
			@Valid @RequestBody CategoryDTO categoryDTO,
			HttpServletRequest request) {
		categoryService.updateCategory(id, categoryDTO);
		return ResponseEntity.ok(UpdateCategoryResponse.builder()
				.message(localizationUtils.getLocalizedMessage(MessageKeys.CATEGORY_UPDATE_SUCCESS))
				.build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCategories(@PathVariable Long id) {
		categoryService.deleteCategory(id);
		return ResponseEntity.ok("Delete category at: " + id);
	}
}
