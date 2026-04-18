package com.utsav.inventory_service.exception;

public class InsufficientInventoryException extends RuntimeException {

    public InsufficientInventoryException(Long productId, Integer requested, Integer available) {
        super("Insufficient inventory for productId=" + productId
                + ", requested=" + requested
                + ", available=" + available);
    }
}
