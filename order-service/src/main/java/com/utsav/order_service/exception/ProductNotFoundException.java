package com.utsav.order_service.exception;

public class ProductNotFoundException extends RuntimeException {
  public ProductNotFoundException(Long productId) {
    super("Product with id: " + productId + " is not found.");
  }
}
