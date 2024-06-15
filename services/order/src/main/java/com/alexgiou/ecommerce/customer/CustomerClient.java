package com.alexgiou.ecommerce.customer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

/**
 * This interface represents a client for interacting with the customer service.
 * It uses Feign, a declarative web service client, to simplify the HTTP request process.
 */
@FeignClient(name = "customer-service", url = "${application.config.customer-url}")
public interface CustomerClient {

    /**
     * This method is used to find a customer by their ID.
     * It sends a GET request to the customer service.
     *
     * @param customerId The ID of the customer to find.
     * @return An Optional that contains the CustomerResponse if found, or empty if not found.
     */
    @GetMapping("/{customer-id}")
    Optional<CustomerResponse> findCustomerById(@PathVariable("customer-id") String customerId);
}
