package com.utsav.order_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.utsav.order_service.entity.OrderStatus;

public record OrderResponse(long id, long productId, int quantity, long customerId,
    BigDecimal amount, OrderStatus status, LocalDateTime createdAt) {}
