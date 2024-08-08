package com.projects.storemanagement.service;

import com.projects.storemanagement.controller.dto.CreateOrderDTO;
import com.projects.storemanagement.entity.Order;

import java.util.List;

public interface OrderService {

    Order findById(Long id);
    List<Order> findAll();
    Order create(CreateOrderDTO createOrderDto);

}
