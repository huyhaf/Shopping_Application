package com.example.shopapp.controllers;
import com.example.shopapp.dto.CategoryDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
//@Validated
public class CategoryController {
    //Showing all categories
    @GetMapping("")  // http://localhost:8088/api/v1/categories?page=1&limit=10
    public ResponseEntity<String> getAllCategories(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        return ResponseEntity.ok("hello");
    }

    @PostMapping("")
    public ResponseEntity<?> insertCategories(
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult result) {
        if (result.hasErrors()){
            List<String> errorMessages= result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        return ResponseEntity.ok("Insert successfully" + categoryDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategories (@PathVariable Long id){
        return ResponseEntity.ok("update successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategories (@PathVariable Long id){
        return ResponseEntity.ok("Delete category at: " +id);
    }
}
