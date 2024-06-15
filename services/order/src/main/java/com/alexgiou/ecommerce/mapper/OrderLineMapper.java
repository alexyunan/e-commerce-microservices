package com.alexgiou.ecommerce.mapper;

import com.alexgiou.ecommerce.order.Order;
import com.alexgiou.ecommerce.orderline.OrderLine;
import com.alexgiou.ecommerce.orderline.OrderLineRequest;
import com.alexgiou.ecommerce.orderline.OrderLineResponse;
import org.springframework.stereotype.Service;

@Service
public class OrderLineMapper {
    public OrderLine toOrderLine(OrderLineRequest orderLineRequest) {
        return OrderLine.builder()
                .id(orderLineRequest.id())
                .order(Order.builder()
                        .id(orderLineRequest.id())
                        .build())
                .productId(orderLineRequest.productId())
                .quantity(orderLineRequest.quantity())
                .build();
    }

    public OrderLineResponse toOrderLineResponse(OrderLine orderLine) {
        return new OrderLineResponse(
                orderLine.getId(),
                orderLine.getQuantity()
        );
    }
}
