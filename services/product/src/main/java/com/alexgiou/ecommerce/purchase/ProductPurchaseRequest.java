package com.alexgiou.ecommerce.purchase;

import jakarta.validation.constraints.NotBlank;

public record ProductPurchaseRequest(
        @NotBlank(message = "Product is mandatory")
        Integer productId,
        @NotBlank(message = "Quantity is mandatory")
        double quantity
) {
}
