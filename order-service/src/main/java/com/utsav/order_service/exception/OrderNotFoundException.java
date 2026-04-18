package com.utsav.order_service.exception;

public class OrderNotFoundException extends RuntimeException {
  public OrderNotFoundException(Long orderId) {
    super("Order with id: " + orderId + " is not found.");
  }
}
