package com.utsav.product_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse(
        long id,
        String name,
        BigDecimal price,
        LocalDateTime createdAt) {
}
