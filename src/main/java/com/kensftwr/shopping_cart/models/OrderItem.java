package com.kensftwr.shopping_cart.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private Integer quantity;

    private BigDecimal price;

    public OrderItem(Product product, Order order, Integer quantity, BigDecimal price) {
        this.product = product;
        this.order = order;
        this.quantity = quantity;
        this.price = price;
    }
}
