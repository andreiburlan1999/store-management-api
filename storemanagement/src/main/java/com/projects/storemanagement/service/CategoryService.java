package com.projects.storemanagement.service;

import com.projects.storemanagement.entity.Category;

import java.util.List;

public interface CategoryService {

    Category findById(Long id);
    List<Category> findAll();
    Category create(Category category);
    Category update(Long id, Category category);

}
