package com.matchmycoffee.mapper;

import com.matchmycoffee.dto.request.order.CreateOrderRequest;
import com.matchmycoffee.dto.response.OrderItemResponse;
import com.matchmycoffee.dto.response.OrderResponse;
import com.matchmycoffee.model.entity.Order;
import com.matchmycoffee.model.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    // Request to Entity mappings
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "orderReference", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "totalAmount", constant = "0.0")
    @Mapping(target = "currency", constant = "RON")
    @Mapping(target = "orderItems", ignore = true)
    Order toEntity(CreateOrderRequest request);


    // Entity to Response mappings
    @Mapping(target = "items", ignore = true)
    OrderResponse toOrderResponse(Order order);

    default OrderResponse toOrderResponseWithItems(Order order, List<OrderItem> items) {
        OrderResponse response = toOrderResponse(order);
        response.setItems(toOrderItemResponseList(items));
        return response;
    }

    @Mapping(source = "id", target = "itemId")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);

    List<OrderItemResponse> toOrderItemResponseList(List<OrderItem> orderItems);
}
