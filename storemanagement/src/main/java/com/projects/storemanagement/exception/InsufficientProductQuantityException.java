package com.projects.storemanagement.exception;

public class InsufficientProductQuantityException extends RuntimeException {

    public InsufficientProductQuantityException(Long id, int quantity) {
        super("The quantity of the product with id " + id + " is insufficient in order to buy the quantity of " + quantity);
    }

}
