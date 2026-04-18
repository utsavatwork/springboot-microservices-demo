package com.utsav.inventory_service.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.utsav.inventory_service.event.InventoryReservedEvent;

@Service
public class InventoryEventProducer {
  private final KafkaTemplate<String, InventoryReservedEvent> kafkaTemplate;
  
  public InventoryEventProducer(KafkaTemplate<String, InventoryReservedEvent> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void publishInventoryReservedEvent(InventoryReservedEvent event) {
    kafkaTemplate.send("inventory-reserved", event);
  }
}
