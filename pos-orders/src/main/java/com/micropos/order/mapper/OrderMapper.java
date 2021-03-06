package com.micropos.order.mapper;

import com.micropos.dto.*;
import com.micropos.order.model.Item;
import com.micropos.order.model.Order;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mapper
public interface OrderMapper {

    Collection<OrderDto> toOrderDtos(Collection<Order> orders);

    Collection<Order> toOrders(Collection<OrderDto> orderDtos);

    default Order toOrder(OrderDto orderDto) {
        return new Order().id(orderDto.getId())
                .time(orderDto.getTime())
                .items(toItems(orderDto.getItems(), orderDto));
    }

    default OrderDto toOrderDto(Order order) {
        return new OrderDto().id(order.id())
                .items(toItemDtos(order.items()));
    }

    default List<Item> toItems(List<OrderItemDto> itemDtos, OrderDto orderDto) {
        if (itemDtos == null || itemDtos.isEmpty()) {
            return null;
        }
        List<Item> list = new ArrayList<>(itemDtos.size());
        for (OrderItemDto itemDto : itemDtos) {
            list.add(toItem(itemDto, orderDto));
        }

        return list;
    }

    default List<OrderItemDto> toItemDtos(List<Item> items) {
        if (items == null || items.isEmpty()) {
            return null;
        }
        List<OrderItemDto> list = new ArrayList<>(items.size());
        for (Item item : items) {
            list.add(toItemDto(item));
        }

        return list;
    }

    default OrderItemDto toItemDto(Item item) {

        return new OrderItemDto().id(item.id())
                .amount(item.quantity())
                .product(getProductDto(item));
    }

    default Item toItem(OrderItemDto itemDto, OrderDto orderDto) {
        return new Item().id(itemDto.getId())
                .orderId(orderDto.getId())
                .productId(itemDto.getProduct().getId())
                .productName(itemDto.getProduct().getName())
                .quantity(itemDto.getAmount())
                .unitPrice(itemDto.getProduct().getPrice());
    }

    default ProductDto getProductDto(Item item) {
        return new ProductDto().id(item.productId())
                .name(item.productName())
                .price(item.unitPrice());
    }
}
