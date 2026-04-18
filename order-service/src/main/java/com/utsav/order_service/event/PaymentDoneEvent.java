package com.utsav.order_service.event;

import java.math.BigDecimal;

public record PaymentDoneEvent(long orderId, BigDecimal amount) {}
