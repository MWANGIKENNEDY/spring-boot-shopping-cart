package com.kensftwr.shopping_cart.service.category;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.kensftwr.shopping_cart.dtos.CategoryRequest;
import com.kensftwr.shopping_cart.dtos.CategoryResponse;
import com.kensftwr.shopping_cart.exceptions.CategoryNotFoundException;
import com.kensftwr.shopping_cart.exceptions.ProductNotFoundException;
import com.kensftwr.shopping_cart.models.Category;
import com.kensftwr.shopping_cart.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryServiceI {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public CategoryResponse addCategory(CategoryRequest categoryRequest) {
        // Get existing category or create & save new one
        Category category = categoryRepository.findByName(categoryRequest.getName())
                .orElseGet(() -> categoryRepository.save(new Category(categoryRequest.getName())));

        // Map to response DTO
        return modelMapper.map(category, CategoryResponse.class);
    }

    @Override
    public CategoryResponse updateCategory(CategoryRequest categoryRequest, Long id) {
        // Check if category exists
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found!"));

        // Update the name
        category.setName(categoryRequest.getName());

        // Save updated category
        Category updatedCategory = categoryRepository.save(category);

        // Map to response DTO
        return modelMapper.map(updatedCategory, CategoryResponse.class);
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete, () -> {
            throw new ProductNotFoundException("Category not found!");
        });
    }

}
