package com.example.coffeeshop.exception;

public class MenuItemNotFoundException extends RuntimeException {
    public MenuItemNotFoundException(Long id) {
        super("Menu item %d not found".formatted(id));
    }
}
