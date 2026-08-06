package com.example.coffeeshop.service;

import com.example.coffeeshop.exception.MenuItemNotFoundException;
import com.example.coffeeshop.model.MenuItem;
import com.example.coffeeshop.repository.MenuItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuServiceTest {
    @Mock
    MenuItemRepository repo;

    @InjectMocks
    MenuService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetItemById_ReturnsWhenPresent() {
        final Long id = 1L;
        MenuItem item = new MenuItem("Espresso", "Coffee", new BigDecimal("2.50"), true);
        ReflectionTestUtils.setField(item, "id", id);
        when(repo.findById(id)).thenReturn(Optional.of(item));

        MenuItem result = service.getItemById(id);

        assertEquals(item, result);

        verify(repo).findById(id);
    }

    @Test
    void testGetItemById_ThrowsWhenEmpty() {
        final Long id = 1L;
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThrows(MenuItemNotFoundException.class, () -> service.getItemById(id));

        verify(repo).findById(id);
    }

    @Test
    void testCreateItem() {
        final Long id = 1L;
        MenuItem item = new MenuItem("Espresso", "Coffee", new BigDecimal("2.50"), true);
        ReflectionTestUtils.setField(item, "id", id);
        when(repo.save(item)).thenReturn(item);

        MenuItem saved = service.createItem(item);

        assertEquals(item, saved);

        verify(repo).save(item);
    }
}