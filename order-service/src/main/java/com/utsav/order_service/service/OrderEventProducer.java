package com.utsav.order_service.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.utsav.order_service.event.OrderCreatedEvent;

@Service
public class OrderEventProducer {
  private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;
  
  public OrderEventProducer(KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void publishOrderCreatedEvent(OrderCreatedEvent event) {
    kafkaTemplate.send("order-created", event);
  }
}
