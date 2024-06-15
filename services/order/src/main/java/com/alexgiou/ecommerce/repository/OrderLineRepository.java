package com.alexgiou.ecommerce.repository;

import com.alexgiou.ecommerce.orderline.OrderLine;
import com.alexgiou.ecommerce.orderline.OrderLineResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderLineRepository extends JpaRepository<OrderLine,Integer> {
    List<OrderLine> findAllByOrderId(Integer orderId);

}
