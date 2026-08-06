package com.example.coffeeshop.dto;

import com.example.coffeeshop.model.Order;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class OrderRequest {
    @NotEmpty
    private List<Long> itemIds;

    public OrderRequest(List<Long> itemIds) {
        this.itemIds = itemIds;
    }

    public List<Long> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<Long> itemIds) {
        this.itemIds = itemIds;
    }
}
