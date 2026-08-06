package com.example.coffeeshop.exception;

import com.example.coffeeshop.model.OrderStatus;

public class InvalidStatusTransitionException extends RuntimeException {
    public InvalidStatusTransitionException(OrderStatus from, OrderStatus to) {
        super("Cannot move order from %s to %s".formatted(from, to));
    }
}
