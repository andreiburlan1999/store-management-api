package com.projects.storemanagement.service;

import com.projects.storemanagement.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User findById(Long id);
    List<User> findAll();
    User create(User user);
    User update(Long id, User user);
    Optional<User> findByUsername(String username);

}
