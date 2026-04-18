package com.utsav.payment_service.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.utsav.payment_service.event.InventoryReservedEvent;
import com.utsav.payment_service.service.PaymentService;

@Component
public class InventoryReservedEventListener {
  private final PaymentService paymentService;

  public InventoryReservedEventListener(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @KafkaListener(topics = "inventory-reserved", groupId = "payment-service-group")
  public void handleInventoryReservedEvent(InventoryReservedEvent event) {
    paymentService.createPaymentForOrder(event);
  }
}
