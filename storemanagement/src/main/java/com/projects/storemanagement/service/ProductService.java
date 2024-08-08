package com.projects.storemanagement.service;

import com.projects.storemanagement.entity.Product;

import java.util.List;

public interface ProductService {

    Product findById(Long id);
    List<Product> findAll();
    Product create(Product product);
    Product update(Long id, Product product);
    void disableProduct(Long id);
    List<Product> findProductsByCategory(Long categoryId);

}
