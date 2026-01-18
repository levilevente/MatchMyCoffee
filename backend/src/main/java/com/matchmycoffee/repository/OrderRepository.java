package com.matchmycoffee.repository;

import com.matchmycoffee.model.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Object[]> findAllByCustomerEmail(String customerEmail, Pageable pageable);

    Order getOrderById(Long id);
}
