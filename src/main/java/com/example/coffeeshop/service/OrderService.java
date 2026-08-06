package com.example.coffeeshop.service;

import com.example.coffeeshop.exception.InvalidOrderException;
import com.example.coffeeshop.exception.InvalidStatusTransitionException;
import com.example.coffeeshop.exception.MenuItemNotFoundException;
import com.example.coffeeshop.exception.OrderNotFoundException;
import com.example.coffeeshop.model.MenuItem;
import com.example.coffeeshop.model.Order;
import com.example.coffeeshop.model.OrderStatus;
import com.example.coffeeshop.repository.MenuItemRepository;
import com.example.coffeeshop.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepo;
    private final MenuItemRepository menuRepo;

    public OrderService(OrderRepository orderRepo, MenuItemRepository menuRepo) {
        this.orderRepo = orderRepo;
        this.menuRepo = menuRepo;
    }

    private Order findOrderOrThrow(Long id) {
        Order order = orderRepo.findById(id)
                               .orElseThrow(() -> {
                                   log.warn("Order {} not found", id);
                                   return new OrderNotFoundException(id);
                               });
        log.info("Fetched order {}", id);
        return order;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = orderRepo.findAll();
        log.info("Returned all {} orders", orders.size());
        return orders;
    }

    public Order getOrderById(Long id) {
        return findOrderOrThrow(id);
    }

    public Order createOrder(List<Long> itemIds) {
        if (itemIds == null || itemIds.isEmpty())
            throw new InvalidOrderException("Item IDs is either null or empty.");

        var total = BigDecimal.ZERO;
        for (var id : itemIds) {
            MenuItem menuItem = menuRepo.findById(id)
                                        .orElseThrow(() -> new MenuItemNotFoundException(id));

            if (!menuItem.isAvailable())
                throw new InvalidOrderException("Unavailable menu item with ID: %d".formatted(menuItem.getId()));

            total = total.add(menuItem.getPrice());
        }

        Order createdOrder = orderRepo.save(new Order(itemIds, total));
        log.info("Created order {}", createdOrder.getId());
        return createdOrder;
    }

    public Order markReady(Long id) {
        Order order = findOrderOrThrow(id);
        throwIfInvalidStatusTransition(order.getStatus(), OrderStatus.READY);

        order.setStatus(OrderStatus.READY);
        orderRepo.save(order);
        log.info("Marked order {} as ready", id);
        return order;
    }

    public Order markPaid(Long id) {
        Order order = findOrderOrThrow(id);
        throwIfInvalidStatusTransition(order.getStatus(), OrderStatus.PAID);

        order.setStatus(OrderStatus.PAID);
        orderRepo.save(order);
        log.info("Marked order {} as paid", id);
        return order;
    }

    private static boolean isValidStatusTransition(OrderStatus from, OrderStatus to) {
        return switch (from) {
            case PENDING -> to == OrderStatus.READY;
            case READY -> to == OrderStatus.PAID;
            case PAID -> false;
        };
    }

    private static void throwIfInvalidStatusTransition(OrderStatus from, OrderStatus to) {
        if (!isValidStatusTransition(from, to)) {
            log.warn("Cannot move order from {} to {}", from, to);
            throw new InvalidStatusTransitionException(from, to);
        }
    }
}
