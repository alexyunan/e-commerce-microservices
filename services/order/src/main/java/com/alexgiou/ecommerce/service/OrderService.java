package com.alexgiou.ecommerce.service;

import com.alexgiou.ecommerce.customer.CustomerClient;
import com.alexgiou.ecommerce.exception.BusinessException;
import com.alexgiou.ecommerce.kafka.OrderConfirmation;
import com.alexgiou.ecommerce.kafka.OrderProducer;
import com.alexgiou.ecommerce.mapper.OrderMapper;
import com.alexgiou.ecommerce.order.OrderRequest;
import com.alexgiou.ecommerce.order.OrderResponse;
import com.alexgiou.ecommerce.orderline.OrderLineRequest;
import com.alexgiou.ecommerce.product.ProductClient;
import com.alexgiou.ecommerce.product.PurchaseRequest;
import com.alexgiou.ecommerce.product.PurchaseResponse;
import com.alexgiou.ecommerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;

    public Integer createOrder(OrderRequest request) {
        //check customer --> OpenFeign
        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order:: No customer exists with the provided ID"));

        // purchase the products --> product-ms (RestTemplate)
        List<PurchaseResponse> purchaseProducts = this.productClient.purchaseProducts(request.products());

        // persist order
        var order = this.repository.save(mapper.toOrder(request));

        // persist order lines
        for (PurchaseRequest purchaseRequest : request.products()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }
        // start payment process

        // send the order confirmation --> notification-ms(kafka)
        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchaseProducts
                ));

        return  order.getId();
    }

    public List<OrderResponse> findAllOrders() {
        return repository.findAll()
                .stream()
                .map(mapper::toOrderResponse)
                .toList();
    }

    public OrderResponse findOrderById(Integer id) {
        return repository.findById(id)
                .map(mapper::toOrderResponse)
                .orElseThrow(() -> new BusinessException("No order found with the provided ID: %d".formatted(id)));
    }
}
