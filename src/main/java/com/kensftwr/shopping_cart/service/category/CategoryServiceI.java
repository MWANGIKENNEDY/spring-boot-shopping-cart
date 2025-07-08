package com.kensftwr.shopping_cart.service.category;

import java.util.List;

import com.kensftwr.shopping_cart.dtos.CategoryRequest;
import com.kensftwr.shopping_cart.dtos.CategoryResponse;
import com.kensftwr.shopping_cart.models.Category;

public interface CategoryServiceI {

    Category getCategoryById(Long id);

    Category getCategoryByName(String name);

    List<Category> getAllCategories();

    CategoryResponse addCategory(CategoryRequest category);

    CategoryResponse updateCategory(CategoryRequest category, Long id);

    void deleteCategoryById(Long id);

}
