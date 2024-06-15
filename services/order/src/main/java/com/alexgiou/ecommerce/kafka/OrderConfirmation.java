package com.alexgiou.ecommerce.kafka;

import com.alexgiou.ecommerce.customer.CustomerResponse;
import com.alexgiou.ecommerce.payment.PaymentMethod;
import com.alexgiou.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
