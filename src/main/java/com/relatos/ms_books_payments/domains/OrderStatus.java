package com.relatos.ms_books_payments.domains;

import com.relatos.ms_books_payments.domains.commons.SoftEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_status", uniqueConstraints = @UniqueConstraint(columnNames = "name", name = "Name"))
public class OrderStatus extends SoftEntity {

    @NotNull
    private String name;

}
