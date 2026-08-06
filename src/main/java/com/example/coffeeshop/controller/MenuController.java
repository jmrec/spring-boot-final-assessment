package com.example.coffeeshop.controller;

import com.example.coffeeshop.dto.MenuItemRequest;
import com.example.coffeeshop.dto.MenuItemResponse;
import com.example.coffeeshop.model.MenuItem;
import com.example.coffeeshop.service.MenuService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {
    private final MenuService service;

    public MenuController(MenuService service) {
        this.service = service;
    }

    @GetMapping
    public List<MenuItemResponse> getAll() {
        return service.getAllItems()
                      .stream()
                      .map(MenuItemResponse::from)
                      .toList();
    }

    @GetMapping("/{id}")
    public MenuItemResponse getById(@PathVariable Long id) {
        return MenuItemResponse.from(service.getItemById(id));
    }

    @PostMapping
    public ResponseEntity<MenuItemResponse> create(@Valid @RequestBody MenuItemRequest request) {
        MenuItem item = MenuItemRequest.toEntity(request);
        MenuItemResponse saved = MenuItemResponse.from(service.createItem(item));

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(saved);
    }

    @PutMapping("/{id}")
    public MenuItemResponse update(@PathVariable Long id, @Valid @RequestBody MenuItemRequest request) {
        MenuItem saved = service.updateItem(id, MenuItemRequest.toEntity(request));
        return MenuItemResponse.from(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteItem(id);
        return ResponseEntity.noContent()
                             .build();
    }

    @GetMapping("/category/{category}")
    public List<MenuItemResponse> getByCategory(@PathVariable String category) {
        return service.getByCategory(category)
                      .stream()
                      .map(MenuItemResponse::from)
                      .toList();
    }

    @GetMapping("/available")
    public List<MenuItemResponse> getAvailable() {
        return service.getAvailableItems()
                      .stream()
                      .map(MenuItemResponse::from)
                      .toList();
    }
}
