package com.utsav.inventory_service.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateInventoryRequest(
        @Positive long productId,
        @PositiveOrZero int availableQuantity) {
}
