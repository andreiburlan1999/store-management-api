package com.projects.storemanagement.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(long id) {
        super("User not found with id " + id);
    }
    public UserNotFoundException() {
        super("The user was not found");
    }

}
