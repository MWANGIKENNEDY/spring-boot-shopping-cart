package com.kensftwr.shopping_cart.service.order;

import com.kensftwr.shopping_cart.dtos.OrderDto;
import com.kensftwr.shopping_cart.exceptions.ProductNotFoundException;
import com.kensftwr.shopping_cart.models.*;
import com.kensftwr.shopping_cart.repository.OrderRepository;
import com.kensftwr.shopping_cart.repository.ProductRepository;
import com.kensftwr.shopping_cart.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepo;
    private final ICartService cartService;

    private final ModelMapper modelMapper;


    @Transactional
    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order,cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalPrice(calculateTotalPrice(orderItemList));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());
        return savedOrder;
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::convertToDto)
                .orElseThrow(() -> new ProductNotFoundException("Order not found!"));
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId){
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this::convertToDto).toList();
    }

    public Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return  null;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepo.save(product);
            return new OrderItem(product,order,cartItem.getQuantity(),cartItem.getUnitPrice());
        }).toList();
    }


    //calculate total price of the order::
    //Order has a list of order items::
    private BigDecimal calculateTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> item.getPrice()
                        .multiply(new  BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

//    .reduce(BigDecimal.ZERO, BigDecimal::add)
//    This sums up all the item total prices.
//    BigDecimal.ZERO is the identity (starting value for addition).
//    BigDecimal::add adds all the mapped values together.
//    BigDecimal::add | BigDecimal::multiply | BigDecimal::max | BigDecimal::min

    private OrderDto convertToDto(Order order){
        return modelMapper.map(order,OrderDto.class);
    }


}
