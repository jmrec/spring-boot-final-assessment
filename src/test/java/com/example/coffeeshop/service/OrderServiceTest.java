package com.example.coffeeshop.service;

import com.example.coffeeshop.exception.InvalidOrderException;
import com.example.coffeeshop.exception.InvalidStatusTransitionException;
import com.example.coffeeshop.model.MenuItem;
import com.example.coffeeshop.model.Order;
import com.example.coffeeshop.model.OrderStatus;
import com.example.coffeeshop.repository.MenuItemRepository;
import com.example.coffeeshop.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {
    @Mock
    OrderRepository orderRepo;

    @Mock
    MenuItemRepository menuRepo;

    @InjectMocks
    OrderService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder() {
        final Long item1Id = 1L;
        final Long item2Id = 2L;
        final List<Long> itemIds = List.of(item1Id, item2Id);

        MenuItem item1 = new MenuItem("item1", "cat1", new BigDecimal("3.75"), true);
        ReflectionTestUtils.setField(item1, "id", item1Id);

        MenuItem item2 = new MenuItem("item2", "cat2", new BigDecimal("2.95"), true);
        ReflectionTestUtils.setField(item2, "id", item2Id);

        when(menuRepo.findById(item1Id)).thenReturn(Optional.of(item1));
        when(menuRepo.findById(item2Id)).thenReturn(Optional.of(item2));
        when(orderRepo.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = service.createOrder(itemIds);

        assertEquals(new BigDecimal("6.70"), result.getTotal());
        verify(menuRepo).findById(item1Id);
        verify(menuRepo).findById(item2Id);
        verify(orderRepo).save(any(Order.class));
    }

    @Test
    void testCreateOrder_ThrowsWhenEmpty() {
        assertThrows(InvalidOrderException.class, () -> service.createOrder(List.of()));

        verifyNoInteractions(orderRepo, menuRepo);
    }


    @Test
    void testMarkReady_FromPending() {
        final Long orderId = 1L;
        Order order = new Order(List.of(5L, 10L), new BigDecimal("500.50"));
        ReflectionTestUtils.setField(order, "id", orderId);

        order.setStatus(OrderStatus.PENDING);

        when(orderRepo.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepo.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = service.markReady(orderId);

        assertEquals(OrderStatus.READY, result.getStatus());
        verify(orderRepo).findById(orderId);
        verify(orderRepo).save(any(Order.class));
    }

    @Test
    void testMarkPaid_FromPending() {
        final Long orderId = 1L;
        Order order = new Order(List.of(5L, 10L), new BigDecimal("500.50"));
        ReflectionTestUtils.setField(order, "id", orderId);
        order.setStatus(OrderStatus.PENDING);

        when(orderRepo.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(InvalidStatusTransitionException.class, () -> service.markPaid(orderId));
        verify(orderRepo).findById(orderId);
        verify(orderRepo, never()).save(any(Order.class));
    }
}