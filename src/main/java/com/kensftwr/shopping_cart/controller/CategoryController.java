package com.kensftwr.shopping_cart.controller;

import java.util.List;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kensftwr.shopping_cart.dtos.ApiResponse;
import com.kensftwr.shopping_cart.dtos.CategoryRequest;
import com.kensftwr.shopping_cart.dtos.CategoryResponse;
import com.kensftwr.shopping_cart.exceptions.AlreadyExistsException;
import com.kensftwr.shopping_cart.models.Category;
import com.kensftwr.shopping_cart.service.category.CategoryServiceI;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {

    CategoryServiceI categoryServiceI;

    @GetMapping("/categories/all")
    public ResponseEntity<ApiResponse> getAllCategories() {
        try {
            List<Category> categories = categoryServiceI.getAllCategories();
            return ResponseEntity.ok(new ApiResponse("Found", categories));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to fetch categories!", INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/categories/add")
    public ResponseEntity<ApiResponse> createCategory(@RequestBody CategoryRequest name) {
        try {
            CategoryResponse newCategory = categoryServiceI.addCategory(name);
            return ResponseEntity.ok(new ApiResponse("Found", newCategory));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse("Failed to create category!", e.getMessage()));
        }
    }

    @GetMapping("/categories/category/{categoryId}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long categoryId) {
        try {
            Category category = categoryServiceI.getCategoryById(categoryId);
            return ResponseEntity.ok(new ApiResponse("Found", category));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("Failed to fetch category!", INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/categories/category/cat-by-name/{categoryName}")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String categoryName) {
        try {
            Category category = categoryServiceI.getCategoryByName(categoryName);
            return ResponseEntity.ok(new ApiResponse("Found", category));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("Failed to fetch category!", INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/categories/delete/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable Long categoryId) {
        try {
            categoryServiceI.deleteCategoryById(categoryId);
            return ResponseEntity.ok(new ApiResponse("Found", null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("Failed to delete category!", INTERNAL_SERVER_ERROR));
        }
    }

    @PutMapping("/categories/update/{categoryId}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long categoryId,
            @RequestBody CategoryRequest name) {
        try {
            CategoryResponse updatedCategory = categoryServiceI.updateCategory(name, categoryId);
            return ResponseEntity.ok(new ApiResponse("Updated!", updatedCategory));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse("Failed to update category!", e.getMessage()));
        }
    }

}
