package com.projects.storemanagement.service;

import com.projects.storemanagement.controller.dto.CreateOrderDto;
import com.projects.storemanagement.entity.Order;
import com.projects.storemanagement.entity.OrderProduct;
import com.projects.storemanagement.exception.OrderNotFoundException;
import com.projects.storemanagement.exception.ProductNotFoundException;
import com.projects.storemanagement.exception.ProductsInOrderNotFound;
import com.projects.storemanagement.exception.UserNotFoundException;
import com.projects.storemanagement.repository.OrderRepository;
import com.projects.storemanagement.repository.ProductRepository;
import com.projects.storemanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order create(CreateOrderDto createOrderDto) {
        Order order = new Order();
        order.setUser(userRepository.findById(createOrderDto.getCustomerId())
                .orElseThrow(() -> new UserNotFoundException(createOrderDto.getCustomerId())));

        List<OrderProduct> orderProducts = getOrderProductList(createOrderDto);
        if(orderProducts.isEmpty()) {
            throw new ProductsInOrderNotFound();
        }
        order.setOrderProducts(orderProducts);

        return orderRepository.save(order);
    }

    private List<OrderProduct> getOrderProductList(CreateOrderDto createOrderDto) {
        return createOrderDto.getProducts().stream()
                .map(dto -> {
                    OrderProduct orderProduct = new OrderProduct();
                    orderProduct.setProduct(productRepository.findById(dto.getProductId())
                            .orElseThrow(() -> new ProductNotFoundException(dto.getProductId())));
                    orderProduct.setQuantity(dto.getQuantity());
                    orderProduct.setPrice(dto.getPrice());
                    return orderProduct;
                })
                .collect(Collectors.toList());
    }

}
