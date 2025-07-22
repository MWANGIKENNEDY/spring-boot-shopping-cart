package com.kensftwr.shopping_cart.controller;

import com.kensftwr.shopping_cart.dtos.ApiResponse;
import com.kensftwr.shopping_cart.exceptions.ProductNotFoundException;
import com.kensftwr.shopping_cart.service.cart.ICartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cartItems")
public class CartItemController {


    private final ICartItemService cartItemService;

    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam Long cartId,
                                                     @RequestParam Long productId,
                                                     @RequestParam Integer quantity
    ) {
        try {
            cartItemService.addItemToCart(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Item added!",null));
        }catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Item not found!",null));
        }
    }

    @DeleteMapping("/cart/{cartId}/item/{productId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId,
                                                          @PathVariable Long productId
    ) {
        try {
            cartItemService.removeItemFromCart(cartId, productId);
            return ResponseEntity.ok(new ApiResponse("Item removed from cart!",null));
        }catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Item not found!",null));

        }
    }


    @PutMapping("/cart/{cartId}/item/{productId}/update")
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId,
                                                          @PathVariable Long productId,
                                                          @RequestParam Integer quantity
    ) {
        try {
            cartItemService.updateCartItemQuantity(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Item quantity updated!",null));
        }catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Item not found!",null));

        }
    }





}

