package com.alexgiou.ecommerce.product;

import com.alexgiou.ecommerce.exception.ProductPurchaseException;
import com.alexgiou.ecommerce.purchase.ProductPurchaseRequest;
import com.alexgiou.ecommerce.purchase.ProductPurchaseResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public Integer createProduct(ProductRequest request) {
        var product = mapper.toProduct(request);
        return repository.save(product).getId();
    }


    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> request) {
        // Extract product IDs from requests
        var productIds = request.stream()
                .map(ProductPurchaseRequest::productId)
                .toList();

        // Retrieve products from database by IDs
        var storedProducts = repository.findAllByIdInOrderById(productIds);

        // Check if any requested products are missing
        if (productIds.size() != storedProducts.size()) {
            throw new ProductPurchaseException("One ore more products does not exists");
        }

        // Sort requests by product ID to match retrieved products
        var storedRequest = request.stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();

        var purchaseProducts = new ArrayList<ProductPurchaseResponse>();

        // Process each product and request
        for (int i = 0; i < storedProducts.size(); i++) {
            var product = storedProducts.get(i);
            var productRequest = storedRequest.get(i);
            // Check if there is enough stock
            if (product.getAvailableQuantity() < productRequest.quantity()) {
                throw new ProductPurchaseException("Insufficient stock quantity for product with ID:: " + productRequest.productId());
            }
            var newAvailableQuantity = product.getAvailableQuantity() - productRequest.quantity();
            // Update product quantity and save
            product.setAvailableQuantity(newAvailableQuantity);
            repository.save(product);
            // Create and add purchase response
            purchaseProducts.add(mapper.toProductPurchaseResponse(product, productRequest.quantity()));
        }

        return purchaseProducts;
    }

    public ProductResponse findById(Integer productId) {
        return repository.findById(productId)
                .map(mapper::toProductResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with the ID :: " + productId));
    }

    public List<ProductResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toProductResponse)
                .collect(Collectors.toList());
    }
}
