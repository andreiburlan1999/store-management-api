package com.projects.storemanagement.exception;

public class ProductsInOrderNotFoundException extends RuntimeException {

    public ProductsInOrderNotFoundException() {
        super("There are no products in the order");
    }

}
