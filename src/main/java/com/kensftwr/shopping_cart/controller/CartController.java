package com.kensftwr.shopping_cart.controller;

import com.kensftwr.shopping_cart.dtos.ApiResponse;
import com.kensftwr.shopping_cart.exceptions.ProductNotFoundException;
import com.kensftwr.shopping_cart.models.Cart;
import com.kensftwr.shopping_cart.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {

    private final ICartService cartService;

    @GetMapping("/{cartId}/cart")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long cartId) {
        try {
            Cart cart = cartService.getCart(cartId);
            return ResponseEntity.ok(new ApiResponse("success",cart));
        }catch(ProductNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("not found",null));
        }
    }

    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.ok(new ApiResponse("success",null));
    }

    @GetMapping("/{cartId}/cart/totalPrice")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartId) {
        BigDecimal totalPrice = cartService.getTotalPrice(cartId);
        return ResponseEntity.ok(new ApiResponse("success",totalPrice));
    }


}
