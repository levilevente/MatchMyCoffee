package com.matchmycoffee.service.impl;

import com.matchmycoffee.dto.request.order.OrderItemRequest;
import com.matchmycoffee.model.entity.Order;
import com.matchmycoffee.model.entity.OrderItem;
import com.matchmycoffee.model.entity.Product;
import com.matchmycoffee.model.enums.OrderStatus;
import com.matchmycoffee.repository.OrderItemRepository;
import com.matchmycoffee.repository.OrderRepository;
import com.matchmycoffee.service.OrderService;
import com.matchmycoffee.service.ProductService;
import com.matchmycoffee.service.exception.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Order getOrderById(Long id) throws OrderNotFoundException {
        log.info("Getting order by id: {}", id);
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found!"));
    }

    @Override
    public Page<Order> getOrdersByEmail(Pageable pageable, String email) {
        log.info("Fetching all orders by email with pagination: email {}, page number {}, page size {}", email,
                pageable.getPageNumber(), pageable.getPageSize());

        return orderRepository.findAllByCustomerEmail(email, pageable);
    }

    @Override
    public Order findByIdWithItems(Long id) throws OrderNotFoundException {
        return orderRepository.findByIdWithItems(id).orElseThrow(() -> new OrderNotFoundException("Order not found!"));
    }

    @Override
    public OrderItem findOrderItemById(Long id) throws OrderNotFoundException {
        return orderItemRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order item not found!"));
    }

    @Override
    @Transactional
    public Order createOrder(Order order, List<OrderItemRequest> items)
            throws IllegalOrderArgumentException, InsufficientStockException, ProductNotAvailableException {
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

        String orderReference = String.format("order-%s", System.currentTimeMillis());
        order.setOrderReference(orderReference);

        order.setStatus(OrderStatus.PENDING);

        log.info("Creating new order: {}", order.getOrderReference());
        Order savedOrder = orderRepository.save(order);

        // Process items if provided
        if (items != null && !items.isEmpty()) {
            for (OrderItemRequest itemRequest : items) {
                try {
                    savedOrder = addItemToOrder(savedOrder.getId(), itemRequest);
                } catch (OrderNotFoundException e) {
                    log.error("Error while adding item to order {}", savedOrder.getId(), e);
                    throw new IllegalStateException("Failed to add item to order!", e);
                }
            }
        }

        try {
            entityManager.flush();
            entityManager.clear();
            return this.findByIdWithItems(savedOrder.getId());
        } catch (OrderNotFoundException e) {
            log.error("Error while retrieving order with id {}", savedOrder.getId(), e);
            throw new IllegalStateException("Failed to retrieve order!", e);
        }
    }

    @Override
    @Transactional
    public Order addItemToOrder(Long orderId, OrderItemRequest itemRequest)
            throws InsufficientStockException, IllegalOrderArgumentException, ProductNotAvailableException,
            OrderNotFoundException {
        if (itemRequest.getQuantity() <= 0) {
            log.error("Invalid item quantity: {}", itemRequest.getQuantity());
            throw new IllegalOrderArgumentException("Quantity must be greater than zero!");
        }

        Product product = productService.getProductById(itemRequest.getProductId());

        if (product.getStock() < itemRequest.getQuantity()) {
            log.error("Insufficient stock for product: {}", product.getName());
            throw new InsufficientStockException("Insufficient stock!");
        }

        Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found!"));
        if (savedOrder.getStatus() != OrderStatus.PENDING) {
            log.error("Order status is not pending: {}", savedOrder.getStatus());
            throw new IllegalOrderArgumentException("Order status is not pending!");
        }

        if (product.getIsActive() == false) {
            log.error("Product is not active: {}", product.getName());
            throw new IllegalOrderArgumentException("Product is not active!");
        }

        // Create OrderItem with price from product
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(savedOrder);
        orderItem.setProduct(product);
        orderItem.setQuantity(itemRequest.getQuantity());
        orderItem.setPriceAtPurchase(product.getPrice());

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
    public void removeItemFromOrder(OrderItem orderItem) throws OrderNotFoundException, ProductNotAvailableException {
        if (orderItem.getOrder().getStatus() != OrderStatus.PENDING) {
            log.error("Can't remove item from order, because status is not pending: {}",
                    orderItem.getOrder().getStatus());
            throw new InvalidOrderStateException("Order status is not pending!");
        }

        OrderItem existingOrderItem = orderItemRepository.findById(orderItem.getId())
                .orElseThrow(() -> new OrderNotFoundException("Order item not found!"));

        Order order = existingOrderItem.getOrder();
        Product product = existingOrderItem.getProduct();

        // Restore stock and decrease total amount
        product.setStock(product.getStock() + existingOrderItem.getQuantity());
        productService.updateProduct(product.getId(), product);

        double itemTotal = existingOrderItem.getPriceAtPurchase() * existingOrderItem.getQuantity();
        order.setTotalAmount(order.getTotalAmount() - itemTotal);
        orderRepository.save(order);

        log.info("Removing order item {} from order: {}", existingOrderItem.getId(), order.getOrderReference());
        orderItemRepository.delete(existingOrderItem);
    }

    @Override
    @Transactional
    public Order updateOrder(Long id, Order order) throws InvalidOrderStateException {
        Order existingOrder =
                orderRepository.findById(id).orElseThrow(() -> new InvalidOrderStateException("Order not found!"));

        if (existingOrder.getStatus() != OrderStatus.PENDING) {
            log.error("Can't update order, because status is not pending: {}", existingOrder.getStatus());
            throw new InvalidOrderStateException("Order status is not pending!");
        }

        // Merge patched values with existing order - only update non-null fields
        if (order.getCustomerEmail() != null) {
            existingOrder.setCustomerEmail(order.getCustomerEmail());
        }
        if (order.getCustomerFirstName() != null) {
            existingOrder.setCustomerFirstName(order.getCustomerFirstName());
        }
        if (order.getCustomerLastName() != null) {
            existingOrder.setCustomerLastName(order.getCustomerLastName());
        }
        if (order.getShippingAddressLine1() != null) {
            existingOrder.setShippingAddressLine1(order.getShippingAddressLine1());
        }
        if (order.getShippingAddressLine2() != null) {
            existingOrder.setShippingAddressLine2(order.getShippingAddressLine2());
        }
        if (order.getShippingCity() != null) {
            existingOrder.setShippingCity(order.getShippingCity());
        }
        if (order.getShippingState() != null) {
            existingOrder.setShippingState(order.getShippingState());
        }
        if (order.getShippingZip() != null) {
            existingOrder.setShippingZip(order.getShippingZip());
        }
        if (order.getShippingCountry() != null) {
            existingOrder.setShippingCountry(order.getShippingCountry());
        }
        if (order.getStatus() != null) {
            existingOrder.setStatus(order.getStatus());
        }

        log.info("Updating order: {}", existingOrder.getOrderReference());
        return orderRepository.save(existingOrder);
    }

    @Override
    @Transactional
    public void cancelOrder(Long id) throws OrderNotFoundException, IllegalOrderArgumentException, ProductNotAvailableException {
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
