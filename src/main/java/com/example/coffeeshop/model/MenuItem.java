package com.example.coffeeshop.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String category;
    private BigDecimal price;
    private boolean available = true;

    public MenuItem() {
    }

    public MenuItem(String name, String category, BigDecimal price, Boolean available) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.available = available;
    }

    // TODO: include setId?

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean isAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
