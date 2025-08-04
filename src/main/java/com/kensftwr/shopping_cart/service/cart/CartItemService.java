package com.kensftwr.shopping_cart.service.cart;

import com.kensftwr.shopping_cart.exceptions.ProductNotFoundException;
import com.kensftwr.shopping_cart.models.Cart;
import com.kensftwr.shopping_cart.models.CartItem;
import com.kensftwr.shopping_cart.models.Product;
import com.kensftwr.shopping_cart.repository.CartItemRepository;
import com.kensftwr.shopping_cart.repository.CartRepository;
import com.kensftwr.shopping_cart.repository.ProductRepository;
import com.kensftwr.shopping_cart.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {


    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final ICartService cartService;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        //get cart
        //get the product
        //check if product is in the cart
        //if yes, increase quantity with the requested quantity
        //if no, add item to the cart
        Cart cart = cartService.getCart(cartId);
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found!"));
        CartItem cartItem = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElse(new CartItem());;
        if(cartItem.getId() == null){
            cartItem.setQuantity(quantity);
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setUnitPrice(product.getPrice());

        }else{
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem itemToRemove  = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElseThrow(() -> new ProductNotFoundException("Cart not found!"));
        cart.removeItem(itemToRemove);
        cartRepository.save(cart);
    }

    @Override
    public void updateCartItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                });
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }
}
