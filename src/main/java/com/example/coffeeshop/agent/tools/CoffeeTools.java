package com.example.coffeeshop.agent.tools;

import com.example.coffeeshop.model.MenuItem;
import com.example.coffeeshop.model.Order;
import com.example.coffeeshop.service.MenuService;
import com.example.coffeeshop.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CoffeeTools {
    private final MenuService menuService;
    private final OrderService orderService;

    private static final Logger log = LoggerFactory.getLogger(CoffeeTools.class);

    public CoffeeTools(MenuService menuService, OrderService orderService) {
        this.menuService = menuService;
        this.orderService = orderService;
    }

    @Tool(description = "Search the menu for items in a given category")
    public String searchMenu(String category) {
        log.info("Agent invoked searchMenu({})", category);
        return menuService.getByCategory(category)
                          .stream()
                          .map(CoffeeTools::formatItem)
                          .collect(Collectors.joining(","));
    }

    @Tool(description = "List all menu items currently available to order")
    public String listAvailableItems() {
        log.info("Agent invoked listAvailableItems()");
        return menuService.getAvailableItems()
                          .stream()
                          .map(CoffeeTools::formatItem)
                          .collect(Collectors.joining(","));
    }

    @Tool(description = "Place a new order given a list of menu item IDs; returns order id and total")
    public String placeOrder(List<Long> itemIds) {
        log.info("Agent invoked placeOrder() with {} item IDs", itemIds.size());
        Order order = orderService.createOrder(itemIds);
        return "Created order %d ... Total: %s".formatted(order.getId(), order.getTotal());
    }

    @Tool(description = "Mark an order ready for pickup, by order ID")
    public String markOrderReady(Long orderId) {
        log.info("Agent wants to mark order {} as READY", orderId);
        orderService.markReady(orderId);
        return "Order %d is now marked as READY".formatted(orderId);
    }

    @Tool(description = "Mark an order as paid, by order ID")
    public String payOrder(Long orderId) {
        log.info("Agent wants to mark order {} as PAID", orderId);
        orderService.markPaid(orderId);
        return "Order %d is now marked as PAID".formatted(orderId);
    }

    private static String formatItem(MenuItem item) {
        return "MenuItem(Item ID: %d, Category: %s, Item Name: %s, Price: %s, Is Available: %b)".formatted(
                item.getId(),
                item.getCategory(),
                item.getName(),
                item.getPrice(),
                item.isAvailable()
        );
    }
}