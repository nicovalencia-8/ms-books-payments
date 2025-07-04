package com.relatos.ms_books_payments.controllers.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    @NotNull
    private Long userId;
    @NotNull
    private List<OrderItemRequest> items;
}
