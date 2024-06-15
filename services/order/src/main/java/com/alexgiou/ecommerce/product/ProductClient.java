package com.alexgiou.ecommerce.product;

import com.alexgiou.ecommerce.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductClient {
    @Value("${application.config.product-url}")
    private String productUrl;
    private final RestTemplate restTemplate;

    /**
     * Sends a list of purchase requests to an external product service and receives a list of purchase responses.
     * <p>
     * This method makes an HTTP POST request to the product service's /purchase endpoint. It sends the provided list of
     * purchase requests as the request body and expects a list of purchase responses in return.
     * </p>
     *
     * @param requestBody a list of {@link PurchaseRequest} objects representing the products to be purchased
     * @return a list of {@link PurchaseResponse} objects representing the results of the purchase operations
     */
    public List<PurchaseResponse> purchaseProducts(List<PurchaseRequest> requestBody) {
        // Set the HTTP headers, indicating that the request body is in JSON format
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        // Create an HttpEntity object that contains the request body and headers
        HttpEntity<List<PurchaseRequest>> requestEntity = new HttpEntity<>(requestBody, headers);
        // Define the type of the response body
        ParameterizedTypeReference<List<PurchaseResponse>> responseType = new ParameterizedTypeReference<>() {
        };

        // Make the HTTP POST request and capture the response
        ResponseEntity<List<PurchaseResponse>> responseEntity = restTemplate
                .exchange(productUrl + "/purchase",
                        HttpMethod.POST,
                        requestEntity,
                        responseType
                );
        if (responseEntity.getStatusCode().isError()) {
            throw new BusinessException("An error occurred while processing the products purchase: " + responseEntity.getStatusCode());
        }
        // Return the body of the response, which is a list of PurchaseResponse objects
        return responseEntity.getBody();
    }
}
