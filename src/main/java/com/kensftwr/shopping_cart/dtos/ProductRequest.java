package com.kensftwr.shopping_cart.dtos;

import java.math.BigDecimal;

import com.kensftwr.shopping_cart.models.Category;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotEmpty
    private BigDecimal price;
    @NotEmpty
    private Integer inventory;
    @NotEmpty
    private String brand;
    @NotEmpty
    private Category category;
}

// @Builder makes it easy to create objects with a clean, chainable, and
// readable syntax, avoiding constructor overloading or telescoping
// constructors. Perfect for DTOs like ProductRequest.