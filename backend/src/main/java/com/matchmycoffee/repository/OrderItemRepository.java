package com.matchmycoffee.repository;

import com.matchmycoffee.model.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> getOrderItemById(Long id);

    List<OrderItem> findAllByOrder_Id(Long orderId);
}
