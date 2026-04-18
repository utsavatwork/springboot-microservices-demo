package com.utsav.payment_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.utsav.payment_service.entity.PaymentStatus;

public record PaymentResponse(
    long id,
    long orderId,
    long customerId,
    BigDecimal amount,
    PaymentStatus status,
    LocalDateTime createdAt
) {}
