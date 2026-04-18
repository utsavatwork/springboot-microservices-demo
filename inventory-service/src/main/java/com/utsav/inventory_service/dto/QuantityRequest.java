package com.utsav.inventory_service.dto;

import jakarta.validation.constraints.Positive;

public record QuantityRequest(@Positive int quantity) {
}
