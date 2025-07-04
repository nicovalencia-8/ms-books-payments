package com.relatos.ms_books_payments.repositories;

import com.relatos.ms_books_payments.domains.Order;
import com.relatos.ms_books_payments.domains.OrderItem;
import com.relatos.ms_books_payments.repositories.commons.SoftRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends SoftRepository<OrderItem> {

    @Query("SELECT oi FROM OrderItem oi WHERE oi.deleted = false AND oi.order = :order")
    List<OrderItem> findOrderItemByOrder(@Param("order") Order order);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.deleted = false AND oi.order = :order")
    List<OrderItem> findOrderItemByOrder(@Param("order") List<Order> order);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.deleted = false AND oi.order = :order AND oi.bookId = :bookId")
    OrderItem findOrderItemByOrderAndBookId(@Param("order") Order order, @Param("bookId") Long bookId);

}
