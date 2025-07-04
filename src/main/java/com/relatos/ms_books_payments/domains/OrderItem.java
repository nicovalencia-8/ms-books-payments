package com.relatos.ms_books_payments.domains;

import com.relatos.ms_books_payments.controllers.request.OrderItemRequest;
import com.relatos.ms_books_payments.domains.commons.SoftEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="order_item")
public class OrderItem extends SoftEntity {

    private Long bookId;
    private Integer quantity;
    private Double price;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderItem(OrderItemRequest orderItemRequest, Order order) {
        this.bookId = orderItemRequest.getBookId();
        this.quantity = orderItemRequest.getQuantity();
        this.price = orderItemRequest.getPrice();
        this.order = order;
    }
}
