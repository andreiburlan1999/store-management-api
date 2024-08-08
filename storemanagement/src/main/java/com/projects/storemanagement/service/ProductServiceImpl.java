package com.projects.storemanagement.service;

import com.projects.storemanagement.controller.dto.ProductDTO;
import com.projects.storemanagement.entity.Product;
import com.projects.storemanagement.exception.CategoryNotFoundException;
import com.projects.storemanagement.exception.ProductNotFoundException;
import com.projects.storemanagement.exception.ProductStatusIsNotValidException;
import com.projects.storemanagement.repository.CategoryRepository;
import com.projects.storemanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

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
    public Product create(ProductDTO productDTO) {
        Product product = new Product();

        if(isStatusNotValid(productDTO.getStatus())) {
            throw new ProductStatusIsNotValidException();
        }

        product.setCategory(categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(productDTO.getCategoryId())));

        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setStatus(productDTO.getStatus());

        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product update(Long id, ProductDTO productDTO) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }

        if(isStatusNotValid(productDTO.getStatus())) {
            throw new ProductStatusIsNotValidException();
        }

        Product product = productRepository.findById(id).get();

        product.setCategory(categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID")));

        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setStatus(productDTO.getStatus());

        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void disable(Long id) {
        Product product = findById(id);
        product.setStatus("disabled");
        productRepository.save(product);
    }

    @Override
    public List<Product> findByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    private boolean isStatusNotValid(String status) {
        return status == null || !(status.equals("enabled") || status.equals("disabled"));
    }

}
