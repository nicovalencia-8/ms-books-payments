package com.relatos.ms_books_payments.externals;

import com.relatos.ms_books_payments.externals.request.StockRequest;
import com.relatos.ms_books_payments.externals.response.BookResponse;
import com.relatos.ms_books_payments.externals.response.WrapperResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CatalogueService {

    private final RestTemplate restTemplate;
    private final String bookEndpoint;

    @Autowired
    public CatalogueService(RestTemplate restTemplate,
                            @Value("${ms-catalogue.url}") String bookEndpoint) {
        this.restTemplate = restTemplate;
        this.bookEndpoint = bookEndpoint;
    }

    public WrapperResponse getBook(Long bookId) {
        ResponseEntity<WrapperResponse> responseEntity =
                restTemplate.getForEntity(
                        String.format(bookEndpoint, bookId),
                        WrapperResponse.class
                );
        return responseEntity.getBody();

    }

    public void updateStock(Long bookId, StockRequest stockRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<StockRequest> requestEntity = new HttpEntity<>(stockRequest, headers);
        restTemplate.exchange(
                String.format(bookEndpoint, bookId),
                HttpMethod.PATCH,
                requestEntity,
                WrapperResponse.class
        );
    }

}
