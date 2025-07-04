package com.relatos.ms_books_payments.controllers.response;

import com.relatos.ms_books_payments.domains.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {
    private Long bookId;
    private Integer quantity;
    private Double price;

    public OrderItemResponse (OrderItem item) {
        this.bookId = item.getBookId();
        this.quantity = item.getQuantity();
        this.price = item.getPrice();
    }
}
