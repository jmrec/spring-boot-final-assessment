package com.example.coffeeshop.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super("Order %d not found".formatted(id));
    }
}
