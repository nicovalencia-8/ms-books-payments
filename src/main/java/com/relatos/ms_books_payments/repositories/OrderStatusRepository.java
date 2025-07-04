package com.relatos.ms_books_payments.repositories;

import com.relatos.ms_books_payments.domains.OrderStatus;
import com.relatos.ms_books_payments.repositories.commons.SoftRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusRepository extends SoftRepository<OrderStatus> {

    @Query("SELECT os FROM OrderStatus os WHERE os.deleted = false AND os.name = :name")
    OrderStatus findByName(@Param("name")  String name);


}
