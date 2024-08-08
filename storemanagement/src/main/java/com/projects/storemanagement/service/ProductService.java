package com.projects.storemanagement.service;

import com.projects.storemanagement.controller.dto.ProductDTO;
import com.projects.storemanagement.entity.Product;

import java.util.List;

public interface ProductService {

    Product findById(Long id);
    List<Product> findAll();
    Product create(ProductDTO productDTO);
    Product update(Long id, ProductDTO productDTO);
    void disable(Long id);
    List<Product> findByCategory(Long categoryId);

}
