package com.projects.storemanagement.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(long id) {
        super("Product not found with id " + id);
    }

}
