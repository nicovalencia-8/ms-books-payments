package com.relatos.ms_books_payments.externals.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorResponse {

    private Long id;
    private String firstName;
    private String lastName;

}
