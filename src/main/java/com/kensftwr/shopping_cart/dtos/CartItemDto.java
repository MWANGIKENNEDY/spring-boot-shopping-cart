package com.kensftwr.shopping_cart.dtos;

import com.kensftwr.shopping_cart.models.Product;

import java.math.BigDecimal;

public class CartItemDto {

    private Long itemId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private Product product;
}
