package com.utsav.product_service.controller;

import com.utsav.product_service.dto.CreateProductRequest;
import com.utsav.product_service.dto.ProductResponse;
import com.utsav.product_service.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ProductResponse createProduct(@Valid @RequestBody CreateProductRequest request) {
        return productService.createProduct(request);
    }

    @GetMapping
    public List<ProductResponse> getProducts() {
        return productService.getProducts();
    }

    @GetMapping("/{productId}")
    public ProductResponse getProduct(@PathVariable @Positive Long productId) {
        return productService.getProduct(productId);
    }
}
