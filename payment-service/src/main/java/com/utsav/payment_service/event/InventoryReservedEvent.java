package com.utsav.payment_service.event;

import java.math.BigDecimal;

public record InventoryReservedEvent(long orderId, long productId, int quantity, long customerId, BigDecimal amount) {}
