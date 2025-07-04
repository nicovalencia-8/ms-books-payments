package com.relatos.ms_books_payments.controllers.request;

import com.relatos.ms_books_payments.domains.OrderItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {

    @NotNull
    private Long bookId;

    @Min(value = 1)
    private Integer quantity;
    private Double price;

    public OrderItemRequest(OrderItem orderItem) {
        this.bookId = orderItem.getBookId();
        this.quantity = orderItem.getQuantity();
        this.price = orderItem.getPrice();
    }

}
