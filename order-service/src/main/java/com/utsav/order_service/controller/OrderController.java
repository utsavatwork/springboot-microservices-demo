package com.utsav.order_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utsav.order_service.dto.CreateOrderRequest;
import com.utsav.order_service.dto.OrderResponse;
import com.utsav.order_service.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {
  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping
  public OrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
    return orderService.createOrder(request);
  }

  @GetMapping
  public List<OrderResponse> getOrders() {
    return orderService.getOrders();
  }
}
