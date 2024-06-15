package com.alexgiou.ecommerce.order;

import com.alexgiou.ecommerce.payment.PaymentMethod;
import com.alexgiou.ecommerce.product.PurchaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(
        Integer id,
        String reference,
        @Positive(message = "Order amount should be positive")
        BigDecimal amount,
        @NotBlank(message = "Payment method should be precised")
        PaymentMethod paymentMethod,
        @NotBlank(message = "Customer should be present")
        String customerId,
        @NotBlank(message = "You should at least purchase one product")
        List<PurchaseRequest> products
) {
}
