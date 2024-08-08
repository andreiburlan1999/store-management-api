package com.projects.storemanagement.exception;

public class ProductIsNotAvailableException extends RuntimeException {

    public ProductIsNotAvailableException(long id) {
        super("Product with id " + id + " is not available");
    }

}
