package com.projects.storemanagement.exception;

public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException(String username) {
        super("There is already an existing user with username " + username);
    }

}
