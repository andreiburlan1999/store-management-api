package com.projects.storemanagement.service;

import com.projects.storemanagement.controller.dto.ProductDTO;
import com.projects.storemanagement.entity.Category;
import com.projects.storemanagement.entity.Product;
import com.projects.storemanagement.exception.CategoryNotFoundException;
import com.projects.storemanagement.exception.ProductNotFoundException;
import com.projects.storemanagement.exception.ProductStatusIsNotValidException;
import com.projects.storemanagement.repository.CategoryRepository;
import com.projects.storemanagement.repository.ProductRepository;
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

public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        Product product = new Product();
        product.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product foundProduct = productService.findById(1L);

        assertNotNull(foundProduct);
        assertEquals(1L, foundProduct.getId());
    }

    @Test
    void testFindByIdThrowsException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.findById(1L));
    }

    @Test
    void testFindAll() {
        Product product1 = new Product();
        product1.setId(1L);
        Product product2 = new Product();
        product2.setId(2L);
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        List<Product> products = productService.findAll();

        assertNotNull(products);
        assertEquals(2, products.size());
    }

    @Test
    void testCreate() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Test Product");
        productDTO.setPrice(99.99);
        productDTO.setQuantity(10);
        productDTO.setStatus("enabled");
        productDTO.setCategoryId(1L);

        Category category = new Category();
        category.setId(1L);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Product product = new Product();
        product.setId(1L);
        product.setCategory(category);
        product.setStatus(productDTO.getStatus());
        product.setName(productDTO.getName());
        product.setQuantity(productDTO.getQuantity());
        product.setPrice(productDTO.getPrice());
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product createdProduct = productService.create(productDTO);

        assertNotNull(createdProduct);
        assertEquals("Test Product", createdProduct.getName());
        assertEquals(99.99, createdProduct.getPrice());
        assertEquals(10, createdProduct.getQuantity());
        assertEquals("enabled", createdProduct.getStatus());
    }

    @Test
    void testCreateThrowsProductStatusException() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Test Product");
        productDTO.setPrice(99.99);
        productDTO.setQuantity(10);
        productDTO.setStatus("invalid");
        productDTO.setCategoryId(1L);

        assertThrows(ProductStatusIsNotValidException.class, () -> productService.create(productDTO));
    }

    @Test
    void testCreateThrowsCategoryException() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Test Product");
        productDTO.setPrice(99.99);
        productDTO.setQuantity(10);
        productDTO.setStatus("enabled");
        productDTO.setCategoryId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> productService.create(productDTO));
    }

    @Test
    void testUpdate() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Updated Product");
        productDTO.setPrice(199.99);
        productDTO.setQuantity(15);
        productDTO.setStatus("disabled");
        productDTO.setCategoryId(2L);

        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setName("Old Product");
        existingProduct.setPrice(49.99);
        existingProduct.setQuantity(10);
        existingProduct.setStatus("enabled");
        when(productRepository.existsById(1L)).thenReturn(true);
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));

        Category category = new Category();
        category.setId(2L);
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));

        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(199.99);
        updatedProduct.setQuantity(15);
        updatedProduct.setStatus("disabled");
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        Product result = productService.update(1L, productDTO);

        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
        assertEquals(199.99, result.getPrice());
        assertEquals(15, result.getQuantity());
        assertEquals("disabled", result.getStatus());
    }

    @Test
    void testUpdateThrowsNotFoundException() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Updated Product");
        productDTO.setPrice(199.99);
        productDTO.setQuantity(15);
        productDTO.setStatus("disabled");
        productDTO.setCategoryId(2L);

        when(productRepository.existsById(2L)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> productService.update(2L, productDTO));
    }

    @Test
    void testUpdateThrowsStatusException() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Updated Product");
        productDTO.setPrice(199.99);
        productDTO.setQuantity(15);
        productDTO.setStatus("invalid");
        productDTO.setCategoryId(2L);

        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setName("Old Product");
        existingProduct.setPrice(49.99);
        existingProduct.setQuantity(10);
        existingProduct.setStatus("enabled");
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.existsById(1L)).thenReturn(true);

        assertThrows(ProductStatusIsNotValidException.class, () -> productService.update(1L, productDTO));
    }

    @Test
    void testUpdateProduct_CategoryNotFound() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Updated Product");
        productDTO.setPrice(199.99);
        productDTO.setQuantity(15);
        productDTO.setStatus("disabled");
        productDTO.setCategoryId(2L);

        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setName("Old Product");
        existingProduct.setPrice(49.99);
        existingProduct.setQuantity(10);
        existingProduct.setStatus("enabled");
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.existsById(1L)).thenReturn(true);
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> productService.update(1L, productDTO));
    }

    @Test
    void testDisable() {
        Product product = new Product();
        product.setId(1L);
        product.setStatus("enabled");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.disable(1L);

        assertEquals("disabled", product.getStatus());
    }

    @Test
    void testFindByCategory() {
        Product product1 = new Product();
        product1.setId(1L);
        Product product2 = new Product();
        product2.setId(2L);
        when(productRepository.findByCategoryId(1L)).thenReturn(List.of(product1, product2));

        List<Product> products = productService.findByCategory(1L);

        assertNotNull(products);
        assertEquals(2, products.size());
    }

}
