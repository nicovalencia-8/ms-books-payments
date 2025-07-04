package com.relatos.ms_books_payments.controllers.response.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeneralResponse<T> {

    private int statusCode;
    private String status;
    private String message;
    private T body;

}
