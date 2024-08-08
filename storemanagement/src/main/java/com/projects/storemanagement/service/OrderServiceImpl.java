package com.projects.storemanagement.service;

import com.projects.storemanagement.controller.dto.OrderProductDTO;
import com.projects.storemanagement.entity.*;
import com.projects.storemanagement.exception.*;
import com.projects.storemanagement.repository.OrderRepository;
import com.projects.storemanagement.repository.ProductRepository;
import com.projects.storemanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
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
    @Transactional
    public Order create(List<OrderProductDTO> orderProductDTOList, User authenticatedUser) {
        Order order = new Order();
        order.setUser(authenticatedUser);
        orderRepository.save(order); // to have the PK generated

        List<OrderProduct> orderProducts = getOrderProductList(orderProductDTOList, order);
        if(orderProducts.isEmpty()) {
            throw new ProductsInOrderNotFoundException();
        }
        order.setOrderProducts(orderProducts);

        return orderRepository.save(order);
    }

    @Override
    public List<Order> findByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    private List<OrderProduct> getOrderProductList(List<OrderProductDTO> orderProductDTOList, Order order) {
        return orderProductDTOList.stream()
                .map(dto -> {
                    Product product = productRepository.findById(dto.getProductId())
                            .orElseThrow(() -> new ProductNotFoundException(dto.getProductId()));

                    if(!product.getStatus().equals("enabled")) {
                        throw new ProductIsNotAvailableException(product.getId());
                    }

                    if(product.getQuantity() < dto.getQuantity()) {
                        throw new InsufficientProductQuantityException(dto.getProductId(), dto.getQuantity());
                    }

                    product.setQuantity(product.getQuantity() - dto.getQuantity());
                    productRepository.save(product);

                    OrderProduct orderProduct = new OrderProduct();
                    orderProduct.setId(new OrderProductPk(order.getId(), product.getId()));
                    orderProduct.setProduct(product);
                    orderProduct.setQuantity(dto.getQuantity());
                    orderProduct.setPrice(dto.getPrice());
                    return orderProduct;
                })
                .collect(Collectors.toList());
    }

}