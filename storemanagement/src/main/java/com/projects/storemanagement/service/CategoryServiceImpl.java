package com.projects.storemanagement.service;

import com.projects.storemanagement.entity.Category;
import com.projects.storemanagement.exception.CategoryNotFoundException;
import com.projects.storemanagement.repository.CategoryRepository;
import com.projects.storemanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public Category create(Category category) {
        category.setId(null);
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category update(Long id, Category category) {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException(id);
        }

        category.setId(id);
        return categoryRepository.save(category);
    }

}
