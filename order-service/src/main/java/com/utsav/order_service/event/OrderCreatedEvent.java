package com.utsav.order_service.event;

import java.math.BigDecimal;

public record OrderCreatedEvent(
        long orderId,
        long productId,
        int quantity,
        long customerId,
        BigDecimal amount) {
}
