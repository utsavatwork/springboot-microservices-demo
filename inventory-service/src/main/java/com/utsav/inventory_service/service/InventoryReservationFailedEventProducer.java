package com.utsav.inventory_service.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.utsav.inventory_service.event.InventoryReservationFailedEvent;

@Service
public class InventoryReservationFailedEventProducer {
  private final KafkaTemplate<String, InventoryReservationFailedEvent> kafkaTemplate;
  
  public InventoryReservationFailedEventProducer(KafkaTemplate<String, InventoryReservationFailedEvent> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void publishInventoryReservationFailedEvent(InventoryReservationFailedEvent event) {
    this.kafkaTemplate.send("inventory-reservation-failed", event);
  }
}
