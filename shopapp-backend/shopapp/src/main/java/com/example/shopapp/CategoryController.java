package com.example.shopapp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {
    //Showing all categories
    @GetMapping("")  // http://localhost:8088/api/v1/categories
    public ResponseEntity<String> getAllCategories() {
        return ResponseEntity.ok("hello");
    }

    @PostMapping("")
    public ResponseEntity<String> insertCategories() {
        return ResponseEntity.ok("Insert successfully");
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
