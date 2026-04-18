package com.utsav.order_service.service;

import com.utsav.order_service.dto.ProductResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ProductClient {
    private final RestClient restClient;

    public ProductClient(@Value("${product-service.base-url}") String productServiceBaseUrl) {
        this.restClient = RestClient.builder().baseUrl(productServiceBaseUrl).build();
    }

    public ProductResponse getProduct(Long productId) {
        return restClient.get()
                .uri("/products/{productId}", productId)
                .retrieve()
                .body(ProductResponse.class);
    }
}
