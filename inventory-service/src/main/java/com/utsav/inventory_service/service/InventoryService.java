package com.utsav.inventory_service.service;

import com.utsav.inventory_service.dto.CreateInventoryRequest;
import com.utsav.inventory_service.dto.InventoryResponse;
import com.utsav.inventory_service.entity.Inventory;
import com.utsav.inventory_service.event.InventoryReservationFailedEvent;
import com.utsav.inventory_service.event.InventoryReservedEvent;
import com.utsav.inventory_service.exception.InsufficientInventoryException;
import com.utsav.inventory_service.exception.InventoryAlreadyExistsException;
import com.utsav.inventory_service.exception.InventoryNotFoundException;
import com.utsav.inventory_service.repository.InventoryRepository;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryEventProducer inventoryEventProducer;
    private final InventoryReservationFailedEventProducer inventoryReservationFailedEventProducer;

    public InventoryService(InventoryRepository inventoryRepository,
        InventoryEventProducer inventoryEventProducer, InventoryReservationFailedEventProducer inventoryReservationFailedEventProducer) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryEventProducer = inventoryEventProducer;
        this.inventoryReservationFailedEventProducer = inventoryReservationFailedEventProducer;
    }

    @Transactional
    public InventoryResponse createInventory(CreateInventoryRequest request) {
        if (inventoryRepository.existsByProductId(request.productId())) {
            throw new InventoryAlreadyExistsException(request.productId());
        }

        Inventory inventory = Inventory.builder()
                .productId(request.productId())
                .availableQuantity(request.availableQuantity())
                .reservedQuantity(0)
                .build();

        inventory = inventoryRepository.save(inventory);
        return mapToResponse(inventory);
    }

    @Transactional(readOnly = true)
    public InventoryResponse getInventoryByProductId(Long productId) {
        return mapToResponse(fetchByProductId(productId));
    }

    @Transactional
    public InventoryResponse restockInventory(Long productId, Integer quantity) {
        Inventory inventory = fetchByProductId(productId);
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() + quantity);
        return mapToResponse(inventoryRepository.save(inventory));
    }

    @Transactional
    public InventoryResponse reserveInventory(Long productId, Integer quantity) {
        Inventory inventory = reserveStock(productId, quantity);
        return mapToResponse(inventory);
    }

    @Transactional
    public void reserveInventoryForOrder(
            Long orderId, Long productId, Integer quantity, Long customerId, BigDecimal amount) {
        log.info("Here at Inventory Service");
        try {
            reserveStock(productId, quantity);

            InventoryReservedEvent event =
                new InventoryReservedEvent(orderId, productId, quantity, customerId, amount);
            inventoryEventProducer.publishInventoryReservedEvent(event);
        } catch (InsufficientInventoryException e) {

            log.error("Insufficient inventory exception: " + e.getLocalizedMessage());

            InventoryReservationFailedEvent event = new InventoryReservationFailedEvent(orderId);
            inventoryReservationFailedEventProducer.publishInventoryReservationFailedEvent(event);
        }
    }

    @Transactional
    public InventoryResponse releaseInventory(Long productId, Integer quantity) {
        Inventory inventory = fetchByProductId(productId);
        if (inventory.getReservedQuantity() < quantity) {
            throw new InsufficientInventoryException(
                    productId, quantity, inventory.getReservedQuantity());
        }

        inventory.setReservedQuantity(inventory.getReservedQuantity() - quantity);
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() + quantity);
        return mapToResponse(inventoryRepository.save(inventory));
    }

    private Inventory fetchByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException(productId));
    }

    private Inventory reserveStock(Long productId, Integer quantity) {
        Inventory inventory = fetchByProductId(productId);
        if (inventory.getAvailableQuantity() < quantity) {
            throw new InsufficientInventoryException(
                    productId, quantity, inventory.getAvailableQuantity());
        }

        inventory.setAvailableQuantity(inventory.getAvailableQuantity() - quantity);
        inventory.setReservedQuantity(inventory.getReservedQuantity() + quantity);
        return inventoryRepository.save(inventory);
    }

    private InventoryResponse mapToResponse(Inventory inventory) {
        return new InventoryResponse(
                inventory.getId(),
                inventory.getProductId(),
                inventory.getAvailableQuantity(),
                inventory.getReservedQuantity(),
                inventory.getAvailableQuantity() + inventory.getReservedQuantity(),
                inventory.getCreatedAt(),
                inventory.getUpdatedAt());
    }
}
