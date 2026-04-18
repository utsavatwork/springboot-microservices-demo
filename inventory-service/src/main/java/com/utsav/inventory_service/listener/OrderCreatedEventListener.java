package com.utsav.inventory_service.listener;

import com.utsav.inventory_service.event.OrderCreatedEvent;
import com.utsav.inventory_service.service.InventoryService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderCreatedEventListener {
    private final InventoryService inventoryService;

    public OrderCreatedEventListener(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(topics = "order-created", groupId = "inventory-service-group")
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Here at OrderCreatedEvent listener");
        inventoryService.reserveInventoryForOrder(
                event.orderId(), event.productId(), event.quantity(), event.customerId(), event.amount());
    }
}
