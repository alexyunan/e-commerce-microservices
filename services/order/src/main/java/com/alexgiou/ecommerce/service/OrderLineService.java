package com.alexgiou.ecommerce.service;

import com.alexgiou.ecommerce.mapper.OrderLineMapper;
import com.alexgiou.ecommerce.orderline.OrderLineRequest;
import com.alexgiou.ecommerce.orderline.OrderLineResponse;
import com.alexgiou.ecommerce.repository.OrderLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderLineService {
    private final OrderLineRepository repository;
    private final OrderLineMapper mapper;

    public void saveOrderLine(OrderLineRequest orderLineRequest) {
        var order = mapper.toOrderLine(orderLineRequest);
        repository.save(order);
    }

    public List<OrderLineResponse> findOrderLinesByOrderId(Integer orderId) {
        return repository.findAllByOrderId(orderId)
                .stream()
                . map(mapper::toOrderLineResponse)
                .collect(Collectors.toList());
    }
}
