package com.kensftwr.shopping_cart.repository;

import com.kensftwr.shopping_cart.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByCartId(Long id);
}
