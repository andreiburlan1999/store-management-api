package com.projects.storemanagement.controller;

import com.projects.storemanagement.entity.User;
import com.projects.storemanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        user.setPassword("*");
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        List<User> users = userService.findAll();
        users.forEach(user -> user.setPassword("*"));
        return users;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        User createdUser = userService.create(user);
        createdUser.setPassword("*");
        return createdUser;
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.update(id, user);
        updatedUser.setPassword("*");
        return updatedUser;
    }

    @GetMapping("/current")
    public User getCurrentUser() {
        User user = userService.getCurrentUser();
        user.setPassword("*");
        return user;
    }

}
