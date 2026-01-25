package com.matchmycoffee.service;

import com.matchmycoffee.dto.request.order.OrderItemRequest;
import com.matchmycoffee.model.entity.Order;
import com.matchmycoffee.model.entity.OrderItem;
import com.matchmycoffee.service.exception.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    Order getOrderById(Long id) throws OrderNotFoundException;

    Page<Order> getOrdersByEmail(Pageable pageable, String email);

    Order findByIdWithItems(Long id) throws OrderNotFoundException;

    OrderItem findOrderItemById(Long id) throws OrderNotFoundException;

    Order createOrder(Order order, List<OrderItemRequest> items)
            throws IllegalOrderArgumentException, InsufficientStockException, ProductNotAvailableException;

    Order addItemToOrder(Long orderId, OrderItemRequest itemRequest)
            throws InsufficientStockException, IllegalOrderArgumentException, ProductNotAvailableException,
            OrderNotFoundException;

    void removeItemFromOrder(OrderItem orderItem) throws OrderNotFoundException, ProductNotAvailableException;

    Order updateOrder(Long id, Order order) throws InvalidOrderStateException, OrderNotFoundException;

    void cancelOrder(Long id)
            throws OrderNotFoundException, IllegalOrderArgumentException, ProductNotAvailableException;
}
