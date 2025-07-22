package com.kensftwr.shopping_cart.controller;

import com.kensftwr.shopping_cart.dtos.ApiResponse;
import com.kensftwr.shopping_cart.dtos.OrderDto;
import com.kensftwr.shopping_cart.models.Order;
import com.kensftwr.shopping_cart.service.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final IOrderService iOrderService;

    @PostMapping("/order")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId){
        try{
            Order order = iOrderService.placeOrder(userId);
            return ResponseEntity.ok(new ApiResponse("Order success!",order));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error placing order!",e.getMessage()));
        }
    }

    @GetMapping("/{orderId}/order")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId){
        try{
            OrderDto order = iOrderService.getOrder(orderId);
            return ResponseEntity.ok(new ApiResponse("Order fetched!",order));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error finding order!",e.getMessage()));
        }
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId){
        try{
            List<OrderDto> order = iOrderService.getUserOrders(userId);
            return ResponseEntity.ok(new ApiResponse("Order fetched!",order));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error finding order!",e.getMessage()));
        }
    }

}
