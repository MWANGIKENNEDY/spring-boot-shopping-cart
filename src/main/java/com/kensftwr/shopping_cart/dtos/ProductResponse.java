package com.kensftwr.shopping_cart.dtos;

import java.math.BigDecimal;
import java.util.List;

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
public class ProductResponse {

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
    private CategoryResponse category;

    private List<ImageResponse> images;

}
