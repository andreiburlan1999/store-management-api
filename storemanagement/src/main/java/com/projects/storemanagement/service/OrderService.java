package com.projects.storemanagement.service;

import com.projects.storemanagement.controller.dto.CreateOrderDto;
import com.projects.storemanagement.entity.Order;

import java.util.List;

public interface OrderService {

    Order findById(Long id);
    List<Order> findAll();
    Order create(CreateOrderDto createOrderDto);

}
