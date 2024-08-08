package com.projects.storemanagement.exception;

public class ProductStatusIsNotValidException extends RuntimeException {

    public ProductStatusIsNotValidException() {
        super("The status must be 'enabled' or 'disabled'");
    }

}
