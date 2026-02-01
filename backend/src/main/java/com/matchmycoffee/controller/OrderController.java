package com.matchmycoffee.controller;

import com.matchmycoffee.dto.request.order.CreateOrderRequest;
import com.matchmycoffee.dto.request.order.OrderItemRequest;
import com.matchmycoffee.dto.response.OrderResponse;
import com.matchmycoffee.mapper.OrderMapper;
import com.matchmycoffee.model.entity.Order;
import com.matchmycoffee.model.entity.OrderItem;
import com.matchmycoffee.service.OrderService;
import com.matchmycoffee.service.exception.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid CreateOrderRequest createOrderRequest)
            throws IllegalOrderArgumentException, InsufficientStockException, ProductNotAvailableException {
        log.info("POST /orders");

        Order order = orderMapper.toEntity(createOrderRequest);
        Order createdOrder = orderService.createOrder(order, createOrderRequest.getItems());

        URI createdOrderUri = URI.create(String.format("/orders/%s", createdOrder.getId()));
        return ResponseEntity.created(createdOrderUri)
                .body(orderMapper.toOrderResponseWithItems(createdOrder, createdOrder.getOrderItems()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) throws OrderNotFoundException {
        log.info("GET /orders/{}", id);

        Order order = orderService.findByIdWithItems(id);

        List<OrderItem> items = order.getOrderItems();
        return ResponseEntity.ok(orderMapper.toOrderResponseWithItems(order, items));
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getOrdersByEmail(@RequestParam String email,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size,
                                                                @RequestParam(defaultValue = "id") String sortBy) {
        log.info("GET /orders?email={}", email);

        if (size <= 0 || page < 0) {
            log.warn("Invalid pagination parameters: page={}, size={}", page, size);
            return ResponseEntity.badRequest().build();
        }

        if (!"id".equals(sortBy) && !"createdAt".equals(sortBy) && !"status".equals(sortBy)) {
            log.warn("Invalid sortBy parameter: {}", sortBy);
            return ResponseEntity.badRequest().build();
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<Order> orderPage = orderService.getOrdersByEmail(pageable, email);

        return ResponseEntity.ok(orderPage.map(orderMapper::toOrderResponse));
    }

    @PostMapping("/{orderId}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrderResponse> addItemToOrder(@PathVariable Long orderId,
                                                        @RequestBody @Valid OrderItemRequest orderItemRequest)
            throws InsufficientStockException, IllegalOrderArgumentException, ProductNotAvailableException,
            OrderNotFoundException {
        log.info("POST /orders/{}/items", orderId);

        Order updatedOrder = orderService.addItemToOrder(orderId, orderItemRequest);

        Order orderWithItems = orderService.findByIdWithItems(updatedOrder.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderMapper.toOrderResponseWithItems(orderWithItems, orderWithItems.getOrderItems()));
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItemFromOrder(@PathVariable Long orderId, @PathVariable Long itemId)
            throws OrderNotFoundException, ProductNotAvailableException {
        log.info("DELETE /orders/{}/items/{}", orderId, itemId);

        OrderItem orderItem = orderService.findOrderItemById(itemId);

        if (!orderItem.getOrder().getId().equals(orderId)) {
            throw new OrderNotFoundException("Order item does not belong to this order!");
        }

        orderService.removeItemFromOrder(orderItem);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable Long id, @RequestBody Order orderDetails)
            throws InvalidOrderStateException, OrderNotFoundException {
        log.info("PATCH /orders/{}", id);

        Order updatedOrder = orderService.updateOrder(id, orderDetails);
        return ResponseEntity.ok(orderMapper.toOrderResponse(updatedOrder));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelOrder(@PathVariable Long id)
            throws OrderNotFoundException, IllegalOrderArgumentException, ProductNotAvailableException {
        log.info("DELETE /orders/{}", id);
        orderService.cancelOrder(id);
    }
}
