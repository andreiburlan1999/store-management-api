package com.projects.storemanagement.service;

import com.projects.storemanagement.controller.dto.OrderProductDTO;
import com.projects.storemanagement.entity.Order;
import com.projects.storemanagement.entity.Product;
import com.projects.storemanagement.entity.User;
import com.projects.storemanagement.exception.*;
import com.projects.storemanagement.repository.OrderRepository;
import com.projects.storemanagement.repository.ProductRepository;
import com.projects.storemanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        Order order = new Order();
        order.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order foundOrder = orderService.findById(1L);

        assertNotNull(foundOrder);
        assertEquals(1L, foundOrder.getId());
    }

    @Test
    void testFindByIdThrowsException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.findById(1L));
    }

    @Test
    void testFindAll() {
        Order order1 = new Order();
        order1.setId(1L);
        Order order2 = new Order();
        order2.setId(2L);
        when(orderRepository.findAll()).thenReturn(List.of(order1, order2));

        List<Order> orders = orderService.findAll();

        assertNotNull(orders);
        assertEquals(2, orders.size());
    }

    @Test
    void testCreate() {
        OrderProductDTO orderProductDto = new OrderProductDTO();
        orderProductDto.setProductId(1L);
        orderProductDto.setQuantity(2);
        orderProductDto.setPrice(100.0);
        List<OrderProductDTO> orderProductDTOList = List.of(orderProductDto);

        User user = new User();
        user.setId(1L);
        Product product = new Product();
        product.setId(1L);
        product.setQuantity(10);
        product.setStatus("enabled");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(new Order());

        Order createdOrder = orderService.create(orderProductDTOList, user);

        assertNotNull(createdOrder);
        assertEquals(8, product.getQuantity());
        verify(productRepository, times(1)).save(product);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testCreateThrowsProductNotFoundException() {
        OrderProductDTO orderProductDto = new OrderProductDTO();
        orderProductDto.setProductId(1L);
        orderProductDto.setQuantity(2);
        orderProductDto.setPrice(100.0);
        List<OrderProductDTO> orderProductDTOList = List.of(orderProductDto);

        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> orderService.create(orderProductDTOList, user));
    }

    @Test
    void testCreateThrowsProductIsNotAvailableException() {
        OrderProductDTO orderProductDto = new OrderProductDTO();
        orderProductDto.setProductId(1L);
        orderProductDto.setQuantity(20);
        orderProductDto.setPrice(100.0);
        List<OrderProductDTO> orderProductDTOList = List.of(orderProductDto);

        User user = new User();
        user.setId(1L);
        Product product = new Product();
        product.setId(1L);
        product.setQuantity(10);
        product.setStatus("disabled");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(ProductIsNotAvailableException.class, () -> orderService.create(orderProductDTOList, user));
    }

    @Test
    void testCreateThrowsInsufficientProductQuantityException() {
        OrderProductDTO orderProductDto = new OrderProductDTO();
        orderProductDto.setProductId(1L);
        orderProductDto.setQuantity(20);
        orderProductDto.setPrice(100.0);
        List<OrderProductDTO> orderProductDTOList = List.of(orderProductDto);

        User user = new User();
        user.setId(1L);
        Product product = new Product();
        product.setId(1L);
        product.setQuantity(10);
        product.setStatus("enabled");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(InsufficientProductQuantityException.class, () -> orderService.create(orderProductDTOList, user));
    }

    @Test
    void testCreateThrowsProductsInOrderNotFound() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(ProductsInOrderNotFoundException.class, () -> orderService.create(Collections.emptyList(), user));
    }

    @Test
    void testFindByUser() {
        Order order1 = new Order();
        order1.setId(1L);
        Order order2 = new Order();
        order2.setId(2L);
        when(orderRepository.findByUserId(1L)).thenReturn(List.of(order1, order2));

        List<Order> products = orderService.findByUser(1L);

        assertNotNull(products);
        assertEquals(2, products.size());
    }

}
