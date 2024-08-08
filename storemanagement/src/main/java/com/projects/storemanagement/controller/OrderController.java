package com.projects.storemanagement.controller;

import com.projects.storemanagement.controller.dto.OrderProductDTO;
import com.projects.storemanagement.entity.Order;
import com.projects.storemanagement.entity.User;
import com.projects.storemanagement.service.OrderService;
import com.projects.storemanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Order> findById(@PathVariable Long id) {
        Order order = orderService.findById(id);
        order.getUser().setPassword("*");
        return ResponseEntity.ok(order);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Order>> findAll() {
        List<Order> orders = orderService.findAll();
        orders.forEach(order -> order.getUser().setPassword("*"));
        return ResponseEntity.ok(orders);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Order> create(@RequestBody List<OrderProductDTO> orderProductDTOList, Authentication authentication) {
        User authenticatedUser = userService.getCurrentUser();
        Order createdOrder = orderService.create(orderProductDTOList, authenticatedUser);
        createdOrder.getUser().setPassword("*");
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @GetMapping("/user-orders")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<List<Order>> findByCurrentUser() {
        User authenticatedUser = userService.getCurrentUser();
        List<Order> orders = orderService.findByUser(authenticatedUser.getId());
        orders.forEach(order -> order.getUser().setPassword("*"));
        return ResponseEntity.ok(orders);
    }

}
