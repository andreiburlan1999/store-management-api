package com.projects.storemanagement.service;

import com.projects.storemanagement.controller.dto.OrderProductDTO;
import com.projects.storemanagement.entity.Order;
import com.projects.storemanagement.entity.User;

import java.util.List;

public interface OrderService {

    Order findById(Long id);
    List<Order> findAll();
    Order create(List<OrderProductDTO> orderProductDTOList, User authenticatedUser);
    List<Order> findByUser(Long userId);

}
