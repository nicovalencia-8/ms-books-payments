package com.relatos.ms_books_payments.domains;

import lombok.Getter;

@Getter
public enum OrderStatusEnum {
    PENDING(1),
    PAID(2);

    private final int value;

    OrderStatusEnum(int value) {
        this.value = value;
    }
}
