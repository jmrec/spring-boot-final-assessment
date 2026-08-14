package com.example.coffeeshop.service;

import com.example.coffeeshop.exception.MenuItemNotFoundException;
import com.example.coffeeshop.model.MenuItem;
import com.example.coffeeshop.repository.MenuItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {
    private static final Logger log = LoggerFactory.getLogger(MenuService.class);
    private final MenuItemRepository repo;

    private MenuItem findMenuItemOrThrow(Long id) {
        MenuItem item = repo.findById(id)
                            .orElseThrow(() -> {
                                log.warn("Menu item {} not found", id);
                                return new MenuItemNotFoundException(id);
                            });
        log.info("Fetched menu item {}", id);
        return item;
    }

    public MenuService(MenuItemRepository repo) {
        this.repo = repo;
    }

    public List<MenuItem> getAllItems() {
        List<MenuItem> items = repo.findAll();
        log.info("Returned all {} menu items", items.size());
        return items;
    }

    public MenuItem getItemById(Long id) {
        return findMenuItemOrThrow(id);
    }

    public MenuItem createItem(MenuItem item) {
        MenuItem saved = repo.save(item);
        log.info("Created menu item {}", saved.getId());
        return saved;
    }

    public MenuItem updateItem(Long id, MenuItem updated) {
        MenuItem existing = findMenuItemOrThrow(id);

        existing.setName(updated.getName());
        existing.setCategory(updated.getCategory());
        existing.setPrice(updated.getPrice());
        existing.setAvailable(updated.isAvailable());

        repo.save(existing);
        log.info("Updated menu item {}", id);

        return existing;
    }

    public void deleteItem(Long id) {
        findMenuItemOrThrow(id);
        repo.deleteById(id);
        log.info("Deleted menu item {}", id);
    }

    public List<MenuItem> getByCategory(String category) {
        List<MenuItem> items = repo.findByCategoryIgnoreCase(category);
        log.info("Returned {} menu items for {}", items.size(), category);
        return items;
    }

    public List<MenuItem> getAvailableItems() {
        List<MenuItem> items = repo.findByAvailableTrue();
        log.info("Returned {} available menu items", items.size());
        return items;
    }
}
