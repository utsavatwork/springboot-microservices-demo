package com.utsav.inventory_service.controller;

import com.utsav.inventory_service.dto.CreateInventoryRequest;
import com.utsav.inventory_service.dto.InventoryResponse;
import com.utsav.inventory_service.dto.QuantityRequest;
import com.utsav.inventory_service.service.InventoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public InventoryResponse createInventory(@Valid @RequestBody CreateInventoryRequest request) {
        return inventoryService.createInventory(request);
    }

    @GetMapping("/{productId}")
    public InventoryResponse getInventory(
            @PathVariable @Positive Long productId) {
        return inventoryService.getInventoryByProductId(productId);
    }

    @PostMapping("/{productId}/restock")
    public InventoryResponse restockInventory(
            @PathVariable @Positive Long productId,
            @Valid @RequestBody QuantityRequest request) {
        return inventoryService.restockInventory(productId, request.quantity());
    }

    @PostMapping("/{productId}/reserve")
    public InventoryResponse reserveInventory(
            @PathVariable @Positive Long productId,
            @Valid @RequestBody QuantityRequest request) {
        return inventoryService.reserveInventory(productId, request.quantity());
    }

    @PostMapping("/{productId}/release")
    public InventoryResponse releaseInventory(
            @PathVariable @Positive Long productId,
            @Valid @RequestBody QuantityRequest request) {
        return inventoryService.releaseInventory(productId, request.quantity());
    }
}
