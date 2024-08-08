package com.projects.storemanagement.exception;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(long id) {
        super("Category not found with id " + id);
    }

}
