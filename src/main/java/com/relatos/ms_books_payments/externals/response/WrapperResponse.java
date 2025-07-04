package com.relatos.ms_books_payments.externals.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WrapperResponse {

    private int statusCode;
    private String status;
    private String message;
    private BookResponse body;

}
