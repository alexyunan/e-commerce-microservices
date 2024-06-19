package com.alexgiou.ecommerce.kafka.order;

import java.math.BigDecimal;

public record Product(
        Integer productId,
        String productName,
        String description,
        BigDecimal price,
        double quantity
) {
}
