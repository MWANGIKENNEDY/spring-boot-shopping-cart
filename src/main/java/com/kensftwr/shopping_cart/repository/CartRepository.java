package com.kensftwr.shopping_cart.repository;


import com.kensftwr.shopping_cart.models.Cart;
import com.kensftwr.shopping_cart.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByUserId(Long userId);
}
