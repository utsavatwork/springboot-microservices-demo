package com.utsav.order_service.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.utsav.order_service.event.PaymentDoneEvent;
import com.utsav.order_service.service.OrderService;

@Component
public class PaymentDoneEventListener {
  private final OrderService orderService;
  
  PaymentDoneEventListener(OrderService orderService) {
    this.orderService = orderService;
  }

  @KafkaListener(topics = "payment-done", groupId = "order-service-group")
  public void handlePaymentDoneEvent(PaymentDoneEvent event) {
    orderService.confirmOrder(event);
  }
}
