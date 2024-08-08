package com.projects.storemanagement.service;

import com.projects.storemanagement.entity.Product;
import com.projects.storemanagement.exception.ProductNotFoundException;
import com.projects.storemanagement.exception.ProductStatusIsNotValidException;
import com.projects.storemanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    @Transactional
    public Product create(Product product) {
        product.setId(null);

        if(isStatusNotValid(product.getStatus())) {
            throw new ProductStatusIsNotValidException();
        }

        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product update(Long id, Product product) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }

        if(isStatusNotValid(product.getStatus())) {
            throw new ProductStatusIsNotValidException();
        }

        product.setId(id);
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void disableProduct(Long id) {
        Product product = findById(id);
        product.setStatus("disabled");
        productRepository.save(product);
    }

    @Override
    public List<Product> findProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    private boolean isStatusNotValid(String status) {
        return status == null || !(status.equals("enabled") || status.equals("disabled"));
    }

}
