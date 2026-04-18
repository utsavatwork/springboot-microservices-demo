package com.utsav.inventory_service.dto;

import java.time.LocalDateTime;

public record InventoryResponse(
        long id,
        long productId,
        int availableQuantity,
        int reservedQuantity,
        int totalQuantity,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
