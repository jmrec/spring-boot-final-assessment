package com.example.coffeeshop.controller;

import com.example.coffeeshop.dto.OrderRequest;
import com.example.coffeeshop.dto.OrderResponse;
import com.example.coffeeshop.model.Order;
import com.example.coffeeshop.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderRequest request) {
        Order saved = service.createOrder(request.getItemIds());
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(OrderResponse.from(saved));
    }

    @GetMapping
    public List<OrderResponse> getAll() {
        return service.getAllOrders()
                      .stream()
                      .map(OrderResponse::from)
                      .toList();
    }

    @GetMapping("/{id}")
    public OrderResponse getById(@PathVariable Long id) {
        return OrderResponse.from(service.getOrderById(id));
    }

    @PostMapping("/{id}/ready")
    public OrderResponse markReady(@PathVariable Long id) {
        return OrderResponse.from(service.markReady(id));
    }

    @PostMapping("/{id}/pay")
    public OrderResponse markPaid(@PathVariable Long id) {
        return OrderResponse.from(service.markPaid(id));
    }
}
