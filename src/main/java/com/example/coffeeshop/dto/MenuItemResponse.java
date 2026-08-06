package com.example.coffeeshop.dto;

import com.example.coffeeshop.model.MenuItem;

import java.math.BigDecimal;

public class MenuItemResponse {
    private Long id;
    private String name;
    private String category;
    private BigDecimal price;
    private boolean available;

    public MenuItemResponse(Long id, String name, String category, BigDecimal price, boolean available) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }

    public static MenuItemResponse from(MenuItem item) {
        return new MenuItemResponse(
                item.getId(),
                                    item.getName(),
                                    item.getCategory(),
                                    item.getPrice(),
                                    item.isAvailable()
        );
    }
}
