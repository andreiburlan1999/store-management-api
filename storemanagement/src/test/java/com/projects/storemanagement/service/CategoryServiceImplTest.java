package com.projects.storemanagement.service;

import com.projects.storemanagement.entity.Category;
import com.projects.storemanagement.exception.CategoryNotFoundException;
import com.projects.storemanagement.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category foundCategory = categoryService.findById(1L);

        assertNotNull(foundCategory);
        assertEquals("Electronics", foundCategory.getName());
    }

    @Test
    void testFindByIdThrowsException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.findById(1L));
    }

    @Test
    void testFindAll() {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Electronics");
        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Books");
        when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));

        List<Category> categories = categoryService.findAll();

        assertNotNull(categories);
        assertEquals(2, categories.size());
    }

    @Test
    void testCreate() {
        Category category = new Category();
        category.setName("Toys");
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category createdCategory = categoryService.create(category);

        assertNotNull(createdCategory);
        assertEquals("Toys", createdCategory.getName());
    }

    @Test
    void testUpdate() {
        Category existingCategory = new Category();
        existingCategory.setId(1L);
        existingCategory.setName("Electronics");
        when(categoryRepository.existsById(1L)).thenReturn(true);

        Category updatedCategory = new Category();
        updatedCategory.setId(1L);
        updatedCategory.setName("Home Appliances");
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        Category result = categoryService.update(1L, updatedCategory);

        assertNotNull(result);
        assertEquals("Home Appliances", result.getName());
    }

    @Test
    void testUpdateThrowsException() {
        Category nonExistingCategory = new Category();
        nonExistingCategory.setId(2L);
        nonExistingCategory.setName("Non-existent Category");
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.update(2L, nonExistingCategory));
    }

}
