package com.alexgiou.ecommerce.payment;

import com.alexgiou.ecommerce.customer.Customer;

import java.math.BigDecimal;

public record PaymentRequest(
        Integer id,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        Customer customer
) {
}
