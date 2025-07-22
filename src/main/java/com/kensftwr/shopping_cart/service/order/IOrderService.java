package com.kensftwr.shopping_cart.service.order;

import com.kensftwr.shopping_cart.dtos.OrderDto;
import com.kensftwr.shopping_cart.models.Order;

import java.util.List;

public interface IOrderService {

    Order placeOrder(Long userId);

    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrders(Long userId);
}
