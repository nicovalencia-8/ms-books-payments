package com.relatos.ms_books_payments.domains;

import com.relatos.ms_books_payments.domains.commons.SoftEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books_order")
public class Order extends SoftEntity {

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private OrderStatus status;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;


    public Order(Long userId, OrderStatus status) {
        this.userId = userId;
        this.status = status;
        this.paidAt = LocalDateTime.now();
    }
}
