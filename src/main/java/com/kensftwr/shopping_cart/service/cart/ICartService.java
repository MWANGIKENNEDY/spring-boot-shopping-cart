package com.kensftwr.shopping_cart.service.cart;

import com.kensftwr.shopping_cart.models.Cart;
import com.kensftwr.shopping_cart.models.User;

import java.math.BigDecimal;

public interface ICartService {

    Cart getCart(Long id);

    void clearCart(Long id);

    BigDecimal getTotalPrice(Long id);

    Cart initializeNewCart(User user);

    Cart getCartByUserId(Long userId);
}
