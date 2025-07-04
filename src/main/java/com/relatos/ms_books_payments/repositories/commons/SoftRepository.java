package com.relatos.ms_books_payments.repositories.commons;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;

@NoRepositoryBean
public interface SoftRepository<T> extends JpaRepository<T, Long> {

    @Query("SELECT e FROM #{#entityName} e WHERE e.deleted = false AND e.id = :id")
    T findByIdC(@Param("id") Long id);

    @Query("SELECT e FROM #{#entityName} e WHERE e.deleted = false")
    List<T> findAllC();

    @Query("SELECT e FROM #{#entityName} e WHERE e.deleted = false")
    Page<T> findAllC(Pageable pageable);

    @Modifying
    @Query("UPDATE #{#entityName} e SET e.deleted = true WHERE e.id = :id")
    void softDelete(@Param("id") Long id);

}

