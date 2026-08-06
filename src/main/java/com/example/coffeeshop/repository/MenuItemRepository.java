package com.example.coffeeshop.repository;

import com.example.coffeeshop.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    public List<MenuItem> findByCategoryIgnoreCase(String category);

    public List<MenuItem> findByAvailableTrue();
}
