package com.utsav.product_service.service;

import com.utsav.product_service.dto.CreateProductRequest;
import com.utsav.product_service.dto.ProductResponse;
import com.utsav.product_service.exception.ProductNotFoundException;
import com.utsav.product_service.entity.Product;
import com.utsav.product_service.repository.ProductRepository;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        Product product = Product.builder()
                .name(request.name())
                .price(request.price())
                .build();
        return mapToProductResponse(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long productId) {
        return mapToProductResponse(getProductEntity(productId));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToProductResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Product getProductEntity(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    private ProductResponse mapToProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getCreatedAt());
    }
}
