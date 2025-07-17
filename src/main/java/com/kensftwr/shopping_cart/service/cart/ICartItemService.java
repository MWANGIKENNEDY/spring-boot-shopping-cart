package com.kensftwr.shopping_cart.service.cart;

public interface ICartItemService {
    //add item to cart
    //remove item from cart
    //update item quantity
    void addItemToCart(Long cartId, Long productId,int quantity);
    void removeItemFromCart(Long cartId, Long productId);
    void updateCartItemQuantity(Long cartId, Long productId, int quantity);
}
