package com.utsav.order_service.dto;

import jakarta.validation.constraints.Positive;

public record CreateOrderRequest(@Positive long productId,
    @Positive int quantity, @Positive long customerId) {}
