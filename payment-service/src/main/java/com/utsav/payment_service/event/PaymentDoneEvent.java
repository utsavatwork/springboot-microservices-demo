package com.utsav.payment_service.event;

import java.math.BigDecimal;

public record PaymentDoneEvent(long orderId, BigDecimal amount) {}
