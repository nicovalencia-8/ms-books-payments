package com.relatos.ms_books_payments.controllers.response;

import com.relatos.ms_books_payments.domains.Order;
import com.relatos.ms_books_payments.domains.OrderItem;
import com.relatos.ms_books_payments.domains.OrderStatus;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderResponse {
    private Long id;
    private Long userId;
    private OrderStatus status;
    private List<OrderItemResponse> items;

    public OrderResponse(Order order, List<OrderItem> orderItem) {
        this.id = order.getId();
        this.userId = order.getUserId();
        this.status = order.getStatus();
        this.items = orderItem.stream().map(OrderItemResponse::new).collect(Collectors.toList());
    }
}
