package com.kensftwr.shopping_cart.service.cart;

import com.kensftwr.shopping_cart.models.Cart;

import java.math.BigDecimal;

public interface ICartService {

    Cart getCart(Long id);

    void clearCart(Long id);

    BigDecimal getTotalPrice(Long id);
}
