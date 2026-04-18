package com.utsav.order_service.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.utsav.order_service.event.InventoryReservationFailedEvent;
import com.utsav.order_service.service.OrderService;

@Component
public class InventoryReservationFailedEventListener {
  private final OrderService orderService;
  
  InventoryReservationFailedEventListener(OrderService orderService) {
    this.orderService = orderService;
  }

  @KafkaListener(topics = "inventory-reservation-failed", groupId = "order-service-group")
  public void handlePaymentDoneEvent(InventoryReservationFailedEvent event) {
    orderService.denyOrder(event);
  }
}
