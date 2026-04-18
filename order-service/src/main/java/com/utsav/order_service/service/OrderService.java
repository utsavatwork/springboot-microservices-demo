package com.utsav.order_service.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.utsav.order_service.dto.CreateOrderRequest;
import com.utsav.order_service.dto.OrderResponse;
import com.utsav.order_service.entity.Order;
import com.utsav.order_service.entity.OrderStatus;
import com.utsav.order_service.event.InventoryReservationFailedEvent;
import com.utsav.order_service.event.OrderCreatedEvent;
import com.utsav.order_service.event.PaymentDoneEvent;
import com.utsav.order_service.exception.OrderNotFoundException;
import com.utsav.order_service.repository.OrderRepository;

import org.springframework.stereotype.Service;

@Service
public class OrderService {
  private final OrderRepository orderRepository;
  private final OrderEventProducer orderEventProducer;
  private final ProductClient productClient;

  public OrderService(
      OrderRepository orderRepository,
      OrderEventProducer orderEventProducer,
      ProductClient productClient) {
    this.orderRepository = orderRepository;
    this.orderEventProducer = orderEventProducer;
    this.productClient = productClient;
  }

  public OrderResponse createOrder(CreateOrderRequest request) {
    BigDecimal amount = productClient.getProduct(request.productId())
        .price()
        .multiply(BigDecimal.valueOf(request.quantity()));

    Order order = new Order();
    order.setProductId(request.productId());
    order.setCustomerId(request.customerId());
    order.setQuantity(request.quantity());
    order.setAmount(amount);

    // Set status to "Created" at the time of creation.
    order.setStatus(OrderStatus.CREATED);

    // Save/Create new order
    order = orderRepository.save(order);

    // Publish "order-created" event
    OrderCreatedEvent orderCreatedEvent = 
        new OrderCreatedEvent(
            order.getId(),
            order.getProductId(),
            order.getQuantity(),
            order.getCustomerId(),
            order.getAmount());
    orderEventProducer.publishOrderCreatedEvent(orderCreatedEvent);

    return orderResponseMapper(order);
  }

  public OrderResponse denyOrder(InventoryReservationFailedEvent event) {
    Long orderId = event.orderId();

    Order order = updateOrderStatus(orderId, OrderStatus.FAILED);

    return orderResponseMapper(orderRepository.save(order));
  }

  public OrderResponse confirmOrder(PaymentDoneEvent event) {
    Long orderId = event.orderId();

    Order order = updateOrderStatus(orderId, OrderStatus.CONFIRMED);

    return orderResponseMapper(orderRepository.save(order));
  }

  public List<OrderResponse> getOrders() {
    return orderRepository.findAll().stream().map(order -> orderResponseMapper(order)).toList();
  }

  public Order updateOrderStatus(Long orderId, OrderStatus updatedStatus) {
    Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));

    // Update the status
    order.setStatus(updatedStatus);

    return orderRepository.save(order);
  }

  private OrderResponse orderResponseMapper(Order order) {
    return new OrderResponse(order.getId(), order.getProductId(), order.getQuantity(),
          order.getCustomerId(), order.getAmount(), order.getStatus(), order.getCreatedAt());
  }
}
