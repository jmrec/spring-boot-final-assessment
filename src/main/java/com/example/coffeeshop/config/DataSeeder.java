package com.example.coffeeshop.config;

import com.example.coffeeshop.model.MenuItem;
import com.example.coffeeshop.repository.MenuItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seedMenu(MenuItemRepository repository) {
        return args -> {
            if (repository.count() > 0)
                return;
            repository.saveAll(List.of(
                    new MenuItem("Espresso", "Coffee", new BigDecimal("2.50"), true),
                    new MenuItem("Latte", "Coffee", new BigDecimal("3.75"), true),
                    new MenuItem("Cappuccino", "Coffee", new BigDecimal("3.50"), true),
                    new MenuItem("Croissant", "Pastry", new BigDecimal("2.95"), true),
                    new MenuItem("Blueberry Muffin", "Pastry", new BigDecimal("2.75"), true),
                    new MenuItem("Iced Tea", "Cold Drink", new BigDecimal("3.00"), false)
            ));
        };
    }
}