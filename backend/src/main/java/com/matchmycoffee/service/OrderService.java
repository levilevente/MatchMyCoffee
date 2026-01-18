package com.matchmycoffee.service;

import com.matchmycoffee.model.entity.Order;
import com.matchmycoffee.model.entity.OrderItem;
import com.matchmycoffee.service.exception.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Order getOrderById(Long id) throws OrderNotFoundException;

    Page<Order> getOrdersByEmail(Pageable pageable, String email);

    Order createOrder(Order order) throws IllegalOrderArgumentException, InsufficientStockException;

    Order addItemToOrder(OrderItem orderItem)
            throws InsufficientStockException, IllegalOrderArgumentException, ProductNotAvailableException,
            OrderNotFoundException;

    void removeItemFromOrder(OrderItem orderItem) throws OrderNotFoundException;

    Order updateOrder(Long id, Order order) throws InvalidOrderStateException, OrderNotFoundException;

    void cancelOrder(Long id) throws OrderNotFoundException, IllegalOrderArgumentException;
}
