package com.relatos.ms_books_payments.repositories;

import com.relatos.ms_books_payments.domains.Order;
import com.relatos.ms_books_payments.repositories.commons.SoftRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends SoftRepository<Order> {

    @Query("SELECT o FROM Order o WHERE o.deleted = false AND o.userId = :userId")
    List<Order> findByUserId(@Param("userId") Long userId);
}
