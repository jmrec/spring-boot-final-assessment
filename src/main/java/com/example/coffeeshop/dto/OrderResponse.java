package com.example.coffeeshop.dto;

import com.example.coffeeshop.model.Order;

import java.math.BigDecimal;
import java.util.List;

public class OrderResponse {
    private Long id;
    private List<Long> itemIds;
    private String status;
    private BigDecimal total;

    public OrderResponse(Long id, List<Long> itemIds, String status, BigDecimal total) {
        this.id = id;
        this.itemIds = itemIds;
        this.status = status;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public List<Long> getItemIds() {
        return itemIds;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getItemIds(),
                order.getStatus()
                     .name(),
                order.getTotal()
        );
    }
}
