package com.relatos.ms_books_payments.externals.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime publishedDate;
    private Integer stock;
    private Double price;
    private Double rating;
    private AuthorResponse author;
    private String image;
    private List<CategoryResponse> category;
    private String isbn;

}
