package com.utsav.order_service.service;

import com.utsav.order_service.dto.ProductResponse;
import com.utsav.order_service.exception.ProductNotFoundException;
import com.utsav.order_service.exception.ProductServiceUnavailableException;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class ProductClient {
    private final RestClient restClient;

    public ProductClient(@Value("${product-service.base-url}") String productServiceBaseUrl) {
        this.restClient = RestClient.builder().baseUrl(productServiceBaseUrl).build();
    }

    @Retry(name = "productClient", fallbackMethod = "productServiceFallback")
    @CircuitBreaker(name = "productClient", fallbackMethod = "productServiceFallback")
    @Bulkhead(name = "productClient", type = Bulkhead.Type.SEMAPHORE)
    public ProductResponse getProduct(Long productId) {
        try {
            log.info("In getProduct client");
            
            ProductResponse response = restClient.get()
                .uri("/products/{productId}", productId)
                .retrieve()
                .body(ProductResponse.class);

            if (response == null) {
                throw new ProductNotFoundException(productId);
            }

            return response;
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ProductNotFoundException(productId);
        }
    }

    private ProductResponse productServiceFallback(Long productId, Throwable throwable) {
        if (throwable instanceof ProductNotFoundException productNotFoundException) {
            throw productNotFoundException;
        }

        log.error("Product service unavailable for productId={}", productId, throwable);
        throw new ProductServiceUnavailableException(
            "Product service is temporarily unavailable. Please try again later.",
            throwable);
    }
}
