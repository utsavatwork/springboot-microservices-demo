package com.utsav.payment_service.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreatePaymentRequest(
    @Positive long orderId,
    @Positive long customerId,
    @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal amount
) {}
