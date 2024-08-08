package com.projects.storemanagement.service;

import com.projects.storemanagement.entity.Product;
import com.projects.storemanagement.exception.ProductNotFoundException;
import com.projects.storemanagement.exception.ProductStatusIsNotValidException;
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
        Product product = new Product();
        product.setStatus("enabled");

        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product createdProduct = productService.create(product);

        assertNotNull(createdProduct);
        assertEquals("enabled", createdProduct.getStatus());
    }

    @Test
    void testCreateThrowsException() {
        Product product = new Product();
        product.setStatus("invalid");

        assertThrows(ProductStatusIsNotValidException.class, () -> productService.create(product));
    }

    @Test
    void testUpdate() {
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setStatus("enabled");
        when(productRepository.existsById(1L)).thenReturn(true);

        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setStatus("enabled");

        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        Product result = productService.update(1L, updatedProduct);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testUpdateThrowsNotFoundException() {
        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setStatus("enabled");
        when(productRepository.existsById(1L)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> productService.update(1L, updatedProduct));
    }

    @Test
    void testUpdateThrowsStatusException() {
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setStatus("enabled");
        when(productRepository.existsById(1L)).thenReturn(true);

        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setStatus("invalid");

        assertThrows(ProductStatusIsNotValidException.class, () -> productService.update(1L, updatedProduct));
    }

    @Test
    void testDisableProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setStatus("enabled");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.disableProduct(1L);

        assertEquals("disabled", product.getStatus());
    }

    @Test
    void testFindProductsByCategory() {
        Product product1 = new Product();
        product1.setId(1L);
        Product product2 = new Product();
        product2.setId(2L);
        when(productRepository.findByCategoryId(1L)).thenReturn(List.of(product1, product2));

        List<Product> products = productService.findProductsByCategory(1L);

        assertNotNull(products);
        assertEquals(2, products.size());
    }

}
