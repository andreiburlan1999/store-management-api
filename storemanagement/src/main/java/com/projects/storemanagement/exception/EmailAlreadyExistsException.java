package com.projects.storemanagement.exception;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email) {
        super("There is already an existing customer with email " + email);
    }
}
