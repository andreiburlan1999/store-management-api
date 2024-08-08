package com.projects.storemanagement.exception;

public class ProductsInOrderNotFound extends RuntimeException {

    public ProductsInOrderNotFound() {
        super("There are no products in the order");
    }

}
