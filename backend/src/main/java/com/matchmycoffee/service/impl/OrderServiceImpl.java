package com.matchmycoffee.service.impl;

import com.matchmycoffee.model.entity.Order;
import com.matchmycoffee.model.entity.OrderItem;
import com.matchmycoffee.model.entity.Product;
import com.matchmycoffee.model.enums.OrderStatus;
import com.matchmycoffee.repository.OrderItemRepository;
import com.matchmycoffee.repository.OrderRepository;
import com.matchmycoffee.service.OrderService;
import com.matchmycoffee.service.ProductService;
import com.matchmycoffee.service.exception.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductService productService;

    @Override
    public Order getOrderById(Long id) throws OrderNotFoundException {
        log.info("Getting order by id: {}", id);
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found!"));
    }

    @Override
    public Page<Order> getOrdersByEmail(Pageable pageable, String email) {
        log.info("Fetching all orders by email with pagination: email {}, page number {}, page size {}", email,
                pageable.getPageNumber(), pageable.getPageSize());

        Page<Object[]> orderPage = orderRepository.findAllByCustomerEmail(email, pageable);
        return orderPage.map(objects -> (Order) objects[0]);
    }

    @Override
    @Transactional
    public Order createOrder(Order order) throws IllegalOrderArgumentException, InsufficientStockException {
        Pattern emailPattern = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        if (order.getCustomerEmail() == null || !emailPattern.matcher(order.getCustomerEmail()).matches()) {
            log.error("Invalid email: {}", order.getCustomerEmail());
            throw new IllegalOrderArgumentException("Invalid email!");
        }

        Pattern zipCodePattern = Pattern.compile("^[0-9]{5}(?:-[0-9]{4})?$");
        if (order.getShippingZip() == null || !zipCodePattern.matcher(order.getShippingZip()).matches()) {
            log.error("Invalid zip code: {}", order.getShippingZip());
            throw new IllegalOrderArgumentException("Invalid zip code!");
        }

        String orderReference = String.format("%s-%s", order.getUuid(), System.currentTimeMillis());
        order.setOrderReference(orderReference);

        order.setStatus(OrderStatus.PENDING);

        log.info("Creating new order: {}", order.getOrderReference());
        Order savedOrder = orderRepository.save(order);

        try {
            return this.getOrderById(savedOrder.getId());
        } catch (OrderNotFoundException e) {
            log.error("Error while retrieving order with id {}", savedOrder.getId(), e);
            throw new IllegalStateException("Failed to retrieve order!", e);
        }
    }

    @Override
    @Transactional
    public Order addItemToOrder(OrderItem orderItem)
            throws InsufficientStockException, IllegalOrderArgumentException, ProductNotAvailableException,
            OrderNotFoundException {
        if (orderItem.getQuantity() <= 0) {
            log.error("Invalid item quantity: {}", orderItem.getQuantity());
            throw new IllegalOrderArgumentException("Quantity must be greater than zero!");
        }

        if (orderItem.getPriceAtPurchase() <= 0) {
            log.error("Invalid item price: {}", orderItem.getPriceAtPurchase());
            throw new IllegalOrderArgumentException("Price must be greater than zero!");
        }

        if (orderItem.getProduct().getStock() < orderItem.getQuantity()) {
            log.error("Insufficient stock for product: {}", orderItem.getProduct().getName());
            throw new InsufficientStockException("Insufficient stock!");
        }

        Order savedOrder = orderRepository.findById(orderItem.getOrder().getId())
                .orElseThrow(() -> new OrderNotFoundException("Order not found!"));
        if (savedOrder.getStatus() != OrderStatus.PENDING) {
            log.error("Order status is not pending: {}", savedOrder.getStatus());
            throw new IllegalOrderArgumentException("Order status is not pending!");
        }

        Product product = orderItem.getProduct();
        if (product.getIsActive() == false) {
            log.error("Product is not active: {}", product.getName());
            throw new IllegalOrderArgumentException("Product is not active!");
        }

        log.info("Removing {} items from stock for product: {}", orderItem.getQuantity(), product.getName());
        product.setStock(product.getStock() - orderItem.getQuantity());
        productService.updateProduct(product.getId(), product);

        log.info("Adding order item to order: {}", savedOrder.getOrderReference());
        OrderItem savedOrderItem = orderItemRepository.save(orderItem);

        savedOrder.setTotalAmount(
                savedOrder.getTotalAmount() + (savedOrderItem.getPriceAtPurchase() * savedOrderItem.getQuantity()));
        savedOrder = orderRepository.save(savedOrder);

        try {
            orderItemRepository.getOrderItemById((savedOrderItem.getId()));
        } catch (EntityNotFoundException e) {
            log.error("Error while retrieving order item with id {}", savedOrderItem.getId());
            throw new InvalidOrderStateException("Failed to retrieve order item!", e);
        }
        return savedOrder;
    }

    @Override
    @Transactional
    public void removeItemFromOrder(OrderItem orderItem) throws OrderNotFoundException {
        if (orderItem.getOrder().getStatus() != OrderStatus.PENDING) {
            log.error("Can't remove item from order, because status is not pending: {}",
                    orderItem.getOrder().getStatus());
            throw new InvalidOrderStateException("Order status is not pending!");
        }

        OrderItem existingOrderItem = orderItemRepository.findById(orderItem.getId())
                .orElseThrow(() -> new OrderNotFoundException("Order item not found!"));

        Product product = existingOrderItem.getProduct();
        if (existingOrderItem.getQuantity() > orderItem.getQuantity()) {
            product.setStock(product.getStock() + (existingOrderItem.getQuantity() - orderItem.getQuantity()));
        } else {
            product.setStock(product.getStock() + existingOrderItem.getQuantity());
            orderItemRepository.delete(existingOrderItem);
        }
    }

    @Override
    @Transactional
    public Order updateOrder(Long id, Order order) throws InvalidOrderStateException {
        Order existingOrder =
                orderRepository.findById(id).orElseThrow(() -> new InvalidOrderStateException("Order not found!"));

        if (existingOrder.getStatus() != OrderStatus.PENDING) {
            log.error("Can't update order, because status is not pending: {}", order.getStatus());
            throw new InvalidOrderStateException("Order status is not pending!");
        }

        log.info("Updating order: {}", order.getOrderReference());
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void cancelOrder(Long id) throws OrderNotFoundException, IllegalOrderArgumentException {
        Order order = getOrderById(id);

        if (order.getStatus() != OrderStatus.PENDING) {
            log.error("Can't cancel order, because status is not pending: {}", order.getStatus());
            throw new IllegalOrderArgumentException("Order status is not pending!");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        for (OrderItem orderItem : orderItemRepository.findAllByOrder_Id(id)) {
            removeItemFromOrder(orderItem);
        }
    }
}
